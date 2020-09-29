package com.suse.matcher;

import static java.util.stream.Collectors.toList;

import com.suse.matcher.facts.OneTwoPenalty;
import com.suse.matcher.facts.Penalty;
import com.suse.matcher.solver.Assignment;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.phase.PhaseConfig;
import org.optaplanner.core.config.solver.EnvironmentMode;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.impl.score.director.drools.DroolsScoreDirector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

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
    private Solver initSolver(boolean testing) {
        SolverFactory factory = SolverFactory.createFromXmlResource("com/suse/matcher/config/optaplanner/config.xml");

        SolverConfig config = factory.getSolverConfig();

        if (testing) {
            PhaseConfig localSearchConfig = config.getPhaseConfigList().get(1);
            localSearchConfig.getTerminationConfig().setUnimprovedStepCountLimit(12);
            config.setEnvironmentMode(EnvironmentMode.FULL_ASSERT);
        }

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
