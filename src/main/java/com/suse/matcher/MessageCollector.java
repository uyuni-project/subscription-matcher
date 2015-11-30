package com.suse.matcher;

import com.suse.matcher.facts.Message;
import com.suse.matcher.facts.PinnedMatch;
import com.suse.matcher.facts.Subscription;
import com.suse.matcher.solver.Assignment;
import com.suse.matcher.solver.Match;

import org.apache.commons.math3.util.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Generates Messages facts and adds them to an Assignment.
 */
public class MessageCollector {

    /**
     * Takes an Assignment after OptaPlanner is done with it in order to add user message objects.
     *
     * @param assignment the assignment
     */
    public static void addMessages(Assignment assignment) {
        // filter out interesting collections from facts
        Stream<PinnedMatch> pinnedMatchFacts = assignment.getProblemFacts().stream()
                .filter(object -> object instanceof PinnedMatch)
                .map(object -> (PinnedMatch) object);

        Collection<Match> confirmedMatchFacts = assignment.getMatches().stream()
                .filter(match -> match.confirmed)
                .collect(Collectors.toList());

        Collection<Subscription> subscriptions = assignment.getProblemFacts().stream()
                .filter(object -> object instanceof Subscription)
                .map(object -> (Subscription) object)
                .filter(s -> s.ignored == false)
                .collect(Collectors.toList());

        // prepare map from (system id, product id) to Match object
        Map<Pair<Long, Long>, Match> matchMap = new HashMap<>();
        for (Match match : confirmedMatchFacts) {
            matchMap.put(new Pair<>(match.systemId, match.productId), match);
        }

        // add messages about unsatisfied pins
        Collection<Message> messages = new LinkedList<Message>();
        pinnedMatchFacts.forEach(match -> {
            Match actualMatch = matchMap.get(new Pair<Long, Long>(match.systemId, match.productId));
            if (actualMatch == null || !match.subscriptionId.equals(actualMatch.subscriptionId)) {
                Message message = new Message("unsatisfied_pinned_match", new TreeMap<String, String>(){{
                    put("system_id", match.systemId.toString());
                    put("subscription_id", match.subscriptionId.toString());
                    put("product_id", match.productId.toString());
                }});
                messages.add(message);
            }
        });

        // add messages about unknown part numbers
        Set<String> unknownPartNumbers = new TreeSet<>();
        for (Subscription subscription : subscriptions) {
            if (subscription.policy == null && subscription.partNumber != null) {
                unknownPartNumbers.add(subscription.partNumber);
            }
        }
        for (String partNumber : unknownPartNumbers) {
            Message message = new Message("unknown_part_number", new TreeMap<String, String>(){{
                put("part_number", partNumber);
            }});
            messages.add(message);
        }

        assignment.getProblemFacts().addAll(messages);
    }
}
