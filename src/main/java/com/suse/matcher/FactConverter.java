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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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

        for (JsonSystem system : input.systems) {
            result.add(new System(system.id, system.name, system.cpus, system.physical));
            for (Long guestId : system.virtualSystemIds) {
                result.add(new HostGuest(system.id, guestId));
            }
            for (Long productId : system.productIds) {
                result.add(new SystemProduct(system.id, productId));
            }
        }

        for (JsonProduct product : input.products) {
            result.add(new Product(product.id, product.name));
        }

        for (JsonSubscription subscription : input.subscriptions) {
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

        for (JsonMatch match : input.pinnedMatches) {
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
        JsonOutput output = new JsonOutput();

        output.timestamp = assignment.getProblemFactStream(CurrentTime.class)
                .findFirst().get().timestamp;

        output.confirmedMatches = getConfirmedMatches(assignment).stream()
                .sorted()
                .map(m -> new JsonMatch(
                        m.systemId,
                        m.subscriptionId,
                        m.productId,
                        m.cents
                ))
                .collect(Collectors.toList());

        output.messages = assignment.getProblemFactStream(Message.class)
                .sorted()
                .map(m -> new JsonMessage(
                        m.type,
                        m.data
                ))
                .collect(Collectors.toList());

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

        Stream<Match> freeMatches = assignment.getProblemFactStream(FreeMatch.class)
                .filter(m -> nonFreeIds.contains(m.requiredMatchId))
                .map(m -> new Match(null, m.systemId, m.productId, m.subscriptionId, 0));

        return concat(nonFreeMatches.stream(), freeMatches)
            .sorted()
            .collect(toList());
    }
}
