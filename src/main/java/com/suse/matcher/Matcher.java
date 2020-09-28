package com.suse.matcher;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import com.suse.matcher.facts.PartialMatch;
import com.suse.matcher.facts.InstalledProduct;
import com.suse.matcher.json.JsonInput;
import com.suse.matcher.solver.Assignment;
import com.suse.matcher.solver.Match;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

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
     * @return an object summarizing the match
     */
    public Assignment match(JsonInput input) {
        Assignment assignment = computeAssignment(input);

        OptaPlanner optaPlanner = new OptaPlanner(assignment, testing);
        Assignment result = optaPlanner.getResult();

        // add user messages taking rule engine deductions and CSP solver output into account
        MessageCollector.addMessages(result);

        return result;
    }

    /**
     * Convert input, feed it to Drools and compute the {@link Assignment} for OptaPlanner.
     *
     * @param input the JSON input
     * @return the {@link Assignment} for OptaPlanner
     */
    public Assignment computeAssignment(JsonInput input) {
        // convert inputs into facts the rule engine can reason about
        Collection<Object> baseFacts = FactConverter.convertToFacts(input);

        // activate the rule engine to deduce more facts
        Drools drools = new Drools(baseFacts);
        Collection<Object> deducedFacts = drools.getResult().stream()
            .map(o -> (Object) o)
            .collect(toList());

        // among deductions, the rule engine determines system to subscription "matchability":
        // whether a subscription can be assigned to a system without taking other assignments into account.
        // this is represented by Match objects, divide them from other facts
        List<Match> matches = getMatches(deducedFacts);

        logger.info("Found {} matches", matches.size());
        if (logger.isTraceEnabled()) {
            matches.forEach(m -> {
                logger.trace(m.toString());
                getPartialMatches(deducedFacts)
                    .filter(p -> p.groupId == m.id)
                    .sorted()
                    .map(o -> o.toString())
                    .forEach(s -> logger.trace(s));
                ;
            });
        }

        // compute the map of conflicts between Matches
        // this is used by the CSP solver to avoid bad solutions
        Map<Integer, List<List<Integer>>> conflictMap = getConflictMap(deducedFacts);

        // compute sorted partial matches for caching
        List<PartialMatch> sortedPartialMatches = getPartialMatches(deducedFacts).sorted().distinct().collect(toList());

        // activate the CSP solver with all deduced facts as inputs
        return new Assignment(matches, deducedFacts, conflictMap, sortedPartialMatches);
    }

    private Stream<PartialMatch> getPartialMatches(Collection<Object> deducedFacts) {
        return deducedFacts.stream()
                .filter(f -> f instanceof PartialMatch)
                .map(o -> (PartialMatch)o);
    }

    private List<Match> getMatches(Collection<Object> deducedFacts) {
        return getPartialMatches(deducedFacts)
            .map(p -> p.groupId)
            .sorted()
            .distinct()
            .map(id -> new Match(id, null))
            .collect(toList());
    }

    private Map<Integer, List<List<Integer>>> getConflictMap(Collection<Object> deducedFacts) {
        // group ids in conflicting sets
        // "conflicting" means they target the same (system, product) couple
        Map<InstalledProduct, Set<Integer>> conflicts = getPartialMatches(deducedFacts).collect(
            groupingBy(m -> new InstalledProduct(m.systemId, m.productId),
            mapping(p -> p.groupId, toCollection(TreeSet::new)))
        );

        // discard the above map keys, we only care about values (conflict sets)
        // also delete any duplicated set
        // finally turn all sets to arrays, which are quicker to scan
        List<List<Integer>> conflictList = conflicts.values().stream()
            .distinct()
            .map(s -> new ArrayList<>(s))
            .collect(toList());

        // now build a map from each Match id
        // to all of the conflict sets in which it is in
        return getMatches(deducedFacts).stream()
            .collect(toMap(
                    m -> m.id,
                    m -> conflictList.stream()
                       .filter(s -> Collections.binarySearch(s, m.id) >= 0)
                       .collect(toList())
            ));
    }
}
