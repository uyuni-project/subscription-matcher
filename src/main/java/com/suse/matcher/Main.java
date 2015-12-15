package com.suse.matcher;

import com.suse.matcher.json.JsonInput;
import com.suse.matcher.json.JsonOutput;
import com.suse.matcher.solver.Assignment;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;

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
        String outdir = null;
        if (cmd.hasOption('o')) {
            outdir = cmd.getOptionValue('o');
        }

        // load files
        JsonIO io = new JsonIO();
        Reader reader = null;
        if (cmd.hasOption('i')) {
            reader = new FileReader(cmd.getOptionValue('i'));
        }
        else{
            reader = new InputStreamReader(System.in);
        }
        JsonInput input = io.loadInput(reader);

        // do the matching
        Assignment assignment = new Matcher().match(input, new Date());
        JsonOutput result = FactConverter.convertToOutput(assignment);

        if (outdir != null) {
            OutputWriter rw = new OutputWriter(assignment, outdir);
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
        opts.addOption("i", "input", true, "input.json file (Default: standard input)");
        opts.addOption("o", "directory", true, "Output directory");
        opts.addOption("d", "delimiter", true, "CSV Delimiter (Default: ,)");

        CommandLineParser parser = new BasicParser();
        try {
            cmd = parser.parse(opts, args);
            if (cmd.hasOption('h')) {
                help(opts);
                java.lang.System.exit(0);
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
