package com.suse.matcher;

import com.suse.matcher.facts.PossibleMatch;
import com.suse.matcher.json.JsonMatch;
import com.suse.matcher.json.JsonSubscription;
import com.suse.matcher.json.JsonSystem;
import com.suse.matcher.solver.Assignment;
import com.suse.matcher.solver.Match;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

/**
 * Matches a list of systems to a list of subscriptions.
 */
public class Matcher {

    /** Logger instance. */
    private final Logger logger = LoggerFactory.getLogger(Matcher.class);

    /**
     * Matches a list of systems to a list of subscriptions.
     *
     * @param systems the systems
     * @param subscriptions the subscriptions
     * @param pinnedMatches a list of user-preferred system-subscription matches
     * @param timestamp the timestamp for this matching
     * @return an object summarizing the match
     */
    public Assignment match(List<JsonSystem> systems, List<JsonSubscription> subscriptions, List<JsonMatch> pinnedMatches, Date timestamp) {
        // convert inputs into facts the rule engine can reason about
        Collection<Object> baseFacts = FactConverter.convertToFacts(systems, subscriptions, pinnedMatches, timestamp);

        // activate the rule engine to deduce more facts
        Drools drools = new Drools(baseFacts);
        Collection<? extends Object> deducedFacts = drools.getResult();

        // among deductions, the rule engine determines system to subscription "matchability":
        // whether a subscription can be assigned to a system without taking other assignments into account.
        // this is represented by Match objects, divide them from other facts
        Collection<Match> matches = new TreeSet<>();
        Collection<Object> otherFacts = new LinkedList<>();
        for (Object fact : deducedFacts) {
            if (fact instanceof PossibleMatch) {
                PossibleMatch possibleMatch = (PossibleMatch) fact;
                logger.debug("Deduced: {}", possibleMatch);

                Match match = new Match(possibleMatch.systemId, possibleMatch.productId, possibleMatch.subscriptionId, possibleMatch.cents);
                matches.add(match);
            }
            else {
                otherFacts.add(fact);
            }
        }

        // activate the CSP solver with all deduced facts as inputs
        OptaPlanner optaPlanner = new OptaPlanner(new Assignment(matches, otherFacts));
        Assignment result = optaPlanner.getResult();

        // add user messages taking rule engine deductions and CSP solver output into account
        MessageCollector.addMessages(result);

        return result;
    }
}
