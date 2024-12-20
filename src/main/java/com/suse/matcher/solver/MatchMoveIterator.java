package com.suse.matcher.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Generates {@link MatchMove}s.
 *
 * In particular, every {@link MatchMove} produced by this class will flip the confirmed
 * flag of one {@link Match} and also make sure all incompatible flags are flipped to false
 */
public class MatchMoveIterator implements Iterator<MatchMove> {

    /** Solution instance. */
    private final Assignment assignment;

    /** Iterator over all matches. */
    private final Iterator<Match> iterator;

    /** Map from id to {@link Match}. */
    private final Map<Integer, Match> idMap;

    /**
     * Standard constructor.
     * @param assignmentIn a solution instance
     * @param randomIn a random number generator instance
     */
    public MatchMoveIterator(Assignment assignmentIn, Random randomIn) {
        assignment = assignmentIn;

        List<Match> orderedMatches = new ArrayList<>(assignment.getMatches());
        Collections.shuffle(orderedMatches, randomIn);

        iterator = orderedMatches.iterator();

        idMap = orderedMatches.stream()
            .collect(Collectors.toMap(match -> match.id, match -> match));
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
        ArrayList<Match> matches = new ArrayList<>();
        ArrayList<Boolean> states = new ArrayList<>();

        // pick the match to flip
        Match match = iterator.next();

        // add it, flipped, to the move lists
        boolean newState = !match.confirmed;
        matches.add(match);
        states.add(newState);

        // also make sure any conflicting match is (flipped to) false
        if (newState) {
            assignment.getConflictingMatchIds(match.id)
                .map(id -> idMap.get(id))
                .filter(conflict -> conflict.confirmed)
                .forEach(conflict -> {
                    matches.add(conflict);
                    states.add(false);
                });
        }

        return new MatchMove(matches, states);
    }
}
