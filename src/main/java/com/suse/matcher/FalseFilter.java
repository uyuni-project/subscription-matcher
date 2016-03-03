package com.suse.matcher;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.ChangeMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

/**
 * Filters ChangeMoves by only accepting those changing bits to false.
 */
public class FalseFilter implements SelectionFilter<ChangeMove> {

    /**
     * Default constructor.
     */
    public FalseFilter() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accept(ScoreDirector arg0In, ChangeMove arg1In) {
        return ((Boolean) arg1In.getPlanningValues().iterator().next()) == false;
    }
}
