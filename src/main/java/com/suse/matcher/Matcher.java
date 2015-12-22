package com.suse.matcher;

import com.suse.matcher.facts.FreeMatch;
import com.suse.matcher.facts.PossibleMatch;
import com.suse.matcher.json.JsonInput;
import com.suse.matcher.solver.Assignment;
import com.suse.matcher.solver.Match;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
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
     * @param input a JSON input data blob
     * @param timestamp the timestamp for this matching
     * @return an object summarizing the match
     */
    public Assignment match(JsonInput input, Date timestamp) {
        // convert inputs into facts the rule engine can reason about
        Collection<Object> baseFacts = FactConverter.convertToFacts(input, timestamp);

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

                Match match = new Match(possibleMatch.id, possibleMatch.systemId, possibleMatch.productId, possibleMatch.subscriptionId, possibleMatch.cents);
                matches.add(match);
            }
            else {
                otherFacts.add(fact);
            }
        }

        if (logger.isDebugEnabled()) {
            deducedFacts.stream()
                .filter(o -> (o instanceof PossibleMatch) || (o instanceof FreeMatch))
                .map(o -> o.toString())
                .sorted()
                .forEach(s -> logger.debug(s));
        }

        // activate the CSP solver with all deduced facts as inputs
        OptaPlanner optaPlanner = new OptaPlanner(new Assignment(matches, otherFacts));
        Assignment result = optaPlanner.getResult();

        // add user messages taking rule engine deductions and CSP solver output into account
        MessageCollector.addMessages(result);

        return result;
    }
}
