package com.suse.matcher;

import com.suse.matcher.solver.Assignment;
import com.suse.matcher.solver.Match;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicPhaseConfig;
import org.optaplanner.core.config.constructionheuristic.placer.QueuedEntityPlacerConfig;
import org.optaplanner.core.config.heuristic.selector.common.SelectionCacheType;
import org.optaplanner.core.config.heuristic.selector.common.SelectionOrder;
import org.optaplanner.core.config.heuristic.selector.entity.EntitySelectorConfig;
import org.optaplanner.core.config.heuristic.selector.move.MoveSelectorConfig;
import org.optaplanner.core.config.heuristic.selector.move.composite.CartesianProductMoveSelectorConfig;
import org.optaplanner.core.config.heuristic.selector.move.composite.UnionMoveSelectorConfig;
import org.optaplanner.core.config.heuristic.selector.move.generic.ChangeMoveSelectorConfig;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

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
         * Construct an initial solution by setting all possible Matches' confirmed property to false.
         *
         * At the end of this process (called a Construction Heuristic or CH step) the score will be
         * exactly 0/0.
         */
        ConstructionHeuristicPhaseConfig constructionHeuristic = new ConstructionHeuristicPhaseConfig();
        QueuedEntityPlacerConfig entityPlacer = new QueuedEntityPlacerConfig();
        EntitySelectorConfig entitySelector = new EntitySelectorConfig();
        entitySelector.setCacheType(SelectionCacheType.PHASE);
        entitySelector.setSelectionOrder(SelectionOrder.ORIGINAL);
        entityPlacer.setEntitySelectorConfig(entitySelector);
        ChangeMoveSelectorConfig constructionHeuristicMove = new ChangeMoveSelectorConfig();
        constructionHeuristicMove.setFilterClassList(new ArrayList<Class<? extends SelectionFilter>>() {{
            add(FalseFilter.class);
        }});
        entityPlacer.setMoveSelectorConfigList(new ArrayList<MoveSelectorConfig>() {{ add(constructionHeuristicMove); }});
        constructionHeuristic.setEntityPlacerConfig(entityPlacer);
        config.getPhaseConfigList().add(constructionHeuristic);

        /*
         * Starting from the initial solution from the CH phase, explore other solutions by flipping some
         * Match.confirmed boolean values. A change from a certain solution to a new solution is called a move,
         * moves are repeated in iterations called steps.
         *
         * Sequences of steps will hopefully get to some new solutions that have a better score.
         *
         * We allow moves to change from 1 to 4 Match.confirmed values per step.
         */
        LocalSearchPhaseConfig search = new LocalSearchPhaseConfig();
        UnionMoveSelectorConfig move = new UnionMoveSelectorConfig(new ArrayList<>());
        move.getMoveSelectorConfigList().add(new ChangeMoveSelectorConfig());
        move.getMoveSelectorConfigList().add(new CartesianProductMoveSelectorConfig(new ArrayList<MoveSelectorConfig>() {{
            add(new ChangeMoveSelectorConfig());
            add(new ChangeMoveSelectorConfig());
        }}));
        move.getMoveSelectorConfigList().add(new CartesianProductMoveSelectorConfig(new ArrayList<MoveSelectorConfig>() {{
            add(new ChangeMoveSelectorConfig());
            add(new ChangeMoveSelectorConfig());
            add(new ChangeMoveSelectorConfig());
        }}));
        move.getMoveSelectorConfigList().add(new CartesianProductMoveSelectorConfig(new ArrayList<MoveSelectorConfig>() {{
            add(new ChangeMoveSelectorConfig());
            add(new ChangeMoveSelectorConfig());
            add(new ChangeMoveSelectorConfig());
            add(new ChangeMoveSelectorConfig());
        }}));

        /*
         * Every step, generate several moves and pick the best scoring one as the next step.
         *
         * As possible moves might be a lot, don't generate more than 20_000 in any case.
         */
        move.setSelectedCountLimit(20_000L);
        search.setMoveSelectorConfig(move);

        /*
         * Among generated moves, don't accept moves:
         *   - that do not actually change the solution (eg. true -> true)
         *   - that make the hard score negative
         *   - that were already attempted in the last 50 steps (colloquially called "taboo").
         *
         * This prevents us to run in circles, repeating the same moves over and over (provided
         * the circle is shorter than 50 steps).
         */
        AcceptorConfig acceptor = new AcceptorConfig();
        acceptor.setMoveTabuSize(50);
        search.setAcceptorConfig(acceptor);

        /*
         * As accepted moves might still be a lot, don't evaluate more than 10_000 in any case.
         */
        LocalSearchForagerConfig forager = new LocalSearchForagerConfig();
        forager.setAcceptedCountLimit(10_000);
        search.setForagerConfig(forager);

        /*
         * Continue stepping and keep track of the overall best solution found so far.
         *
         * At some point we have to stop stepping, and we do so when:
         *   - we stepped 500 times with no score improvement (typically)
         *   - we stepped 10_000 times (when all else fails)
         */
        TerminationConfig termination = new TerminationConfig();
        termination.setUnimprovedStepCountLimit(500);
        termination.setStepCountLimit(10_000);
        search.setTerminationConfig(termination);

        /*
         * Tweak parameters in unit tests, which deal with much less data and need
         * to run faster. These can only degrade results, so if a unit test passes
         * in test mode it will pass in production mode too.
         * Also activate OptaPlanner full assertions to catch more issues.
         */
        if (testing) {
            termination.setUnimprovedStepCountLimit(5);
            move.setSelectedCountLimit(100L);
            forager.setAcceptedCountLimit(100);
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
