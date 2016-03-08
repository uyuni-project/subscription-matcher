package com.suse.matcher.solver;

import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveIteratorFactory;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.Iterator;
import java.util.Random;

/**
 * A factory for {@link MatchMoveIterator}s.
 */
public class MatchMoveIteratorFactory implements MoveIteratorFactory {

    /** {@inheritDoc} */
    @Override
    public long getSize(ScoreDirector director) {
        // we generate exactly one move per Match
        return getAssignment(director).getMatches().size();
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Move> createRandomMoveIterator(ScoreDirector director, Random random) {
        return new MatchMoveIterator(getAssignment(director), random);
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Move> createOriginalMoveIterator(ScoreDirector director) {
        throw new UnsupportedOperationException("ORIGINAL selectionOrder is not supported.");
    }

    private Assignment getAssignment(ScoreDirector director) {
        return (Assignment)director.getWorkingSolution();
    }
}
