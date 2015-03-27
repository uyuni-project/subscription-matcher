package com.suse.matcher;

import com.suse.matcher.model.Match;
import com.suse.matcher.model.Subscription;
import com.suse.matcher.model.System;

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
    private List<System> systems;

    /** List of subscriptions in the current test run. */
    private List<Subscription> subscriptions;

    /** List of pinned matches in the current test run. */
    private List<Match> pinnedMatches;

    /**
     * Loads test data, instantiating multiple {@link MatcherTest} objects
     * with files loaded from resources/subscriptions* JSON files.
     *
     * @return a collection of parameters to the constructor of this class
     * @throws Exception in case anything goes wrong
     */
    @Parameters
    public static Collection<Object[]> loadTestData() throws Exception {
        Loader loader = new Loader();
        Collection<Object[]> result = new LinkedList<>();
        int i = 1;
        boolean moreFiles = true;
        while (moreFiles) {
            try {
                result.add(new Object[] {
                        loader.loadSystems(getReader(i, "systems.json")),
                        loader.loadSubscriptions(getReader(i, "subscriptions.json")),
                        loader.loadMatches(getReader(i, "pinned_matches.json"))
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
     */
    public MatcherTest(List<System> systemsIn, List<Subscription> subscriptionsIn, List<Match> pinnedMatchesIn) {
        matcher = new Matcher();
        systems = systemsIn;
        subscriptions = subscriptionsIn;
        pinnedMatches = pinnedMatchesIn;
    }

    /**
     * Test.
     */
    @Test
    public void test() {
        matcher.match(systems, subscriptions, pinnedMatches);
        // TODO: currently this simply tests no Exceptions are thrown, more significant checks should be added here
    }
}
