package com.suse.matcher;

import com.suse.matcher.facts.CurrentTime;
import com.suse.matcher.facts.HostGuest;
import com.suse.matcher.facts.PinnedMatch;
import com.suse.matcher.facts.Product;
import com.suse.matcher.facts.Subscription;
import com.suse.matcher.facts.SubscriptionProduct;
import com.suse.matcher.facts.System;
import com.suse.matcher.facts.SystemProduct;
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

import org.apache.commons.math3.util.Pair;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * @param timestamp the timestamp for this set of facts
     * @return a collection of facts
     */
    public static Collection<Object> convertToFacts(List<JsonSystem> systems,
            List<JsonSubscription> subscriptions, List<JsonMatch> pinnedMatches,
            Date timestamp) {
        Collection<Object> result = new LinkedList<Object>();

        result.add(new CurrentTime(timestamp));

        for (JsonSystem system : systems) {
            result.add(new System(system.id, system.cpus));
            for (Long guestId : system.virtualSystemIds) {
                result.add(new HostGuest(system.id, guestId));
            }
            for (Map.Entry<Long, String> product : system.products.entrySet()) {
                result.add(new SystemProduct(system.id, product.getKey()));
                result.add(new Product(product.getKey(), product.getValue()));
            }
        }

        for (JsonSubscription subscription : subscriptions) {
            result.add(new Subscription(subscription.id, subscription.partNumber,
                    subscription.systemLimit, subscription.startsAt,
                    subscription.expiresAt, subscription.sccOrgId));
            for (Long productId : subscription.productIds) {
                result.add(new SubscriptionProduct(subscription.id, productId));
            }
        }

        for (JsonMatch match : pinnedMatches) {
            result.add(
                    new PinnedMatch(match.systemId, match.productId, match.subscriptionId));
        }

        return result;
    }

    /**
     * Converts the outputs from CSP solver in output JSON format.
     *
     * @param assignment the assignment object, output of the CSP solver
     * @return the output
     */
    public static JsonOutput convertToOutput(Assignment assignment) {
        // extract facts from assignment by type
        Collection<Match> confirmedMatchFacts = assignment.getMatches().stream()
                .filter(match -> match.confirmed)
                .collect(Collectors.toList());

        Stream<PinnedMatch> pinnedMatchFacts = assignment.getProblemFacts().stream()
                .filter(object -> object instanceof PinnedMatch)
                .map(object -> (PinnedMatch) object);

        Stream<System> systemFacts = assignment.getProblemFacts().stream()
                .filter(object -> object instanceof System)
                .map(object -> (System) object);

        List<SystemProduct> systemProductFacts = assignment.getProblemFacts().stream()
                .filter(object -> object instanceof SystemProduct)
                .map(object -> (SystemProduct) object)
                .collect(Collectors.toList());

        // prepare map from (system id, product id) to Match object
        Map<Pair<Long, Long>, Match> matchMap = new HashMap<>();
        for (Match match : confirmedMatchFacts) {
            matchMap.put(new Pair<>(match.systemId, match.productId), match);
        }

        // prepare map from system id to set of product ids
        Map<Long, List<Long>> systemMap = systemFacts
                .collect(Collectors.toMap(
                        system -> system.id,
                        system -> Stream.concat(
                                confirmedMatchFacts.stream()
                                    .filter(match -> match.systemId.equals(system.id))
                                    .map(match -> match.productId),
                                systemProductFacts.stream()
                                    .filter(systemProduct -> systemProduct.systemId.equals(system.id))
                                    .map(systemProduct -> systemProduct.productId)
                            ).distinct().sorted().collect(Collectors.toList()),
                        (id1, id2) -> id1,
                        TreeMap::new
                ));

        // fill output object's system fields
        JsonOutput output = new JsonOutput();
        for (Long systemId : systemMap.keySet()) {
            JsonOutputSystem system = new JsonOutputSystem(systemId);

            boolean compliantProductExists = false;
            boolean allProductsCompliant = true;
            Collection<Long> productIds = systemMap.get(systemId);
            if (productIds != null) {
                for (Long productId : productIds) {
                    JsonOutputProduct product = new JsonOutputProduct(productId);

                    Match match = matchMap.get(new Pair<>(system.id, product.id));
                    if (match != null) {
                        product.subscriptionId = match.subscriptionId;
                        product.subscriptionCents = match.cents;
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
        Collection<Subscription> subscriptions = new TreeSet<>();
        for (Object fact : assignment.getProblemFacts()) {
           if (fact instanceof Subscription) {
               subscriptions.add((Subscription)fact);
           }
        }
        Map<Long, Integer> remainings = new TreeMap<>();
        for (Subscription subscription : subscriptions) {
            remainings.put(subscription.id, subscription.quantity * 100);
        }
        for (Match match : confirmedMatchFacts) {
            remainings.put(match.subscriptionId, remainings.get(match.subscriptionId) - match.cents);
        }
        for (Subscription subscription : subscriptions) {
            Integer remaining = remainings.get(subscription.id);
            if (remaining > 0) {
                output.remainingSubscriptions.add(new JsonOutputSubscription(subscription.id, remaining));
            }
        }

        // fill output object's errors field
        // unsatisfied pinned matches
        pinnedMatchFacts.forEach(match -> {
            Match actualMatch = matchMap.get(new Pair<Long, Long>(match.systemId, match.productId));
            if (actualMatch == null || !match.subscriptionId.equals(actualMatch.subscriptionId)) {
                JsonOutputError error = new JsonOutputError("unsatisfied_pinned_match");
                error.data.put("system_id", match.systemId.toString());
                error.data.put("subscription_id", match.subscriptionId.toString());
                error.data.put("product_id", match.productId.toString());
                output.errors.add(error);
            }
        });

        // unknown part numbers
        Set<String> unknownPartNumbers = new TreeSet<>();
        for (Subscription subscription : subscriptions) {
            if (subscription.policy == null && subscription.partNumber != null) {
                unknownPartNumbers.add(subscription.partNumber);
            }
        }
        for (String partNumber : unknownPartNumbers) {
            JsonOutputError error = new JsonOutputError("unknown_part_number");
            error.data.put("part_number", partNumber);
            output.errors.add(error);
        }

        return output;
    }
}
