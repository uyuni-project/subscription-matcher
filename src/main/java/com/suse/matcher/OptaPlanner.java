package com.suse.matcher;

import com.suse.matcher.solver.Assignment;

import org.optaplanner.core.api.score.constraint.ConstraintMatch;
import org.optaplanner.core.api.score.constraint.ConstraintMatchTotal;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.impl.score.director.ScoreDirector;
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
        solver.solve(unsolved);
        result = (Assignment) solver.getBestSolution();

        if (logger.isDebugEnabled()) {
            ScoreDirector director = solver.getScoreDirectorFactory().buildScoreDirector();
            director.setWorkingSolution(result);
            for (ConstraintMatchTotal total : director.getConstraintMatchTotals()) {
                logger.debug("Constraint: {}, total score: {}", total.getConstraintName(), total.getWeightTotalAsNumber());
                for (ConstraintMatch match : total.getConstraintMatchSet()) {
                    logger.debug("  Match partial score: {}", match.getWeightAsNumber());
                    logger.debug("  Match justification list: {}", match.getJustificationList());
                }
            }
        }
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
