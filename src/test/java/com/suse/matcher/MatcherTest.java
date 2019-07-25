package com.suse.matcher;

import static org.junit.Assert.assertEquals;

import com.suse.matcher.json.JsonInput;
import com.suse.matcher.json.JsonOutput;
import com.suse.matcher.solver.Assignment;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

/**
 * Tests {@link Matcher}.
 */
@RunWith(Parameterized.class)
public class MatcherTest {

    /** The matcher object under test. */
    private Matcher matcher;

    /** Input. */
    private JsonInput input;

    /** Expected processed input after matching. */
    private final Optional<JsonInput> expectedProcessedInput;

    /** The expected output. */
    private JsonOutput expectedOutput;

    /** The scenario number. */
    private int scenarioNumber;

    /**
     * Loads test data, instantiating multiple {@link MatcherTest} objects
     * with files loaded from resources/subscriptions* JSON files.
     *
     * @return a collection of parameters to the constructor of this class
     * @throws Exception in case anything goes wrong
     */
    @Parameters
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
                    maybeGetString(i, "processed_input.json").map(input -> io.loadInput(input)),
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
        InputStream is = MatcherTest.class.getResourceAsStream("/scenario" + scenarioNumber
                + "/" + fileName);
        if (is == null) {
            throw new FileNotFoundException();
        }
        return IOUtils.toString(is);
    }

    /**
     * Returns a string for a JSON scenario file if the file exists
     *
     * @param scenarioNumber the i
     * @param fileName the filename
     * @return the string, or empty if the file doesn't exist
     * @throws IOException if an unexpected condition happens
     */
    private static Optional<String> maybeGetString(int scenarioNumber, String fileName) throws IOException {
        InputStream is = MatcherTest.class.getResourceAsStream("/scenario" + scenarioNumber
                + "/" + fileName);

        if (is == null) {
            return Optional.empty();
        }

        return Optional.of(IOUtils.toString(is));
    }

    /**
     * Instantiates a new Matcher test.
     *
     * @param inputIn a JSON input data blob
     * @param expectedProcessedInputIn the expected processed input
     * @param expectedOutputIn the expected output
     * @param scenarioNumberIn the scenario number
     */
    public MatcherTest(JsonInput inputIn, Optional<JsonInput> expectedProcessedInputIn, JsonOutput expectedOutputIn,
            int scenarioNumberIn) {
        matcher = new Matcher(true);
        input = inputIn;
        expectedOutput = expectedOutputIn;
        expectedProcessedInput = expectedProcessedInputIn;
        scenarioNumber = scenarioNumberIn;
        Drools.resetIdMap();
    }

    /**
     * Tests against scenario data.
     */
    @Test
    public void test() {
        Log4J.initConsoleLogging();

        Assignment assignment = matcher.match(input);
        JsonOutput actualOutput = FactConverter.convertToOutput(assignment);
        JsonInput processedInput = FactConverter.processInput(input, assignment);

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
        expectedProcessedInput.ifPresent(input ->
                assertEquals("scenario" + scenarioNumber + " processed input",
                        io.toJson(input),
                        io.toJson(processedInput)));
    }
}
