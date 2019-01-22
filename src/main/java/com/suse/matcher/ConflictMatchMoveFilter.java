package com.suse.matcher;

import com.suse.matcher.solver.Assignment;
import com.suse.matcher.solver.Match;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.ChangeMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Filters ChangeMoves by only accepting those that do not lead to conflicts.
 */
public class ConflictMatchMoveFilter implements SelectionFilter<Assignment, ChangeMove> {

    /**
     * Default constructor.
     */
    public ConflictMatchMoveFilter() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accept(ScoreDirector<Assignment> director, ChangeMove move) {
        boolean confirmed = (Boolean) move.getPlanningValues().iterator().next();

        // we are confirming a Match
        if (confirmed) {
            Assignment solution = director.getWorkingSolution();
            Match match = (Match) move.getPlanningEntities().iterator().next();
            Set<Integer> conflictingIds = solution.getConflictingMatchIds(match.id)
                .collect(Collectors.toSet());

            // accept this Move only if no conflicting Match has been confirmed already
            return !solution.getMatches().stream()
                .anyMatch(m -> m.confirmed == Boolean.TRUE && conflictingIds.contains(m.id));
        }
        else {
            // leaving a Match unconfirmed is always OK
            return true;
        }
    }
}
