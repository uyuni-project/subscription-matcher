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

package com.suse.matcher.solver;

import com.suse.matcher.facts.PotentialMatch;
import com.suse.matcher.util.CollectionUtils;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Generates {@link MatchMove}s.
 *
 * In particular, every {@link MatchMove} produced by this class will swap the confirmed flag
 * of two {@link Match}es that have same subscription and different "confirmed" flag.
 *
 * All "confirmed" flags of incompatible {@link Match}es are flipped to false.
 */
public class MatchSwapMoveIterator implements Iterator<MatchMove> {

    /** Solution instance. */
    private Assignment assignment;

    /** Iterator over all matches. */
    private final Iterator<Pair<PotentialMatch, PotentialMatch>> iterator;

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
                .collect(Collectors.toMap(
                        match -> match.id,
                        match -> match
                ));

        // subscription id -> confirmed/not confirmed -> shuffled list of matches
        Map<Long, Map<Boolean, List<PotentialMatch>>> subscriptionMatches = assignmentIn.getSortedPotentialMatchesCache()
                .stream()
                .collect(Collectors.groupingBy(
                    pm -> pm.subscriptionId,
                    TreeMap::new,
                    Collectors.groupingBy(
                        pm -> idMap.get(pm.groupId).confirmed,
                        TreeMap::new,
                        CollectionUtils.toShuffledList(randomIn)
                    )
                ));

        // Starting from the subscription id -> map of matches, build a shuffled list of pairs of matches containing the
        // subscription, such that a confirmed match is on the left and not confirmed match is on the right in the match
        iterator = subscriptionMatches.values()
                                      .stream()
                                      .map(map -> CollectionUtils.zip(map.get(true), map.get(false), Pair::of))
                                      .flatMap(Collection::stream)
                                      .collect(CollectionUtils.toShuffledList(randomIn))
                                      .iterator();
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
        Pair<PotentialMatch, PotentialMatch> next = iterator.next();
        Match match1 = idMap.get(next.getLeft().groupId);
        Match match2 = idMap.get(next.getRight().groupId);

        // swap their "confirmed" flag
        matches.add(match1);
        states.add(match2.confirmed);
        matches.add(match2);
        states.add(match1.confirmed);

        // also make sure any conflicting match is (flipped to) false
        if (BooleanUtils.isTrue(match2.confirmed)) {
            assignment.getConflictingMatchIds(match1.id)
                    .map(id -> idMap.get(id))
                    .filter(conflict -> conflict.confirmed)
                    .forEach(conflict -> {
                        matches.add(conflict);
                        states.add(false);
                    });
        }

        if (BooleanUtils.isTrue(match1.confirmed)) {
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

}
