/**
 * Copyright (c) 2020 SUSE LLC
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

package com.suse.matcher;

import com.suse.matcher.json.JsonInput;
import com.suse.matcher.solver.Assignment;

import org.apache.commons.io.FileUtils;
import org.optaplanner.benchmark.api.PlannerBenchmark;
import org.optaplanner.benchmark.api.PlannerBenchmarkFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Benchmark matcher.
 */
public class Benchmarker {

    /**
     * Entrypoint
     *
     * @param args the arguments
     */
    public static void main(String ... args) {
        if (args.length < 2) {
            System.err.println("ERROR: Invalid arguments");
            System.err.println("Specify path to benchmarker config XML followed by list of matcher input files, " +
                    "for example: config.xml input-user1.json input-user2.json");
            System.exit(1);
        }

        String configPath = args[0];
        Stream<String> inputPaths = Arrays.asList(args).stream().skip(1);

        PlannerBenchmarkFactory benchmarkFactory =
                PlannerBenchmarkFactory.createFromXmlFile(new File(configPath));

        // precompute the assignments using Drools
        List<Assignment> assignments = inputPaths
                .map(inputPath -> readJsonInput(new File(inputPath)))
                .map(input -> new Matcher(false).computeAssignment(input))
                .collect(Collectors.toList());

        PlannerBenchmark plannerBenchmark = benchmarkFactory.buildPlannerBenchmark(assignments);
        plannerBenchmark.benchmarkAndShowReportInBrowser();
    }

    private static JsonInput readJsonInput(File inputPath) {
        try {
            String inputStr = FileUtils.readFileToString(inputPath);
            return new JsonIO().loadInput(inputStr);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
