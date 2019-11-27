/**
 * Copyright (c) 2019 SUSE LLC
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 * Red Hat trademarks are not licensed under GPLv2. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */

package com.suse.matcher;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import com.suse.matcher.facts.PartialMatch;
import com.suse.matcher.solver.Assignment;
import com.suse.matcher.solver.Match;
import com.suse.matcher.solver.MatchMove;

import com.google.common.collect.Streams;

import org.apache.commons.math3.util.Pair;
import org.optaplanner.core.impl.heuristic.move.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collector;

/**
 * Generates {@link MatchMove}s.
 *
 * In particular, every {@link MatchMove} produced by this class will swap the confirmed flag
 * of two {@link Match}es that have same subscription and different "confirmed" flag.
 *
 * All "confirmed" flags of incompatible {@link Match}es are flipped to false.
 */
public class MatchSwapMoveIterator implements Iterator<Move> {

    /** Solution instance. */
    private Assignment assignment;

    /** Iterator over all matches. */
    private final Iterator<Pair<PartialMatch, PartialMatch>> iterator;

    /** Map from id to {@link Match}. */
    private Map<Integer, Match> idMap;

    /**
     * Standard constructor.
     * @param assignmentIn a solution instance
     * @param randomIn a random number generator instance
     */
    public MatchSwapMoveIterator(Assignment assignmentIn, Random randomIn) {
        assignment = assignmentIn;

        List<Match> orderedMatches = new ArrayList<>(assignment.getMatches());

        idMap = orderedMatches.stream()
                .collect(toMap(
                        match -> match.id,
                        match -> match
                ));

        // subscription id -> confirmed/not confirmed -> shuffled list of matches
        Map<Long, Map<Boolean, List<PartialMatch>>> subscriptionMatches = assignmentIn.getSortedPartialMatchesCache()
                .stream()
                .collect(groupingBy(
                        pm -> pm.subscriptionId,
                        TreeMap::new,
                        groupingBy(
                                pm -> idMap.get(pm.groupId).confirmed,
                                TreeMap::new,
                                toShuffledList(randomIn)
                        )));

        // subscription id -> list of pairs of matches containing the subscription, such that a confirmed match
        // is on the left and not confirmed match is on the right in the match
        Map<Long, List<Pair<PartialMatch, PartialMatch>>> zipped = zip(subscriptionMatches);

        // transform to a list of [confirmed match, unconfirmed match] pairs of matches with same subscription
        List<Pair<PartialMatch, PartialMatch>> pairs = zipped.values().stream().flatMap(c -> c.stream()).collect(toList());
        Collections.shuffle(pairs, randomIn);
        iterator = pairs.iterator();
    }

    // zip partial matches in the map into pairs of [confirmed, not confirmed] matches
    private static Map<Long, List<Pair<PartialMatch, PartialMatch>>> zip(Map<Long, Map<Boolean, List<PartialMatch>>> map) {
        return map.entrySet().stream()
                .collect(toMap(
                        e -> e.getKey(),
                        e -> Streams.zip(
                                e.getValue().getOrDefault(true, Collections.emptyList()).stream(),
                                e.getValue().getOrDefault(false, Collections.emptyList()).stream(),
                                (v1, v2) -> Pair.create(v1, v2)
                        ).collect(toList())
                ));
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

        // pick the matches to change
        Pair<PartialMatch, PartialMatch> next = iterator.next();
        Match match1 = idMap.get(next.getFirst().groupId);
        Match match2 = idMap.get(next.getSecond().groupId);

        // swap their "confirmed" flag
        matches.add(match1);
        states.add(match2.confirmed);
        matches.add(match2);
        states.add(match1.confirmed);

        // also make sure any conflicting match is (flipped to) false
        if (match2.confirmed) {
            assignment.getConflictingMatchIds(match1.id)
                    .map(id -> idMap.get(id))
                    .filter(conflict -> conflict.confirmed)
                    .forEach(conflict -> {
                        matches.add(conflict);
                        states.add(false);
                    });
        }

        if (match1.confirmed) {
            assignment.getConflictingMatchIds(match2.id)
                    .map(id -> idMap.get(id))
                    .filter(conflict -> conflict.confirmed)
                    .forEach(conflict -> {
                        matches.add(conflict);
                        states.add(false);
                    });
        }

        return new MatchMove(matches, states);
    }

    /**
     * Custom toList collector that shuffles the list after creating it
     *
     * @param random the random numbers generator
     * @param <T> the type of the list
     * @return the shuffled list
     */
    private static <T> Collector<T, ?, List<T>> toShuffledList(Random random) {
        return Collector.of(
                ArrayList::new,
                List::add,
                (left, right) -> { left.addAll(right); return left; },
                list -> { Collections.shuffle(list, random); return (List<T>) list; }
        );
    }
}
