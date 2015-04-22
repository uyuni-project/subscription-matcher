package com.suse.matcher;

import com.suse.matcher.json.JsonMatch;
import com.suse.matcher.json.JsonOutput;
import com.suse.matcher.json.JsonSubscription;
import com.suse.matcher.json.JsonSystem;

import java.io.FileReader;
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
        JsonIO io = new JsonIO();
        List<JsonSystem> systems = io.loadSystems(new FileReader(systemsPath));
        List<JsonSubscription> subscriptions = io.loadSubscriptions(new FileReader(subscriptionsPath));
        List<JsonMatch> pinnedMatches = io.loadMatches(new FileReader(pinnedMatchPath));

        // do the matching
        JsonOutput result = new Matcher().match(systems, subscriptions, pinnedMatches);

        // print output
        System.out.println(io.toJson(result));
    }
}
