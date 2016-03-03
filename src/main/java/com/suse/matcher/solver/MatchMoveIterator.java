package com.suse.matcher.solver;

import org.optaplanner.core.impl.heuristic.move.Move;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Generates {@link MatchMove}s.
 *
 * In particular, every {@link MatchMove} produced by this class will flip the confirmed
 * flag of one {@link Match} and also make sure all incompatible flags are flipped to false
 */
public class MatchMoveIterator implements Iterator<Move> {

    /** Map from a {@link Match} to a list of other {@link Match}es that are incompatible with it. */
    private final Map<Match, List<Match>> conflicts;

    /** Iterator over all matches. */
    private final Iterator<Match> iterator;

    /**
     * Standard constructor.
     *
     * @param conflictsIn the conflict map
     * @param iteratorIn the iterator over all matches
     */
    public MatchMoveIterator(Map<Match, List<Match>> conflictsIn, Iterator<Match> iteratorIn) {
        conflicts = conflictsIn;
        iterator = iteratorIn;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    /** {@inheritDoc} */
    @Override
    public MatchMove next() {
        // prepare MatchMove's lists
        ArrayList<Match> matches = new ArrayList<Match>();
        ArrayList<Boolean> states = new ArrayList<Boolean>();

        // pick the match to flip
        Match match = iterator.next();

        // add it, flipped, to the move lists
        boolean newState = !match.confirmed;
        matches.add(match);
        states.add(newState);

        // also make sure any conflicting match is (flipped to) false
        if (newState) {
            conflicts.get(match).stream()
                .filter(conflict -> conflict.confirmed)
                .forEach(conflict -> {
                    matches.add(conflict);
                    states.add(false);
                })
            ;
        }

        return new MatchMove(matches, states);
    }
}
