package com.suse.matcher.solver;

import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveIteratorFactory;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.Iterator;
import java.util.Random;

/**
 * A factory for {@link MatchMoveIterator}s.
 */
public class MatchMoveIteratorFactory implements MoveIteratorFactory<Assignment> {

    /** {@inheritDoc} */
    @Override
    public long getSize(ScoreDirector<Assignment> director) {
        // we generate exactly one move per Match
        return director.getWorkingSolution().getMatches().size();
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<MatchMove> createRandomMoveIterator(ScoreDirector<Assignment> director, Random random) {
        return new MatchMoveIterator(director.getWorkingSolution(), random);
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<MatchMove> createOriginalMoveIterator(ScoreDirector<Assignment> director) {
        throw new UnsupportedOperationException("ORIGINAL selectionOrder is not supported.");
    }

}
