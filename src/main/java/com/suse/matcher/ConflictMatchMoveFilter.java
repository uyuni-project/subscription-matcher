package com.suse.matcher;

import com.suse.matcher.solver.Assignment;
import com.suse.matcher.solver.Match;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.ChangeMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.List;
import java.util.Map;

/**
 * Filters ChangeMoves by only accepting those changing bits to false.
 */
public class ConflictMatchMoveFilter implements SelectionFilter<ChangeMove> {

    /**
     * Default constructor.
     */
    public ConflictMatchMoveFilter() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accept(ScoreDirector director, ChangeMove move) {
        boolean state = (Boolean) move.getPlanningValues().iterator().next();

        if (state) {
            Match match = (Match) move.getPlanningEntities().iterator().next();
            Map<Match, List<Match>> conflictMap = ((Assignment) director.getWorkingSolution()).getConflictMap();

            return conflictMap.get(match).stream().allMatch(m -> m.confirmed == null || m.confirmed == false);
        }

        return true;
    }
}
