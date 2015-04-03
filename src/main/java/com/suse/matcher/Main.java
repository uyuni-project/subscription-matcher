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
        JsonConverter converter = new JsonConverter();
        List<JsonSystem> systems = converter.loadSystems(new FileReader(systemsPath));
        List<JsonSubscription> subscriptions = converter.loadSubscriptions(new FileReader(subscriptionsPath));
        List<JsonMatch> pinnedMatches = converter.loadMatches(new FileReader(pinnedMatchPath));

        // run the engine
        try(Matcher matcher = new Matcher()){
            matcher.addSystems(systems);
            matcher.addSubscriptions(subscriptions);
            matcher.addPinnedMatches(pinnedMatches);

            JsonOutput output = matcher.match();

            java.lang.System.out.println(converter.toJson(output));
        }
    }
}
