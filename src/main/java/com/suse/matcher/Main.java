package com.suse.matcher;

import com.suse.matcher.json.JsonMatch;
import com.suse.matcher.json.JsonOutput;
import com.suse.matcher.json.JsonSubscription;
import com.suse.matcher.json.JsonSystem;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
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
        CommandLine cmd = parseOptions(args);
        String systemsPath = cmd.getOptionValue('s');
        String subscriptionsPath = cmd.getOptionValue('u');
        String pinnedMatchPath = null;
        String outdir = null;
        if (cmd.hasOption('p')) {
            pinnedMatchPath = cmd.getOptionValue('p');
        }
        if (cmd.hasOption('o')) {
            outdir = cmd.getOptionValue('o');
        }

        // load files
        JsonIO io = new JsonIO();
        List<JsonSystem> systems = io.loadSystems(new FileReader(systemsPath));
        List<JsonSubscription> subscriptions = io.loadSubscriptions(new FileReader(subscriptionsPath));

        List<JsonMatch> pinnedMatches = new ArrayList<JsonMatch>();
        if (pinnedMatchPath != null) {
            pinnedMatches = io.loadMatches(new FileReader(pinnedMatchPath));
        }

        // do the matching
        Matcher m = new Matcher();
        JsonOutput result = m.match(systems, subscriptions, pinnedMatches, new Date());

        if (outdir != null) {
            ReportWriter rw = new ReportWriter(systems, subscriptions, m.getAssignment(), outdir);
            if (cmd.hasOption('d')) {
                rw.setDelimiter(cmd.getOptionValue('d').charAt(0));
            }
            rw.writeReports();
        }
        else {
            // print output
            System.out.println(io.toJson(result));
        }
    }

    private static CommandLine parseOptions(String[] args) {
        CommandLine cmd = null;
        Options opts = new Options();
        opts.addOption("h", "help", false, "show this help");
        opts.addOption("s", "systems", true, "Systems");
        opts.addOption("u", "subscriptions", true, "Subscriptions");
        opts.addOption("p", "pinned", true, "Pinned subscriptions to systems");
        opts.addOption("o", "directory", true, "Output directory");
        opts.addOption("d", "delimiter", true, "CSV Delimiter (Default: ,)");

        CommandLineParser parser = new BasicParser();
        try {
            cmd = parser.parse(opts, args);
            if (cmd.hasOption('h')) {
                help(opts);
                java.lang.System.exit(0);
            }
            if (!cmd.hasOption("s")) {
                throw new ParseException("Missing option 'systems'");
            }
            if (!cmd.hasOption("u")) {
                throw new ParseException("Missing option 'subscriptions'");
            }
            if (cmd.hasOption('o') && ! new File(cmd.getOptionValue('o')).isDirectory()) {
                throw new ParseException("Given output directory does not exist " +
                        "or is not a directory");
            }
        }
        catch (ParseException e) {
            java.lang.System.err.println("Failed to parse comand line properties:" + e);
            help(opts);
            java.lang.System.exit(1);
        }
        return cmd;
    }

    private static void help(Options opts) {
        HelpFormatter formater = new HelpFormatter();
        formater.printHelp("subscription-matcher OPTIONS", opts);
    }
}
