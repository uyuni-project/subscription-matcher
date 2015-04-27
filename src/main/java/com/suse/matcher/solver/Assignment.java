package com.suse.matcher.solver;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A set of {@link Match}es which is a subset of all
 * {@link com.suse.matcher.facts.PossibleMatch}es, as produced by OptaPlanner.
 */
@PlanningSolution
public class Assignment implements Solution<HardSoftScore> {

    /** Score of this assignment. */
    private HardSoftScore score;

    /** Match objects that the OptaPlanner will try to assign Kinds to. */
    private Collection<Match> matches;

    /** Other problem facts passed by Drools. */
    private Collection<Object> problemFacts;

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
    public Assignment(Collection<Match> matchesIn, Collection<Object> problemFactsIn) {
        matches = matchesIn;
        problemFacts = problemFactsIn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<? extends Object> getProblemFacts() {
        // those will be inserted in the private OptaPlanner Drools instance
        // so that they can be used in score rules
        return problemFacts;
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
    public Collection<Match> getMatches() {
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
