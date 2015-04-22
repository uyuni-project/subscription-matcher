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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Tests {@link Matcher}.
 */
@SuppressWarnings("restriction") // we use our own JUnit distribution, don't care about Drools complaining
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
        JsonConverter loader = new JsonConverter();
        Collection<Object[]> result = new LinkedList<>();
        int i = 1;
        boolean moreFiles = true;
        while (moreFiles) {
            try {
                result.add(new Object[] {
                    loader.loadSystems(getReader(i, "systems.json")),
                    loader.loadSubscriptions(getReader(i, "subscriptions.json")),
                    loader.loadMatches(getReader(i, "pinned_matches.json")),
                    loader.loadOutput(getReader(i, "output.json"))
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
        JsonOutput actualOutput = matcher.match(systems, subscriptions, pinnedMatches);

        JsonConverter c = new JsonConverter();

        assertEquals("compliant systems", c.toJson(expectedOutput.compliantSystems), c.toJson(actualOutput.compliantSystems));
        assertEquals("partially compliant systems",
                c.toJson(expectedOutput.partiallyCompliantSystems),
                c.toJson(actualOutput.partiallyCompliantSystems)
        );
        assertEquals("non compliant systems", c.toJson(expectedOutput.nonCompliantSystems), c.toJson(actualOutput.nonCompliantSystems));
        assertEquals("remaining subscriptions", c.toJson(expectedOutput.remainingSubscriptions), c.toJson(actualOutput.remainingSubscriptions));
        assertEquals("errors", c.toJson(expectedOutput.errors), c.toJson(actualOutput.errors));
    }
}
