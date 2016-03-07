package com.suse.matcher;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

import com.suse.matcher.facts.PartialMatch;
import com.suse.matcher.json.JsonInput;
import com.suse.matcher.solver.Assignment;
import com.suse.matcher.solver.Match;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;

/**
 * Matches a list of systems to a list of subscriptions.
 */
public class Matcher {

    /** Logger instance. */
    private final Logger logger = LoggerFactory.getLogger(Matcher.class);

    /** true if the matcher is being tested. */
    private boolean testing;

    /**
     * Standard constructor.
     *
     * @param testingIn true if running as a unit test, false otherwise
     */
    public Matcher(boolean testingIn) {
        testing = testingIn;
    }

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
        Collection<Object> deducedFacts = drools.getResult().stream()
            .map(o -> (Object) o)
            .collect(toList());

        // among deductions, the rule engine determines system to subscription "matchability":
        // whether a subscription can be assigned to a system without taking other assignments into account.
        // this is represented by Match objects, divide them from other facts
        Collection<Match> matches = deducedFacts.stream()
            .filter(f -> f instanceof PartialMatch)
            .map(o -> (PartialMatch)o)
            .map(p -> p.groupId)
            .map(id -> new Match(id, null))
            .collect(toCollection(() -> new TreeSet<>()))
        ;

        logger.info("Found {} possible matches", matches.size());
        if (logger.isDebugEnabled()) {
            matches.forEach(m -> {
                logger.debug(m.toString());
                deducedFacts.stream()
                    .filter(o -> o instanceof PartialMatch)
                    .map(o -> (PartialMatch)o)
                    .filter(p -> p.groupId == m.id)
                    .sorted()
                    .map(o -> o.toString())
                    .forEach(s -> logger.debug(s));
                ;
            });
        }

        // activate the CSP solver with all deduced facts as inputs
        OptaPlanner optaPlanner = new OptaPlanner(new Assignment(matches, deducedFacts), testing);
        Assignment result = optaPlanner.getResult();

        // add user messages taking rule engine deductions and CSP solver output into account
        MessageCollector.addMessages(result);

        return result;
    }
}
