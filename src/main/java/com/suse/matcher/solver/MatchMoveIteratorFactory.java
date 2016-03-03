package com.suse.matcher.solver;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import com.suse.matcher.facts.IncompatibleGroups;

import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveIteratorFactory;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * A factory for {@link MatchMoveIterator}s.
 */
public class MatchMoveIteratorFactory implements MoveIteratorFactory {

    /** List of all matches. Cached as it does not change between steps. */
    private Optional<List<Match>> matchListCache = empty();

    /** List of all conflicts. Cached as it does not change between steps. */
    private Optional<Map<Match, List<Match>>> conflictMapCache = empty();

    /** {@inheritDoc} */
    @Override
    public long getSize(ScoreDirector director) {
        // we generate exactly one move per Match
        return getMatches(director).size();
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Move> createRandomMoveIterator(ScoreDirector director, Random random) {
        List<Match> matches = getMatches(director);

        // as this method is called once at the beginning of each step, take the chance
        // to ensure we try different moves in different steps if we are unable to try
        // them all (see setSelectedCountLimit in OptaPlanner)
        Collections.shuffle(matches, random);

        return new MatchMoveIterator(getConflictMap(director), matches.iterator());
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Move> createOriginalMoveIterator(ScoreDirector director) {
        throw new UnsupportedOperationException("ORIGINAL selectionOrder is not supported.");
    }

    /**
     * Gets a cached list of {@link Match}es.
     *
     * @param director the director
     * @return the conflict map
     */
    private List<Match> getMatches(ScoreDirector director) {
        if (!matchListCache.isPresent()) {
            Assignment solution = ((Assignment)director.getWorkingSolution());
            matchListCache = of(solution.getMatches()
                .stream()
                .sorted((a,b) -> a.id - b.id)
                .collect(Collectors.toList())
            );
        }
        return matchListCache.get();
    }

    /**
     * Gets a cached map from a {@link Match} to a list of other {@link Match}es that are incompatible with it.
     *
     * @param director the director
     * @return the conflict map
     */
    private Map<Match, List<Match>> getConflictMap(ScoreDirector director) {
        if (!conflictMapCache.isPresent()) {
            List<Match> matches = getMatches(director);

            Collection<IncompatibleGroups> incompatibleGroups =
                    ((Assignment) director.getWorkingSolution()).getProblemFacts(IncompatibleGroups.class);

            Map<Integer, Match> idMatchMap = matches.stream()
                    .collect(Collectors.toMap(m -> m.getId(), m -> m));

            conflictMapCache = of(matches.stream()
                .collect(Collectors.toMap(
                    match -> match,
                    match -> incompatibleGroups.stream()
                        .filter(pair -> match.getId() == pair.getGroupId1() || match.getId() == pair.getGroupId2())
                        .map(pair -> match.getId() == pair.getGroupId1() ? pair.getGroupId2() : pair.getGroupId1())
                        .distinct()
                        .sorted()
                        .map(id -> idMatchMap.get(id))
                        .collect(Collectors.toList())
                )
            ));
        }

        return conflictMapCache.get();
    }
}
