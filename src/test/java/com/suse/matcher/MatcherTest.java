package com.suse.matcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.suse.matcher.json.JsonInput;
import com.suse.matcher.json.JsonOutput;
import com.suse.matcher.solver.Assignment;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Tests {@link Matcher}.
 */
class MatcherTest {

    private static final Logger LOGGER = LogManager.getLogger(MatcherTest.class);

    // CSV files to be checked
    private static final List<String> CSV_FILES = List.of(
        "message_report.csv",
        "subscription_report.csv",
        "unmatched_product_report.csv"
    );

    // Utility to convert from/to JSON
    private static final JsonIO JSON_IO = new JsonIO();


    @BeforeAll
    static void initAll() {
        Log4J.initialize(Optional.of(Level.DEBUG), Optional.empty());
    }

    @BeforeEach
    void init() {
        Drools.resetIdMap();
    }

    /**
     * Tests against scenario data.
     */
    @DisplayName("Run test scenarios")
    @ParameterizedTest(name = "{1}")
    @MethodSource("listScenarios")
    void testScenario(int scenarioNumber, String description) {
        LOGGER.info("Executing {}", description);
        Matcher matcher = new Matcher(true);

        Assignment assignment = matcher.match(getJsonInput(scenarioNumber));
        JsonOutput actualOutput = FactConverter.convertToOutput(assignment);

        try {
            OutputWriter outputWriter = getOutputWriter(scenarioNumber);
            outputWriter.writeOutput(assignment, Optional.empty());
        }
        catch (IOException ex) {
            fail("Unable to write output csv");
        }

        // Check the JSON output produced by the process
        JsonOutput expectedOutput = getJsonOutput(scenarioNumber);
        assertJsonEquals(expectedOutput.getTimestamp(), actualOutput.getTimestamp());
        assertJsonEquals(expectedOutput.getMatches(), actualOutput.getMatches());
        assertJsonEquals(expectedOutput.getSubscriptionPolicies(), actualOutput.getSubscriptionPolicies());
        assertJsonEquals(expectedOutput.getMessages(), actualOutput.getMessages());
        assertJsonEquals(expectedOutput.getSubscriptions(), actualOutput.getSubscriptions());

        // Check all the CSV files
        CSV_FILES.forEach(csvFile -> {
            URL expectedCsvURL = MatcherTest.class.getResource(getResourcePath(scenarioNumber, csvFile));
            assertNotNull(expectedCsvURL, "Cannot load resource for csv " + csvFile);

            Path actualFile = getOutputPath(scenarioNumber).resolve(csvFile);
            assertTrue(Files.exists(actualFile), "Csv " + csvFile + " not generated");

            try (InputStream stream = expectedCsvURL.openStream()) {
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

                List<String> actualContent = Files.readAllLines(actualFile, StandardCharsets.UTF_8);
                List<String> expectedContent = streamReader.lines().collect(Collectors.toList());

                // Verify that the header of both files match
                assertEquals(expectedContent.get(0), actualContent.get(0), "Csv header " + csvFile + " do not match");

                // Verify the content is the same, apart from the order
                assertEquals(expectedContent.stream().skip(1).sorted().collect(Collectors.joining("\n")),
                    actualContent.stream().skip(1).sorted().collect(Collectors.joining("\n")),
                    "Csv content " + csvFile + "do not match");
            } catch(IOException ex) {
                fail("Unable to verify CSV files");
            }
        });
    }

    /**
     * Loads test data, instantiating multiple {@link MatcherTest} objects
     * with files loaded from resources/subscriptions* JSON files.
     *
     * @return a collection of parameters to the constructor of this class
     */
    static Stream<Arguments> listScenarios() {
        return Stream.iterate(1, MatcherTest::scenarioExists, i -> i + 1)
            .map(scenarioNumber -> Arguments.of(scenarioNumber, getScenarioTitle(scenarioNumber)));
    }

    /**
     * Checks if the given scenario exists
     *
     * @param scenarioNumber the scenario number
     * @return true if the scenario is available.
     */
    private static boolean scenarioExists(int scenarioNumber) {
        // Ensure all the required resources exists
        return Stream.concat(Stream.of("input.json", "output.json", "README.md"), CSV_FILES.stream())
            .map(file -> MatcherTest.class.getResource(getResourcePath(scenarioNumber, file)))
            .noneMatch(Objects::isNull);
    }

    /**
     * Returns the JSON input for the given scenario
     * @param scenarioNumber the scenario number
     * @return the provided JSON input for this scenario
     */
    private static JsonInput getJsonInput(int scenarioNumber) {
        return JSON_IO.loadInput(getContentAsString(scenarioNumber, "input.json"));
    }

    /**
     * Returns the JSON output for the given scenario
     * @param scenarioNumber the scenario number
     * @return the expected JSON output for this scenario
     */
    private static JsonOutput getJsonOutput(int scenarioNumber) {
        return JSON_IO.loadOutput(getContentAsString(scenarioNumber, "output.json"));
    }

    /**
     * Creates the output writer for the matching process result files
     * @param scenarioNumber the scenario number
     * @return the output writer
     */
    private static OutputWriter getOutputWriter(int scenarioNumber) {
        Path outputPath = getOutputPath(scenarioNumber);

        try {
            Files.createDirectories(outputPath);
        } catch(IOException ex) {
            throw new IllegalStateException("Unable to create directory structure: " + outputPath);
        }

        return new OutputWriter(Optional.of(outputPath.toString()), Optional.of(','));
    }

    /**
     * Retrieves the tille of the specified scenario
     * @param scenarioNumber the scenario number
     * @return the title as specified in rt
     */
    private static String getScenarioTitle(int scenarioNumber) {
        URL resource = MatcherTest.class.getResource(getResourcePath(scenarioNumber, "README.md"));
        if (resource == null) {
            throw new IllegalStateException("Unable to find README.md for scenario " + scenarioNumber);
        }

        try (InputStream stream = resource.openStream()) {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            return streamReader.readLine();
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to load README.md for scenario " + scenarioNumber);
        }
    }

    /**
     * Returns a string for a JSON scenario file
     *
     * @param scenarioNumber the i
     * @param fileName the filename
     * @return the content as string
     */
    private static String getContentAsString(int scenarioNumber, String fileName) {
        URL resource = MatcherTest.class.getResource(getResourcePath(scenarioNumber, fileName));
        if (resource == null) {
            throw new IllegalStateException("Unable to find resource " + fileName + " for scenario " + scenarioNumber);
        }

        try (InputStream is = resource.openStream()) {
            return new String(is.readAllBytes(), Charset.defaultCharset());
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to read file " + fileName + " for scenario " + scenarioNumber);
        }
    }

    /**
     * Get the full resource path for the given scenario number and file
     * @param scenarioNumber the scenario number
     * @param file the file to retrieve
     * @return the full resources path, as accessible from the {@link MatcherTest} class.
     */
    private static String getResourcePath(int scenarioNumber, String file) {
        return "scenarios/" + scenarioNumber + "/" + file;
    }


    /**
     * Retrieves the output path for a given scenario
     * @param scenarioNumber the scenario number
     * @return the output path
     */
    private static Path getOutputPath(int scenarioNumber) {
        return Paths.get("target", "output", "scenarios", String.valueOf(scenarioNumber));
    }

    /**
     * Asserts that the given object are converted to the same JSON
     * @param expected the expected object
     * @param actual the object to check
     */
    private static void assertJsonEquals(Object expected, Object actual) {
        assertEquals(JSON_IO.toJson(expected), JSON_IO.toJson(actual));
    }
}
