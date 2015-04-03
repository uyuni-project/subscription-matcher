package com.suse.matcher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.suse.matcher.json.JsonMatch;
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

    /** List of expected matches in the current test run. */
    private Collection<JsonMatch> expectedMatches;

    /** List of expected invalid pinned matches in the current test run. */
    private Collection<JsonMatch> expectedInvalidPinnedMatches;

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
                    loader.loadMatches(getReader(i, "expected_matches.json")),
                    loader.loadMatches(getReader(i, "expected_invalid_pinned_matches.json"))
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
     * @param expectedMatchesIn the expected output matches
     * @param expectedInvalidPinnedMatchesIn the expected invalid pinned matches
     */
    public MatcherTest(List<JsonSystem> systemsIn, List<JsonSubscription> subscriptionsIn, List<JsonMatch> pinnedMatchesIn,
            List<JsonMatch> expectedMatchesIn, List<JsonMatch> expectedInvalidPinnedMatchesIn) {
        matcher = new Matcher();
        systems = systemsIn;
        subscriptions = subscriptionsIn;
        pinnedMatches = pinnedMatchesIn;
        expectedMatches = expectedMatchesIn;
        expectedInvalidPinnedMatches = expectedInvalidPinnedMatchesIn;
    }

    /**
     * Test.
     */
    @Test
    public void test() {
        matcher.addSystems(systems);
        matcher.addSubscriptions(subscriptions);
        matcher.addPinnedMatches(pinnedMatches);

        matcher.match();

        Collection<JsonMatch> actualMatches = matcher.getMatches();
        Collection<JsonMatch> actualInvalidPinnedMatches = matcher.getInvalidPinnedMatches();

        assertThat(actualMatches, equalTo(expectedMatches));
        assertThat(actualInvalidPinnedMatches, equalTo(expectedInvalidPinnedMatches));
    }
}
