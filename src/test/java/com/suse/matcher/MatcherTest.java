package com.suse.matcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.suse.matcher.json.JsonInput;
import com.suse.matcher.json.JsonOutput;
import com.suse.matcher.solver.Assignment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Tests {@link Matcher}.
 */
@RunWith(Parameterized.class)
public class MatcherTest {

    /** The matcher object under test. */
    private final Matcher matcher;

    //** Writer to create the output CSVs */
    private final OutputWriter outputWriter;

    /** Input. */
    private final JsonInput input;

    /** Expected output **/
    private final JsonOutput expectedOutput;

    /** The scenario number. */
    private final int scenarioNumber;

    /** Logger instance. */
    private final Logger logger = LogManager.getLogger(MatcherTest.class);

    /**
     * Loads test data, instantiating multiple {@link MatcherTest} objects
     * with files loaded from resources/subscriptions* JSON files.
     *
     * @return a collection of parameters to the constructor of this class
     * @throws Exception in case anything goes wrong
     */
    @Parameters(name = "Scenario #{2}") // name is the number of the scenario
    public static Collection<Object[]> loadTestData() throws Exception {
        JsonIO io = new JsonIO();
        Collection<Object[]> result = new LinkedList<>();
        int i = 1;
        boolean moreFiles = true;
        while (moreFiles) {
            try {
                String inputStr = getString(i, "input.json");
                result.add(new Object[] {
                    io.loadInput(inputStr),
                    io.loadOutput(getString(i, "output.json")),
                    i
                });
                i++;
            }
            catch (FileNotFoundException e) {
                moreFiles = false;
            }
        }
        return result;
    }

    /**
     * Returns a string for a JSON scenario file
     *
     * @param scenarioNumber the i
     * @param fileName the filename
     * @return the string
     * @throws IOException if an unexpected condition happens
     */
    private static String getString(int scenarioNumber, String fileName) throws IOException {
        try (InputStream is = MatcherTest.class.getResourceAsStream("/scenario" + scenarioNumber + "/" + fileName)) {
            if (is == null) {
                throw new FileNotFoundException();
            }

            return new String(is.readAllBytes(), Charset.defaultCharset());
        }
    }

    /**
     * Instantiates a new Matcher test.
     *
     * @param inputIn a JSON input data blob
     * @param expectedOutputIn the expected output
     * @param scenarioNumberIn the scenario number
     */
    public MatcherTest(JsonInput inputIn, JsonOutput expectedOutputIn, int scenarioNumberIn) {
        matcher = new Matcher(true);
        input = inputIn;
        expectedOutput = expectedOutputIn;
        scenarioNumber = scenarioNumberIn;
        Drools.resetIdMap();

        Path outputPath = Paths.get("target/output/scenario" + scenarioNumberIn);
        try {
            Files.createDirectories(outputPath);
        } catch(IOException ex) {
            logger.error("Unable to create the required directory structure: {}", outputPath, ex);
            throw new IllegalStateException("Unable to create directory structure: " + outputPath);
        }

        outputWriter = new OutputWriter(Optional.of(outputPath.toString()), Optional.of(','));
    }

    @BeforeClass
    public static void setUp() {
        Log4J.initialize(Optional.empty(), Optional.empty());
    }

    /**
     * Tests against scenario data.
     */
    @Test
    public void test() {
        logger.info("TESTING SCENARIO {}", scenarioNumber);

        Assignment assignment = matcher.match(input);
        JsonOutput actualOutput = FactConverter.convertToOutput(assignment);

        try {
            outputWriter.writeOutput(assignment, Optional.empty());
        }
        catch (IOException ex) {
            logger.error("Unable to write output csv", ex);
            fail("Unable to write output csv");
        }

        JsonIO io = new JsonIO();

        assertEquals("scenario" + scenarioNumber + " timestamp",
                io.toJson(expectedOutput.getTimestamp()),
                io.toJson(actualOutput.getTimestamp()));
        assertEquals("scenario" + scenarioNumber + " matches",
                io.toJson(expectedOutput.getMatches()),
                io.toJson(actualOutput.getMatches()));
        assertEquals("scenario" + scenarioNumber + " subscriptionPolices",
                io.toJson(expectedOutput.getSubscriptionPolicies()),
                io.toJson(actualOutput.getSubscriptionPolicies()));
        assertEquals("scenario" + scenarioNumber + " messages",
                io.toJson(expectedOutput.getMessages()),
                io.toJson(actualOutput.getMessages()));
        assertEquals("scenario" + scenarioNumber + " subscriptions",
                io.toJson(expectedOutput.getSubscriptions()),
                io.toJson(actualOutput.getSubscriptions()));

        // Checking CSV
        Stream.of("message_report.csv", "subscription_report.csv", "unmatched_product_report.csv").forEach(csvFile -> {
            URL expectedCsvURL = MatcherTest.class.getResource("/scenario" + scenarioNumber + "/" + csvFile);
            assertNotNull("scenario" + scenarioNumber + " cannot load resource for csv " + csvFile, expectedCsvURL);

            Path actualFile = Paths.get("target/output/scenario" + scenarioNumber).resolve(csvFile);
            assertTrue("scenario" + scenarioNumber + " csv " + csvFile + " not generated", Files.exists(actualFile));

            try (InputStream stream = expectedCsvURL.openStream()) {
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

                List<String> actualContent = Files.readAllLines(actualFile, StandardCharsets.UTF_8);
                List<String> expectedContent = streamReader.lines().collect(Collectors.toList());

                // Verify that the header of both files match
                assertEquals("scenario" + scenarioNumber + " csv header " + csvFile,
                    expectedContent.get(0), actualContent.get(0));

                // Verify the content is the same, apart from the order
                assertEquals("scenario" + scenarioNumber + " csv content " + csvFile,
                    expectedContent.stream().skip(1).sorted().collect(Collectors.joining("\n")),
                    actualContent.stream().skip(1).sorted().collect(Collectors.joining("\n")));
            } catch(IOException ex) {
                logger.error("Unable to verify CSV files", ex);
                fail("Unable to verify CSV files");
            }
        });
    }
}
