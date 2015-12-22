package com.suse.matcher;

import com.suse.matcher.facts.Message;
import com.suse.matcher.facts.PinnedMatch;
import com.suse.matcher.solver.Assignment;
import com.suse.matcher.solver.Match;

import java.util.Collection;
import java.util.LinkedList;
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
        Stream<PinnedMatch> pinnedMatchFacts = assignment.getProblemFacts().stream()
                .filter(object -> object instanceof PinnedMatch)
                .map(object -> (PinnedMatch) object);

        Collection<Match> confirmedMatchFacts = FactConverter.getConfirmedMatches(assignment);

        // add messages about unsatisfied pins
        Collection<Message> messages = new LinkedList<Message>();
        pinnedMatchFacts
            .filter(pin -> !confirmedMatchFacts.stream() // filter unmatched pins
                    .filter(m -> m.subscriptionId.equals(pin.subscriptionId) && m.systemId.equals(pin.systemId))
                    .findAny()
                    .isPresent()
            )
            .forEach(unmatchedPin -> {
                Message message = new Message(Message.Level.INFO, "unsatisfied_pinned_match", new TreeMap<String, String>(){{
                    put("system_id", unmatchedPin.systemId.toString());
                    put("subscription_id", unmatchedPin.subscriptionId.toString());
                }});
                messages.add(message);
            });

        assignment.getProblemFacts().addAll(messages);
    }
}
