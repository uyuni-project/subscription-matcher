package com.suse.matcher;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import com.suse.matcher.facts.Timestamp;
import com.suse.matcher.facts.HostGuest;
import com.suse.matcher.facts.Message;
import com.suse.matcher.facts.PartialMatch;
import com.suse.matcher.facts.PinnedMatch;
import com.suse.matcher.facts.Product;
import com.suse.matcher.facts.Subscription;
import com.suse.matcher.facts.SubscriptionProduct;
import com.suse.matcher.facts.System;
import com.suse.matcher.facts.InstalledProduct;
import com.suse.matcher.json.JsonInput;
import com.suse.matcher.json.JsonMatch;
import com.suse.matcher.json.JsonMessage;
import com.suse.matcher.json.JsonOutput;
import com.suse.matcher.json.JsonProduct;
import com.suse.matcher.json.JsonSubscription;
import com.suse.matcher.json.JsonSystem;
import com.suse.matcher.solver.Assignment;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

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

        result.add(new Timestamp(timestamp));

        for (JsonSystem system : input.getSystems()) {
            result.add(new System(system.getId(), system.getName(), system.getCpus(), system.getPhysical()));
            for (Long guestId : system.getVirtualSystemIds()) {
                result.add(new HostGuest(system.getId(), guestId));
            }
            for (Long productId : system.getProductIds()) {
                result.add(new InstalledProduct(system.getId(), productId));
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
        Date timestamp = assignment.getProblemFactStream(Timestamp.class)
                .findFirst().get().timestamp;

        List<JsonMatch> confirmedMatches = getMatches(assignment, false);

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
     * Returns a list of {@link JsonMatch}es from the {@link Assignment}.
     * @param assignment the assignment
     * @param confirmedOnly true if only confirmed matches should be returned
     * @return matches
     */
    public static List<JsonMatch> getMatches(Assignment assignment, boolean confirmedOnly) {
        Set<Integer> confirmedGroupIds = assignment.getMatches().stream()
                .filter(m -> m.confirmed)
                .map(m -> m.id)
                .collect(toSet());

        return assignment.getProblemFactStream(PartialMatch.class)
            .map(m -> new JsonMatch(
                m.systemId,
                m.subscriptionId,
                m.productId,
                m.cents,
                confirmedGroupIds.contains(m.groupId)
            ))
            .sorted((a, b) -> new CompareToBuilder()
                .append(a.getSystemId(), b.getSystemId())
                .append(a.getProductId(), b.getProductId())
                .append(a.getSubscriptionId(), b.getSubscriptionId())
                .append(a.getCents(), b.getCents())
                .append(a.getConfirmed(), b.getConfirmed())
                .toComparison()
            )
            .filter(m -> (!confirmedOnly) || m.getConfirmed())
            .collect(toList());
    }
}
