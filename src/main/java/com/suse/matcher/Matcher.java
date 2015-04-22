package com.suse.matcher;

import com.suse.matcher.facts.PossibleMatch;
import com.suse.matcher.json.JsonMatch;
import com.suse.matcher.json.JsonOutput;
import com.suse.matcher.json.JsonSubscription;
import com.suse.matcher.json.JsonSystem;
import com.suse.matcher.solver.Assignment;
import com.suse.matcher.solver.Match;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Matches a list of systems to a list of subscriptions.
 */
public class Matcher {

    /**
     * Matches a list of systems to a list of subscriptions.
     *
     * @param systems the systems
     * @param subscriptions the subscriptions
     * @param pinnedMatches a list of user-preferred system-subscription matches
     * @return an object summarizing the match
     */
    public JsonOutput match(List<JsonSystem> systems, List<JsonSubscription> subscriptions, List<JsonMatch> pinnedMatches) {
        // convert inputs into facts the rule engine can reason about
        Collection<Object> baseFacts = FactConverter.convertToFacts(systems, subscriptions, pinnedMatches);

        // activate the rule engine to deduce more facts
        Drools drools = new Drools(baseFacts);
        Collection<? extends Object> deducedFacts = drools.getResult();

        // among deductions, the rule engine determines system to subscription "matchability":
        // whether a subscription can be assigned to a system without taking other assignments into account.
        // this is represented by Match objects, divide them from other facts
        Collection<Match> matches = new LinkedList<>();
        Collection<Object> otherFacts = new LinkedList<>();
        for (Object fact : deducedFacts) {
            if (fact instanceof PossibleMatch) {
                PossibleMatch match = (PossibleMatch) fact;
                matches.add(new Match(match.systemId, match.productId, match.subscriptionId, match.quantity));
            }
            else {
                otherFacts.add(fact);
            }
        }

        // activate the CSP solver with all deduced facts as inputs
        OptaPlanner<Assignment> optaPlanner = new OptaPlanner<Assignment>(new Assignment(matches, otherFacts));
        Assignment result = optaPlanner.getResult();

        // convert output back to output format and return it
        return FactConverter.convertToOutpt(result, systems, subscriptions, pinnedMatches);
    }
}
