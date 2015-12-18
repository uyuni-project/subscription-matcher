package com.suse.matcher;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.concat;

import com.suse.matcher.facts.CurrentTime;
import com.suse.matcher.facts.FreeMatch;
import com.suse.matcher.facts.HostGuest;
import com.suse.matcher.facts.Message;
import com.suse.matcher.facts.PinnedMatch;
import com.suse.matcher.facts.Product;
import com.suse.matcher.facts.Subscription;
import com.suse.matcher.facts.SubscriptionProduct;
import com.suse.matcher.facts.System;
import com.suse.matcher.facts.SystemProduct;
import com.suse.matcher.json.JsonInput;
import com.suse.matcher.json.JsonInputPinnedMatch;
import com.suse.matcher.json.JsonInputProduct;
import com.suse.matcher.json.JsonInputSubscription;
import com.suse.matcher.json.JsonInputSystem;
import com.suse.matcher.json.JsonOutput;
import com.suse.matcher.json.JsonOutputMessage;
import com.suse.matcher.json.JsonOutputProduct;
import com.suse.matcher.json.JsonOutputSubscription;
import com.suse.matcher.json.JsonOutputSystem;
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
     * @param input a JSON input data blob
     * @param timestamp the timestamp for this set of facts
     * @return a collection of facts
     */
    public static Collection<Object> convertToFacts(JsonInput input, Date timestamp) {
        Collection<Object> result = new LinkedList<Object>();

        result.add(new CurrentTime(timestamp));

        for (JsonInputSystem system : input.systems) {
            result.add(new System(system.id, system.name, system.cpus));
            for (Long guestId : system.virtualSystemIds) {
                result.add(new HostGuest(system.id, guestId));
            }
            for (Long productId : system.productIds) {
                result.add(new SystemProduct(system.id, productId));
            }
        }

        for (JsonInputProduct product : input.products) {
            result.add(new Product(product.id, product.name));
        }

        for (JsonInputSubscription subscription : input.subscriptions) {
            result.add(new Subscription(
                subscription.id,
                subscription.partNumber,
                subscription.name,
                subscription.quantity,
                subscription.startDate,
                subscription.endDate,
                subscription.sccUsername
            ));
            for (Long productId : subscription.productIds) {
                result.add(new SubscriptionProduct(subscription.id, productId));
            }
        }

        for (JsonInputPinnedMatch match : input.pinnedMatches) {
            result.add(new PinnedMatch(match.systemId, match.subscriptionId));
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
        Collection<Match> confirmedMatchFacts = getConfirmedMatches(assignment);

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
        Collection<Subscription> subscriptions = assignment.getProblemFacts().stream()
                .filter(o -> o instanceof Subscription)
                .map(s -> (Subscription) s)
                .filter(s -> !s.ignored)
                .sorted()
                .collect(Collectors.toList());

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

        // fill output object's messages field
        assignment.getProblemFacts().stream()
            .filter(o -> o instanceof Message)
            .map(o -> (Message) o)
            .sorted()
            .forEach(m -> output.messages.add(new JsonOutputMessage(m.type, m.data)));

        return output;
    }

    /**
     * Returns a list of confirmed {@link Match} objects including free ones.
     * @param assignment the assignment
     * @return confirmed matches
     */
    public static List<Match> getConfirmedMatches(Assignment assignment) {
        List<Match> nonFreeMatches = assignment.getMatches().stream()
            .filter(m -> m.confirmed)
            .collect(toList());

        Set<Long> nonFreeIds = nonFreeMatches.stream()
            .map(m -> m.id)
            .collect(toSet());

        Stream<Match> freeMatches = assignment.getProblemFacts().stream()
                .filter(o -> o instanceof FreeMatch)
                .map(o -> (FreeMatch) o)
                .filter(m -> nonFreeIds.contains(m.requiredMatchId))
                .map(m -> new Match(null, m.systemId, m.productId, m.subscriptionId, 0));

        return concat(nonFreeMatches.stream(), freeMatches)
            .sorted()
            .collect(toList());
    }
}
