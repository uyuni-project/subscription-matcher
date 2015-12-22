package com.suse.matcher;

import com.suse.matcher.solver.Assignment;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

/**
 * Facade on the OptaPlanner solver.
 *
 * Fills a Solution object based on configuration specified in solverConfig.xml.
 */
public class OptaPlanner {

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
        solver.solve(unsolved);
        result = (Assignment) solver.getBestSolution();
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
