package com.suse.matcher;

import com.suse.matcher.facts.InstalledProduct;
import com.suse.matcher.facts.PotentialMatch;
import com.suse.matcher.json.JsonInput;
import com.suse.matcher.solver.Assignment;
import com.suse.matcher.solver.Match;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Matches a list of systems to a list of subscriptions.
 */
public class Matcher {

    /** Logger instance. */
    private static final Logger LOGGER = LogManager.getLogger(Matcher.class);

    /** true if the matcher is being tested. */
    private final boolean testing;

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
     * @return an object summarizing the match
     */
    public Assignment match(JsonInput input) {
        // convert inputs into facts the rule engine can reason about
        Collection<Object> baseFacts = FactConverter.convertToFacts(input);

        // activate the rule engine to deduce more facts
        Drools drools = new Drools(baseFacts);
        Collection<Object> deducedFacts = drools.getResult();

        // among deductions, the rule engine determines system to subscription "matchability":
        // whether a subscription can be assigned to a system without taking other assignments into account.
        // this is represented by Match objects, divide them from other facts
        List<Match> matches = getMatches(deducedFacts);

        LOGGER.info("Found {} matches", matches.size());
        if (LOGGER.isTraceEnabled()) {
            matches.forEach(m -> {
                LOGGER.trace(m.toString());
                getPotentialMatches(deducedFacts)
                    .filter(p -> p.groupId == m.id)
                    .sorted()
                    .map(o -> o.toString())
                    .forEach(s -> LOGGER.trace(s));
            });
        }

        // compute the map of conflicts between Matches
        // this is used by the CSP solver to avoid bad solutions
        Map<Integer, List<List<Integer>>> conflictMap = getConflictMap(deducedFacts);

        // compute sorted potential matches for caching
        List<PotentialMatch> sortedPotentialMatches = getPotentialMatches(deducedFacts).sorted().distinct().collect(Collectors.toList());

        // activate the CSP solver with all deduced facts as inputs
        OptaPlanner optaPlanner = new OptaPlanner(
                new Assignment(matches, deducedFacts, conflictMap, sortedPotentialMatches), testing);
        Assignment result = optaPlanner.getResult();

        // add user messages taking rule engine deductions and CSP solver output into account
        MessageCollector.addMessages(result);

        return result;
    }

    private Stream<PotentialMatch> getPotentialMatches(Collection<Object> deducedFacts) {
        return deducedFacts.stream()
                .filter(f -> f instanceof PotentialMatch)
                .map(o -> (PotentialMatch)o);
    }

    private List<Match> getMatches(Collection<Object> deducedFacts) {
        return getPotentialMatches(deducedFacts)
            .map(p -> p.groupId)
            .sorted()
            .distinct()
            .map(id -> new Match(id, null))
            .collect(Collectors.toList());
    }

    private Map<Integer, List<List<Integer>>> getConflictMap(Collection<Object> deducedFacts) {
        // group ids in conflicting sets
        // "conflicting" means they target the same (system, product) couple
        Map<InstalledProduct, Set<Integer>> conflicts = getPotentialMatches(deducedFacts).collect(
            Collectors.groupingBy(m -> new InstalledProduct(m.systemId, m.productId),
            Collectors.mapping(p -> p.groupId, Collectors.toCollection(TreeSet::new)))
        );

        // discard the above map keys, we only care about values (conflict sets)
        // also delete any duplicated set
        // finally turn all sets to arrays, which are quicker to scan
        List<List<Integer>> conflictList = conflicts.values().stream()
            .distinct()
            .map(s -> new ArrayList<>(s))
            .collect(Collectors.toList());

        // now build a map from each Match id
        // to all of the conflict sets in which it is in
        return getMatches(deducedFacts).stream()
            .collect(Collectors.toMap(
                    m -> m.id,
                    m -> conflictList.stream()
                       .filter(s -> Collections.binarySearch(s, m.id) >= 0)
                       .collect(Collectors.toList())
            ));
    }
}
