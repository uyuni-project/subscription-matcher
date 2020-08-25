package com.suse.matcher;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import com.suse.matcher.json.JsonInput;
import com.suse.matcher.solver.Assignment;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Optional;

/**
 * Entry point for the command line version of this program.
 */
public class Main {

    /** Logger instance. */
    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * The main method.
     *
     * @param args command line arguments
     * @throws Exception if anything unexpected happens
     */
    public static final void main(String[] args) throws Exception {
        try {
            long start = System.currentTimeMillis();
            CommandLine commandLine = parseCommandLine(args);

            Optional<Level> logLevel = commandLine.hasOption('v') ?
                    of(Level.toLevel(commandLine.getOptionValue('v'))) :
                    empty();
            Log4J.initConsoleLogging(logLevel);

            // create output writing objects
            Optional<Character> delimiter = commandLine.hasOption('d') ?
                    of(commandLine.getOptionValue('d').charAt(0)) :
                    empty();
            Optional<String> outdir = ofNullable(commandLine.getOptionValue('o'));
            OutputWriter writer = new OutputWriter(outdir, delimiter);
            Log4J.initFileLogging(ofNullable(commandLine.getOptionValue('l')));

            // load input data
            String inputString = commandLine.hasOption('i') ?
                    FileUtils.readFileToString(new File(commandLine.getOptionValue('i'))) :
                    IOUtils.toString(System.in);

            // save a copy of input data in the output directory
            writer.writeJsonInput(inputString);

            // do the matching
            JsonInput input = new JsonIO().loadInput(inputString);
            Assignment assignment = new Matcher(false).match(input);

            // write output data
            writer.writeOutput(assignment, logLevel);

            logger.info("Whole execution took {}ms", System.currentTimeMillis() - start);
        }
        catch (Throwable e) {
            logger.error("Unexpected exception: ", e);
        }
    }

    private static CommandLine parseCommandLine(String[] args) {
        CommandLine cmd = null;
        Options opts = new Options();
        opts.addOption("h", "help", false, "show this help");
        opts.addOption("i", "input", true, "input.json file (Default: standard input)");
        opts.addOption("o", "output-directory", true, "Output directory (Default: current directory)");
        opts.addOption("l", "log-directory", true, "Logging directory (Default: none, only log via STDERR)");
        opts.addOption("v", "log-level", true,
                "Log level (Default: INFO, Possible values: OFF, FATAL, ERROR, WARN, INFO, DEBUG, TRACE, ALL)");
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
            if (cmd.hasOption('l') && ! new File(cmd.getOptionValue('l')).isDirectory()) {
                throw new ParseException("Given logging directory does not exist " +
                        "or is not a directory");
            }
        }
        catch (ParseException e) {
            System.err.println("Failed to parse comand line properties:" + e);
            help(opts);
            System.exit(1);
        }
        return cmd;
    }

    private static void help(Options opts) {
        HelpFormatter formater = new HelpFormatter();
        formater.printHelp("subscription-matcher OPTIONS", opts);
    }
}
