package com.suse.matcher;

import com.suse.matcher.solver.Assignment;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Facade on the OptaPlanner solver.
 *
 * Fills a Solution object based on configuration specified in solverConfig.xml.
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
     */
    public OptaPlanner(Assignment unsolved) {
        // short circuit the planning in case there's nothing to optimize
        if (unsolved.getMatches().isEmpty()) {
            result = unsolved;
            return;
        }

        // init solver
        SolverFactory solverFactory = SolverFactory.createFromXmlResource("solverConfig.xml");
        Solver solver = solverFactory.buildSolver();

        // solve problem
        long start = System.currentTimeMillis();
        solver.solve(unsolved);
        logger.info("Optimization phase took {}ms", System.currentTimeMillis() - start);
        result = (Assignment) solver.getBestSolution();
        logger.info("{} matches confirmed", result.getMatches().stream().filter(m -> m.confirmed).count());
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
