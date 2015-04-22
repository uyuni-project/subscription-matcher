package com.suse.matcher;

import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

/**
 * Facade on the OptaPlanner solver.
 *
 * Fills a Solution object based on configuration specified in solverConfig.xml.
 *
 * @param <T> type of the Solution object
 */
public class OptaPlanner<T extends Solution<?>> {

    /** The result. */
    T result;

    /**
     * Instantiates an OptaPlanner instance with the specified unsolved problem.
     *
     * @param unsolved the unsolved problem
     */
    @SuppressWarnings("unchecked")
    public OptaPlanner(T unsolved) {
        // init solver
        SolverFactory solverFactory = SolverFactory.createFromXmlResource("solverConfig.xml");
        Solver solver = solverFactory.buildSolver();

        // solve problem
        solver.solve(unsolved);
        result = (T) solver.getBestSolution();
    }

    /**
     * Gets the result.
     *
     * @return the result
     */
    public T getResult() {
        return result;
    }
}
