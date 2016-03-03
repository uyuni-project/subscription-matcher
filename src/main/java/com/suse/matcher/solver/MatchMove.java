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
public class MatchMove extends AbstractMove {

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
    protected void doMoveOnGenuineVariables(ScoreDirector director) {
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
    public boolean isMoveDoable(ScoreDirector director) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Move createUndoMove(ScoreDirector director) {
        List<Boolean> newConfirmedFlags = confirmedFlags.stream()
            .map(b -> !b)
            .collect(Collectors.toList());

        return new MatchMove(matches, newConfirmedFlags);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<? extends Object> getPlanningEntities() {
        return matches;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<? extends Object> getPlanningValues() {
        return confirmedFlags;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
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
