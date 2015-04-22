package com.suse.matcher;

import com.suse.matcher.facts.HostGuest;
import com.suse.matcher.facts.PinnedMatch;
import com.suse.matcher.facts.Subscription;
import com.suse.matcher.facts.SubscriptionProduct;
import com.suse.matcher.facts.System;
import com.suse.matcher.facts.SystemProduct;
import com.suse.matcher.facts.Today;
import com.suse.matcher.json.JsonMatch;
import com.suse.matcher.json.JsonOutput;
import com.suse.matcher.json.JsonOutputError;
import com.suse.matcher.json.JsonOutputProduct;
import com.suse.matcher.json.JsonOutputSubscription;
import com.suse.matcher.json.JsonOutputSystem;
import com.suse.matcher.json.JsonSubscription;
import com.suse.matcher.json.JsonSystem;
import com.suse.matcher.solver.Assignment;
import com.suse.matcher.solver.Match;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Converts JSON objects to facts (objects the rule engine and CSP solver can
 * reason about) and vice versa.
 */
public class FactConverter {

    /**
     * Converts JSON objects to facts (inputs to the rule engine).
     *
     * @param systems a list of systems in JSON format
     * @param subscriptions a list of subscriptions in JSON format
     * @param pinnedMatches a list of pinned matches in JSON format
     * @return a collection of facts
     */
    public static Collection<Object> convertToFacts(List<JsonSystem> systems, List<JsonSubscription> subscriptions, List<JsonMatch> pinnedMatches) {
        Collection<Object> result = new LinkedList<Object>();

        result.add(new Today());

        for (JsonSystem system : systems) {
            result.add(new System(system.id, system.cpus));
            for (Long guestId : system.virtualSystemIds) {
                result.add(new HostGuest(system.id, guestId));
            }
            for (Long productId : system.productIds) {
                result.add(new SystemProduct(system.id, productId));
            }
        }

        for (JsonSubscription subscription : subscriptions) {
            result.add(new Subscription(subscription.id, subscription.partNumber, subscription.systemLimit.doubleValue(), subscription.startsAt,
                    subscription.expiresAt, subscription.sccOrgId));
            for (Long productId : subscription.productIds) {
                result.add(new SubscriptionProduct(subscription.id, productId));
            }
        }

        for (JsonMatch match : pinnedMatches) {
            result.add(new PinnedMatch(match.systemId, match.productId, match.subscriptionId));
        }

        return result;
    }

    /**
     * Converts the outputs from CSP solver in output JSON format.
     *
     * @param assignment the assignment object, output of the CSP solver
     * @param systems the systems
     * @param subscriptions the subscriptions
     * @param pinnedMatches the pinned matches
     * @return the output
     */
    @SuppressWarnings("unchecked")
    public static JsonOutput convertToOutpt(Assignment assignment, List<JsonSystem> systems, List<JsonSubscription> subscriptions,
            List<JsonMatch> pinnedMatches) {

        // prepare list of system ids
        Collection<Long> systemIds = new TreeSet<>();
        for (JsonSystem system : systems) {
            systemIds.add(system.id);
        }

        // prepare list of confirmed Matches
        Collection<Match> confirmedMatches = CollectionUtils.select(assignment.getMatches(), new Predicate<Match>() {
            @Override
            public boolean evaluate(Match match) {
                return match.confirmed;
            }
        });

        // prepare map from (system id, product id) to Match object
        Map<Pair<Long, Long>, Match> matchMap = new TreeMap<>();
        for (Match match : confirmedMatches) {
            matchMap.put(new ImmutablePair<>(match.systemId, match.productId), match);
        }

        // prepare multimap from system id to set of product ids
        @SuppressWarnings("rawtypes")
        MultiMap<Long, Long> installationMap = MultiValueMap.multiValueMap(new TreeMap<Long, Collection>(), TreeSet.class);
        for (JsonSystem system : systems) {
            for (Long productId : system.productIds) {
                installationMap.put(system.id, productId);
            }
        }
        for (Match match : confirmedMatches) {
            installationMap.put(match.systemId, match.productId);
        }

        // fill output object's system fields
        JsonOutput output = new JsonOutput();
        for (Long systemId : systemIds) {
            JsonOutputSystem system = new JsonOutputSystem(systemId);

            boolean compliantProductExists = false;
            boolean allProductsCompliant = true;
            Collection<Long> productIds = (Collection<Long>) installationMap.get(systemId);
            if (productIds != null) {
                for (Long productId : productIds) {
                    JsonOutputProduct product = new JsonOutputProduct(productId);

                    Match match = matchMap.get(new ImmutablePair<>(system.id, product.id));
                    if (match != null) {
                        product.subscriptionId = match.subscriptionId;
                        product.subscriptionQuantity = match.quantity;
                    }
                    compliantProductExists = compliantProductExists || (match != null);
                    allProductsCompliant = allProductsCompliant && (match != null);

                    system.products.add(product);
                }
            }

            if (compliantProductExists) {
                if (allProductsCompliant) {
                    output.compliantSystems.add(system);
                }
                else {
                    output.partiallyCompliantSystems.add(system);
                }
            }
            else {
                output.nonCompliantSystems.add(system);
            }
        }

        // fill output object's remaining subscriptions field
        Map<Long, Double> remainings = new TreeMap<>();
        for (JsonSubscription subscription : subscriptions) {
            remainings.put(subscription.id, subscription.systemLimit.doubleValue());
        }
        for (Match match : confirmedMatches) {
            remainings.put(match.subscriptionId, remainings.get(match.subscriptionId) - match.quantity);
        }
        for (JsonSubscription subscription : subscriptions) {
            Double remaining = remainings.get(subscription.id);
            if (remaining > 0) {
                output.remainingSubscriptions.add(new JsonOutputSubscription(subscription.id, remaining));
            }
        }

        // fill output object's errors field
        for (JsonMatch match : pinnedMatches) {
            Match actualMatch = matchMap.get(new ImmutablePair<Long, Long>(match.systemId, match.productId));
            if (actualMatch == null || !match.subscriptionId.equals(actualMatch.subscriptionId)) {
                JsonOutputError error = new JsonOutputError("invalid_pinned_match");
                error.data.put("system_id", match.systemId);
                error.data.put("subscription_id", match.subscriptionId);
                error.data.put("product_id", match.productId);
                output.errors.add(error);
            }
        }

        return output;
    }
}
