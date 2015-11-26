package com.suse.matcher;

import static org.junit.Assert.assertEquals;

import com.suse.matcher.json.JsonMatch;
import com.suse.matcher.json.JsonOutput;
import com.suse.matcher.json.JsonSubscription;
import com.suse.matcher.json.JsonSystem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Tests {@link Matcher}.
 */
@RunWith(Parameterized.class)
public class MatcherTest {

    /** The matcher object under test. */
    private Matcher matcher;

    /** List of systems in the current test run. */
    private List<JsonSystem> systems;

    /** List of subscriptions in the current test run. */
    private List<JsonSubscription> subscriptions;

    /** List of pinned matches in the current test run. */
    private List<JsonMatch> pinnedMatches;

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
                    io.loadSystems(getReader(i, "systems.json")),
                    io.loadSubscriptions(getReader(i, "subscriptions.json")),
                    io.loadMatches(getReader(i, "pinned_matches.json")),
                    io.loadOutput(getReader(i, "output.json"))
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
     * Returns a reader for a JSON scenario file
     *
     * @param scenarioNumber the i
     * @param fileName the filename
     * @return the reader
     * @throws FileNotFoundException if the JSON file was not found
     */
    private static Reader getReader(int scenarioNumber, String fileName) throws FileNotFoundException {
        InputStream is = MatcherTest.class.getResourceAsStream("/scenario" + scenarioNumber + "/" + fileName);
        if (is == null) {
            throw new FileNotFoundException();
        }
        return new InputStreamReader(is);
    }

    /**
     * Instantiates a new Matcher test.
     *
     * @param systemsIn the systems
     * @param subscriptionsIn the subscriptions
     * @param pinnedMatchesIn the pinned matches
     * @param expectedOutputIn the expected output
     */
    public MatcherTest(List<JsonSystem> systemsIn, List<JsonSubscription> subscriptionsIn, List<JsonMatch> pinnedMatchesIn,
            JsonOutput expectedOutputIn) {
        matcher = new Matcher();
        systems = systemsIn;
        subscriptions = subscriptionsIn;
        pinnedMatches = pinnedMatchesIn;
        expectedOutput = expectedOutputIn;
    }

    /**
     * Tests against scenario data.
     * @throws Exception in case anything goes wrong
     */
    @Test
    public void test() throws Exception {
        Date timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse("2015-05-01T00:00:00.000+0200");
        JsonOutput actualOutput = FactConverter.convertToOutput(
                matcher.match(systems, subscriptions, pinnedMatches, timestamp));

        JsonIO io = new JsonIO();

        assertEquals("compliant systems", io.toJson(expectedOutput.compliantSystems), io.toJson(actualOutput.compliantSystems));
        assertEquals("partially compliant systems",
                io.toJson(expectedOutput.partiallyCompliantSystems),
                io.toJson(actualOutput.partiallyCompliantSystems)
        );
        assertEquals("non compliant systems", io.toJson(expectedOutput.nonCompliantSystems), io.toJson(actualOutput.nonCompliantSystems));
        assertEquals("remaining subscriptions", io.toJson(expectedOutput.remainingSubscriptions), io.toJson(actualOutput.remainingSubscriptions));
        assertEquals("errors", io.toJson(expectedOutput.errors), io.toJson(actualOutput.errors));
    }
}
