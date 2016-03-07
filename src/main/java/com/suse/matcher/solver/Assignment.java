package com.suse.matcher.solver;

import com.suse.matcher.facts.IncompatibleGroups;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A set of {@link Match}es which is a subset of all
 * {@link Match}es, as produced by OptaPlanner.
 */
@PlanningSolution
public class Assignment implements Solution<HardSoftScore> {

    /** Score of this assignment. */
    private HardSoftScore score;

    /** Match objects that the OptaPlanner will try to assign Kinds to. */
    private List<Match> matches;

    /** Other problem facts passed by Drools. */
    private Collection<Object> problemFacts;

    /** Map from each Match to its conflicting Matches. */
    private Map<Match, List<Match>> conflictMap;

    /**
     * Default constructor, required by OptaPlanner.
     */
    public Assignment() {
    }

    /**
     * Standard constructor.
     *
     * @param matchesIn fact corresponding to possible matches
     * @param problemFactsIn any other problem facts
     */
    public Assignment(List<Match> matchesIn, Collection<Object> problemFactsIn) {
        matches = matchesIn;
        problemFacts = problemFactsIn;

        Collection<IncompatibleGroups> incompatibleGroups = getProblemFacts(IncompatibleGroups.class);
        Map<Integer, Match> idMatchMap = matches.stream()
                .collect(Collectors.toMap(m -> m.getId(), m -> m));
        conflictMap = matches.stream()
            .collect(Collectors.toMap(
                match -> match,
                match -> incompatibleGroups.stream()
                    .filter(pair -> match.getId() == pair.getGroupId1() || match.getId() == pair.getGroupId2())
                    .map(pair -> match.getId() == pair.getGroupId1() ? pair.getGroupId2() : pair.getGroupId1())
                    .distinct()
                    .sorted()
                    .map(id -> idMatchMap.get(id))
                    .collect(Collectors.toList())
            )
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Object> getProblemFacts() {
        // those will be inserted in the private OptaPlanner Drools instance
        // so that they can be used in score rules
        return problemFacts;
    }

    /**
     * Returns a stream of problem facts filtered by type.
     *
     * @param <T> type of the facts
     * @param type of the facts
     * @return the facts as stream
     */
    @SuppressWarnings("unchecked") // no way around this in Java 8
    public <T> Stream<T> getProblemFactStream(Class<T> type) {
        return getProblemFacts().stream()
            .filter(o -> type.isAssignableFrom(o.getClass()))
            .map(o -> (T)o);
    }

    /**
     * Returns a Collection of problem facts filtered by type.
     *
     * @param <T> type of the facts
     * @param type of the facts
     * @return the facts as stream
     */
    public <T> Collection<T> getProblemFacts(Class<T> type) {
        return getProblemFactStream(type)
            .collect(Collectors.toList());
    }

    /**
     * Gets the map from each Match to its conflicting Matches.
     *
     * @return the map from each Match to its conflicting Matches
     */
    public Map<Match, List<Match>> getConflictMap() {
        return conflictMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HardSoftScore getScore() {
        return score;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setScore(HardSoftScore scoreIn) {
        score = scoreIn;
    }

    /**
     * Returns Match objects that the OptaPlanner will try to confirm.
     *
     * @return the matches
     */
    @PlanningEntityCollectionProperty
    public List<Match> getMatches() {
        return matches;
    }

    /**
     * Returns values for a {@link Match} confirmed field that OptaPlanner will
     * change.
     *
     * @return the boolean values
     */
    @ValueRangeProvider(id = "booleanRange")
    public List<Boolean> getBooleans() {
        return new ArrayList<Boolean>(){{ add(Boolean.FALSE); add(Boolean.TRUE); }};
    }
}
