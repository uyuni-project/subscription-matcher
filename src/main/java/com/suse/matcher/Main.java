package com.suse.matcher;

import com.suse.matcher.model.Match;
import com.suse.matcher.model.Subscription;
import com.suse.matcher.model.System;

import java.io.FileReader;
import java.util.Collection;
import java.util.List;

/**
 * Entry point for the commandline version of this program.
 */
public class Main {

    /**
     * The main method.
     *
     * @param args commandline arguments
     * @throws Exception if anything unexpected happens
     */
    public static final void main(String[] args) throws Exception {
        // parse commandline
        if (args.length != 3) {
            java.lang.System.err.println("Usage: java -jar matcher.jar systems.json subscriptions.json pinned_matches.json");
            java.lang.System.exit(1);
        }
        String systemsPath = args[0];
        String subscriptionsPath = args[1];
        String pinnedMatchPath = args[2];

        // load files
        Loader loader = new Loader();
        List<System> systems = loader.loadSystems(new FileReader(systemsPath));
        List<Subscription> subscriptions = loader.loadSubscriptions(new FileReader(subscriptionsPath));
        List<Match> pinnedMatches = loader.loadMatches(new FileReader(pinnedMatchPath));

        // run the engine
        Matcher matcher = new Matcher();
        matcher.match(systems, subscriptions, pinnedMatches);
        Collection<Match> results = matcher.getMatches();
        Collection<Match> errors = matcher.getInvalidPinnedMatches();

        // print results
        for (Match error : errors) {
            java.lang.System.out.println("Pinned match of system " + error.systemId + " to subscription " + error.subscriptionId
                    + " is invalid and was ignored");
        }

        for (Match result : results) {
            java.lang.System.out.println(result.systemId + " can match " + result.subscriptionId);
        }

        java.lang.System.exit(0);
    }
}
