package com.suse.matcher;

import com.suse.matcher.facts.Message;
import com.suse.matcher.facts.PinnedMatch;
import com.suse.matcher.json.JsonMatch;
import com.suse.matcher.solver.Assignment;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
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
        Stream<PinnedMatch> pinnedMatchFacts = assignment.getProblemFactStream(PinnedMatch.class);

        Collection<JsonMatch> confirmedMatchFacts = FactConverter.getMatches(assignment);

        // add messages about unsatisfied pins
        Collection<Message> messages = new LinkedList<Message>();
        pinnedMatchFacts
            .filter(pin -> !confirmedMatchFacts.stream() // filter unmatched pins
                    .filter(m -> m.getSubscriptionId().equals(pin.subscriptionId) && m.getSystemId().equals(pin.systemId))
                    .findAny()
                    .isPresent()
            )
            .forEach(unmatchedPin -> {
                Message message = new Message(Message.Level.INFO, "unsatisfied_pinned_match", new TreeMap<>(Map.of(
                    "system_id", unmatchedPin.systemId.toString(),
                    "subscription_id", unmatchedPin.subscriptionId.toString()
                )));
                messages.add(message);
            });

        assignment.getProblemFacts().addAll(messages);
    }
}
