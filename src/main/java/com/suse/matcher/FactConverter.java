package com.suse.matcher;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
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
import com.suse.matcher.json.JsonMatch;
import com.suse.matcher.json.JsonMessage;
import com.suse.matcher.json.JsonOutput;
import com.suse.matcher.json.JsonProduct;
import com.suse.matcher.json.JsonSubscription;
import com.suse.matcher.json.JsonSystem;
import com.suse.matcher.solver.Assignment;
import com.suse.matcher.solver.Match;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
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

        for (JsonSystem system : input.getSystems()) {
            result.add(new System(system.getId(), system.getName(), system.getCpus(), system.getPhysical()));
            for (Long guestId : system.getVirtualSystemIds()) {
                result.add(new HostGuest(system.getId(), guestId));
            }
            for (Long productId : system.getProductIds()) {
                result.add(new SystemProduct(system.getId(), productId));
            }
        }

        for (JsonProduct product : input.getProducts()) {
            result.add(new Product(product.getId(), product.getName(), product.getFree(), product.getBase()));
        }

        for (JsonSubscription subscription : input.getSubscriptions()) {
            result.add(new Subscription(
                    subscription.getId(),
                    subscription.getPartNumber(),
                    subscription.getName(),
                    subscription.getQuantity(),
                    subscription.getStartDate(),
                    subscription.getEndDate(),
                    subscription.getSccUsername()
            ));
            for (Long productId : subscription.getProductIds()) {
                result.add(new SubscriptionProduct(subscription.getId(), productId));
            }
        }

        for (JsonMatch match : input.getPinnedMatches()) {
            result.add(new PinnedMatch(match.getSystemId(), match.getSubscriptionId()));
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
        Date timestamp = assignment.getProblemFactStream(CurrentTime.class)
                .findFirst().get().timestamp;

        List<JsonMatch> confirmedMatches = getMatches(assignment, false).stream()
                .sorted()
                .map(m -> new JsonMatch(
                        m.systemId,
                        m.subscriptionId,
                        m.productId,
                        m.cents,
                        m.confirmed
                ))
                .collect(Collectors.toList());

        List<JsonMessage> messages = assignment.getProblemFactStream(Message.class)
                .sorted()
                .map(m -> new JsonMessage(
                        m.type,
                        m.data
                ))
                .collect(Collectors.toList());

        Map<Long, String> subscriptionPolicies = assignment
                .getProblemFactStream(Subscription.class)
                .filter(s -> s.getPolicy() != null)
                .sorted()
                .collect(Collectors.toMap(
                        s -> s.getId(),
                        s -> s.getPolicy().name().toLowerCase(),
                        throwingMerger(),
                        LinkedHashMap::new));

        return new JsonOutput(timestamp, confirmedMatches, messages, subscriptionPolicies);
    }

    private static BinaryOperator<String> throwingMerger() {
        return (u,v) -> {
            throw new IllegalStateException(String.format("Duplicate key %s", u));
        };
    }

    /**
     * Returns a list of confirmed {@link Match} objects including free ones.
     * @param assignment the assignment
     * @param confirmedOnly true if only confirmed matches should be returned
     * @return confirmed matches
     */
    public static List<Match> getMatches(Assignment assignment, boolean confirmedOnly) {
        Map<Long, Match> matchMap = assignment.getMatches().stream()
            .collect(toMap(m -> m.id, m -> m));

        Stream<Match> freeMatches = assignment.getProblemFactStream(FreeMatch.class)
            .map(m -> new Match(
                    null,
                    m.systemId,
                    m.productId,
                    m.subscriptionId,
                    0,
                    matchMap.get(m.requiredMatchId).confirmed)
             );

        return concat(matchMap.values().stream(), freeMatches)
            .filter(m -> !confirmedOnly || m.confirmed)
            .sorted()
            .collect(toList());
    }
}
