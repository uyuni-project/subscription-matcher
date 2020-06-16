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

import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveIteratorFactory;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.Iterator;
import java.util.Random;

public class MatchSwapMoveIteratorFactory implements MoveIteratorFactory {

    /** {@inheritDoc} */
    @Override
    public long getSize(ScoreDirector director) {
        // we generate exactly one move per Match
        return getAssignment(director).getMatches().size();
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Move> createRandomMoveIterator(ScoreDirector director, Random random) {
        return new MatchSwapMoveIterator(getAssignment(director), random);
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Move> createOriginalMoveIterator(ScoreDirector director) {
        throw new UnsupportedOperationException("ORIGINAL selectionOrder is not supported.");
    }

    private Assignment getAssignment(ScoreDirector director) {
        return (Assignment)director.getWorkingSolution();
    }
}
