package com.suse.matcher.solver;

import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveIteratorFactory;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
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
        List<Match> matches = getAssignment(director).getMatches();

        // as this method is called once at the beginning of each step, take the chance
        // to ensure we try different moves in different steps if we are unable to try
        // them all (see setSelectedCountLimit in OptaPlanner)
        Collections.shuffle(matches, random);

        return new MatchMoveIterator(getAssignment(director).getConflictMap(), matches.iterator());
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
