package com.suse.matcher;

import static java.util.stream.Collectors.toList;

import com.suse.matcher.facts.OneTwoPenalty;
import com.suse.matcher.facts.Penalty;
import com.suse.matcher.solver.Assignment;
import com.suse.matcher.solver.Match;
import com.suse.matcher.solver.MatchMoveIteratorFactory;
import com.suse.matcher.solver.MatchSwapMoveIteratorFactory;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicPhaseConfig;
import org.optaplanner.core.config.constructionheuristic.placer.QueuedEntityPlacerConfig;
import org.optaplanner.core.config.heuristic.selector.common.SelectionCacheType;
import org.optaplanner.core.config.heuristic.selector.common.SelectionOrder;
import org.optaplanner.core.config.heuristic.selector.entity.EntitySelectorConfig;
import org.optaplanner.core.config.heuristic.selector.move.MoveSelectorConfig;
import org.optaplanner.core.config.heuristic.selector.move.composite.UnionMoveSelectorConfig;
import org.optaplanner.core.config.heuristic.selector.move.factory.MoveIteratorFactoryConfig;
import org.optaplanner.core.config.heuristic.selector.move.generic.ChangeMoveSelectorConfig;
import org.optaplanner.core.config.heuristic.selector.value.ValueSelectorConfig;
import org.optaplanner.core.config.localsearch.LocalSearchPhaseConfig;
import org.optaplanner.core.config.localsearch.decider.acceptor.AcceptorConfig;
import org.optaplanner.core.config.localsearch.decider.forager.LocalSearchForagerConfig;
import org.optaplanner.core.config.score.definition.ScoreDefinitionType;
import org.optaplanner.core.config.score.director.ScoreDirectorFactoryConfig;
import org.optaplanner.core.config.solver.EnvironmentMode;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.random.RandomType;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.score.director.drools.DroolsScoreDirector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Facade on the OptaPlanner solver.
 *
 * Fills a Solution object.
 */
public class OptaPlanner {

    /** Logger instance. */
    private final Logger logger = LoggerFactory.getLogger(OptaPlanner.class);

    /** The result. */
    Assignment result;

    /**
     * Instantiates an OptaPlanner instance with the specified unsolved problem.
     *
     * @param unsolved the unsolved problem
     * @param testing true if running as a unit test, false otherwise
     */
    public OptaPlanner(Assignment unsolved, boolean testing) {
        // short circuit the planning in case there's nothing to optimize
        if (unsolved.getMatches().isEmpty()) {
            result = unsolved;
            return;
        }

        // init solver
        Solver solver = initSolver(testing);

        // solve problem
        long start = System.currentTimeMillis();
        solver.solve(unsolved);
        logger.info("Optimization phase took {}ms", System.currentTimeMillis() - start);
        result = (Assignment) solver.getBestSolution();
        logger.info("{} matches confirmed", result.getMatches().stream().filter(m -> m.confirmed).count());

        // show Penalty facts generated in Scores.drl using DroolsScoreDirector and re-calculating
        // the score of the best solution because facts generated dynamically are not available outside of this object
        if (logger.isDebugEnabled()) {
            DroolsScoreDirector scoreDirector = (DroolsScoreDirector) solver.getScoreDirectorFactory().buildScoreDirector();
            scoreDirector.setWorkingSolution(scoreDirector.cloneSolution(result));
            scoreDirector.calculateScore();
            Collection<Penalty> penalties = scoreDirector.getKieSession().getObjects()
                    .stream()
                    .filter(f -> f instanceof OneTwoPenalty)
                    .map(f -> (Penalty)f)
                    .collect(toList());
            logger.debug("The best solution has " + penalties.size() + " penalties for 1-2 subscriptions.");
            penalties.forEach(penalty -> logger.debug(penalty.toString()));
        }
    }

    /**
     * Configures and returns an OptaPlanner solver.
     *
     * This method replaces the XML configuration file cited in Optaplanner's documentation.
     *
     * @return the solver
     * @param testing true if running as a unit test, false otherwise
     */
    @SuppressWarnings("rawtypes")
    private Solver initSolver(boolean testing) {
        // init basic objects
        SolverFactory factory = SolverFactory.createEmpty();
        SolverConfig config = factory.getSolverConfig();
        config.setPhaseConfigList(new ArrayList<>());

        /*
         * Ensure results are reproducible across runs and JVMs
         */
        config.setEnvironmentMode(EnvironmentMode.REPRODUCIBLE);
        config.setRandomType(RandomType.MERSENNE_TWISTER);
        config.setRandomSeed(0L);

        /*
         * Declare solution and entity classes
         */
        config.setSolutionClass(Assignment.class);
        config.setEntityClassList(new ArrayList<Class<?>>(){{ add(Match.class); }});

        /*
         * Declare score type and file location
         */
        ScoreDirectorFactoryConfig score = new ScoreDirectorFactoryConfig();
        score.setScoreDefinitionType(ScoreDefinitionType.HARD_SOFT);
        score.setScoreDrlList(new ArrayList<String>() {{ add("com/suse/matcher/rules/optaplanner/Scores.drl"); }});
        config.setScoreDirectorFactoryConfig(score);

        /*
         * Construct an initial solution by visiting all possible Matches one by one, in order,
         * and changing that Match's confirmed property from the initial null value first to true and
         * then to false. Take whichever of the two has higher score and move on to the next Match
         * (jargon for this is "first fit").
         *
         * Because of how the score is calculated, moving a Match's confirmed property from null to either
         * true or false can only make hard score go down and/or the soft score go up (see Scores.drl).
         *
         * At the end of this process (called a Construction Heuristic or CH step) the hard score cannot
         * be negative and the soft score is typically positive (worst case is all Match.confirmed being set to
         * false, which yields 0/0).
         *
         * We use a custom Move Filter (ConflictMatchMoveFilter) to avoid moves that would result in conflicting
         * Matches to be confirmed.
         */
        ConstructionHeuristicPhaseConfig constructionHeuristic = new ConstructionHeuristicPhaseConfig();
        QueuedEntityPlacerConfig entityPlacer = new QueuedEntityPlacerConfig();
        EntitySelectorConfig placerEntitySelector = new EntitySelectorConfig();
        placerEntitySelector.setId("entitySelector");
        placerEntitySelector.setCacheType(SelectionCacheType.PHASE);
        placerEntitySelector.setSelectionOrder(SelectionOrder.ORIGINAL);
        entityPlacer.setEntitySelectorConfig(placerEntitySelector);

        ChangeMoveSelectorConfig changeMove = new ChangeMoveSelectorConfig();

        EntitySelectorConfig moveEntitySelector = new EntitySelectorConfig();
        moveEntitySelector.setMimicSelectorRef("entitySelector");
        changeMove.setEntitySelectorConfig(moveEntitySelector);

        ValueSelectorConfig valueSelector = new ValueSelectorConfig();
        valueSelector.setCacheType(SelectionCacheType.PHASE);
        valueSelector.setSelectionOrder(SelectionOrder.ORIGINAL);
        changeMove.setValueSelectorConfig(valueSelector);

        changeMove.setFilterClassList(new ArrayList<Class<? extends SelectionFilter>>() {{
            add(ConflictMatchMoveFilter.class);
        }});
        entityPlacer.setMoveSelectorConfigList(new ArrayList<MoveSelectorConfig>() {{ add(changeMove); }});
        constructionHeuristic.setEntityPlacerConfig(entityPlacer);
        config.getPhaseConfigList().add(constructionHeuristic);

        /*
         * Starting from the initial solution from the CH phase, explore other solutions by flipping some
         * Match.confirmed boolean values. A change from a certain solution to a new solution is called a move,
         * moves are repeated in iterations called steps.
         *
         * Sequences of steps will hopefully get to some new solutions that have a better score.
         *
         * For more information about how those moves are generated see the MatchMoveIteratorFactory class.
         */
        MoveIteratorFactoryConfig move = new MoveIteratorFactoryConfig();
        move.setCacheType(SelectionCacheType.JUST_IN_TIME);
        move.setSelectionOrder(SelectionOrder.RANDOM);
        move.setMoveIteratorFactoryClass(MatchMoveIteratorFactory.class);
        move.setSelectedCountLimit(10_000L);

        MoveIteratorFactoryConfig swapMove = new MoveIteratorFactoryConfig();
        swapMove.setCacheType(SelectionCacheType.JUST_IN_TIME);
        swapMove.setSelectionOrder(SelectionOrder.RANDOM);
        swapMove.setMoveIteratorFactoryClass(MatchSwapMoveIteratorFactory.class);
        swapMove.setSelectedCountLimit(10_000L);

        /*
         * Union move uses both of the above move implementations:
         * both moves alternate within the step (in random fashion).
         */
        UnionMoveSelectorConfig unionMoveConfig = new UnionMoveSelectorConfig();
        List<MoveSelectorConfig> selectors = new ArrayList<>();
        selectors.add(move);
        selectors.add(swapMove);
        unionMoveConfig.setMoveSelectorConfigList(selectors);

        LocalSearchPhaseConfig search = new LocalSearchPhaseConfig();
        search.setMoveSelectorConfig(unionMoveConfig);

        /*
         * Among generated moves, don't accept moves that were already attempted in the last
         * 50 steps (colloquially called "taboo").
         *
         * This prevents us to run in circles, repeating the same moves over and over (provided
         * the circle is shorter than 50 steps).
         */
        AcceptorConfig acceptor = new AcceptorConfig();
        acceptor.setMoveTabuSize(50);
        search.setAcceptorConfig(acceptor);

        /*
         * As accepted moves might still be a lot, don't evaluate more than 5_000 in any case.
         */
        LocalSearchForagerConfig forager = new LocalSearchForagerConfig();
        forager.setAcceptedCountLimit(5_000);
        search.setForagerConfig(forager);

        /*
         * Continue stepping and keep track of the overall best solution found so far.
         *
         * At some point we have to stop stepping, and we do so when:
         *   - we stepped 200 times with no score improvement (typically)
         *   - we stepped 15_000 times (when all else fails)
         *   - we spent 1 hour finding the solution
         */
        TerminationConfig termination = new TerminationConfig();
        termination.setUnimprovedStepCountLimit(200);
        termination.setStepCountLimit(15_000);
        termination.setHoursSpentLimit(1L);
        search.setTerminationConfig(termination);

        /*
         * Tweak parameters in unit tests, which deal with much less data and need
         * to run faster. These can only degrade results, so if a unit test passes
         * in test mode it will pass in production mode too.
         * Also activate OptaPlanner full assertions to catch more issues.
         */
        if (testing) {
            termination.setUnimprovedStepCountLimit(12);
            config.setEnvironmentMode(EnvironmentMode.FULL_ASSERT);
        }

        // return solver
        config.getPhaseConfigList().add(search);
        return factory.buildSolver();
    }

    /**
     * Gets the result.
     *
     * @return the result
     */
    public Assignment getResult() {
        return result;
    }
}
