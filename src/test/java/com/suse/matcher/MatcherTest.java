package com.suse.matcher;

import static org.junit.Assert.assertEquals;

import com.suse.matcher.json.JsonInput;
import com.suse.matcher.json.JsonOutput;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

/**
 * Tests {@link Matcher}.
 */
@RunWith(Parameterized.class)
public class MatcherTest {

    /** The matcher object under test. */
    private Matcher matcher;

    /** List of systems in the current test run. */
    private JsonInput input;

    /** The expected output. */
    private JsonOutput expectedOutput;

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
                result.add(new Object[] {
                    io.loadInput(getString(i, "input.json")),
                    io.loadOutput(getString(i, "output.json"))
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
     * Instantiates a new Matcher test.
     *
     * @param inputIn a JSON input data blob
     * @param expectedOutputIn the expected output
     */
    public MatcherTest(JsonInput inputIn, JsonOutput expectedOutputIn) {
        matcher = new Matcher(true);
        input = inputIn;
        expectedOutput = expectedOutputIn;
    }

    /**
     * Tests against scenario data.
     * @throws Exception in case anything goes wrong
     */
    @Test
    public void test() throws Exception {
        Date timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse("2015-05-01T00:00:00.000+0200");
        JsonOutput actualOutput = FactConverter.convertToOutput(matcher.match(input, timestamp));

        JsonIO io = new JsonIO();

        assertEquals("timestamp",
                io.toJson(expectedOutput.getTimestamp()),
                io.toJson(actualOutput.getTimestamp()));
        assertEquals("matches",
                io.toJson(expectedOutput.getMatches()),
                io.toJson(actualOutput.getMatches()));
        assertEquals("subscriptionPolices",
                io.toJson(expectedOutput.getSubscriptionPolicies()),
                io.toJson(actualOutput.getSubscriptionPolicies()));
        assertEquals("messages",
                io.toJson(expectedOutput.getMessages()),
                io.toJson(actualOutput.getMessages()));
    }
}
