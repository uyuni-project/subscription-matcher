package com.suse.matcher.solver;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.optaplanner.core.impl.heuristic.move.AbstractMove;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A custom-designed move for this specific problem.
 *
 * This will simply flip multiple "confirmed" bits in an equal number of {@link Match} planning entities.
 */
public class MatchMove extends AbstractMove<Assignment> {

    /** The matches. */
    List<Match> matches;

    /** The confirmed flags. */
    List<Boolean> confirmedFlags;

    /**
     * Instantiates a new match move.
     *
     * @param matchesIn the matches to flip
     * @param confirmedFlagsIn the new values of their confirmed flags. Must match in size()
     */
    public MatchMove(List<Match> matchesIn, List<Boolean> confirmedFlagsIn) {
        matches = matchesIn;
        confirmedFlags = confirmedFlagsIn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doMoveOnGenuineVariables(ScoreDirector<Assignment> director) {
        for (int i = 0; i < matches.size(); i++) {
            Match m = matches.get(i);
            director.beforeVariableChanged(m, "confirmed");
            m.setConfirmed(confirmedFlags.get(i));
            director.afterVariableChanged(m, "confirmed");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMoveDoable(ScoreDirector<Assignment> director) {
        // MatchMoveIterator constructs doable MatchMoves only
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractMove<Assignment> createUndoMove(ScoreDirector<Assignment> director) {
        List<Boolean> newConfirmedFlags = matches.stream()
                .map(m -> m.confirmed)
                .collect(Collectors.toList());

        return new MatchMove(matches, newConfirmedFlags);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Match> getPlanningEntities() {
        return matches;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Boolean> getPlanningValues() {
        return confirmedFlags;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            // because of how MatchMoveIterator works, we only need to check
            // Match objects - confirmedFlags is computed from them
            .append(matches)
            .toHashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object objIn) {
        if (!(objIn instanceof MatchMove)) {
            return false;
        }
        MatchMove other = (MatchMove) objIn;
        return new EqualsBuilder()
            // see hashCode()
            .append(matches, other.matches)
            .isEquals();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MatchMove[");
        for (int i = 0; i < matches.size(); i++) {
            builder.append(matches.get(i));
            builder.append("->");
            builder.append(confirmedFlags.get(i));
            if (i < matches.size() - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
