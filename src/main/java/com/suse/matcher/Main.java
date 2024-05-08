package com.suse.matcher;

import com.suse.matcher.json.JsonInput;
import com.suse.matcher.solver.Assignment;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Entry point for the command line version of this program.
 */
public class Main {

    /**
     * The main method.
     *
     * @param args command line arguments
     * @throws Exception if anything unexpected happens
     */
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        CommandLine commandLine = parseCommandLine(args);

        // First initialize the logging system
        Optional<Level> logLevel = commandLine.hasOption('v') ?
            Optional.of(Level.toLevel(commandLine.getOptionValue('v'))) :
            Optional.empty();

        try (LoggerContext context = Log4J.initialize(logLevel, Optional.ofNullable(commandLine.getOptionValue('l')))) {
            Logger logger = context.getLogger(Main.class);
            logger.info("Starting subscription-matcher process");

            try {
                // create output writing objects
                Optional<Character> delimiter = commandLine.hasOption('d') ?
                    Optional.of(commandLine.getOptionValue('d').charAt(0)) :
                    Optional.empty();
                Optional<String> outdir = Optional.ofNullable(commandLine.getOptionValue('o'));
                OutputWriter writer = new OutputWriter(outdir, delimiter);

                // load input data
                String inputString = commandLine.hasOption('i') ?
                    Files.readString(Path.of(commandLine.getOptionValue('i'))) :
                    new String(System.in.readAllBytes(), Charset.defaultCharset());

                // save a copy of input data in the output directory
                writer.writeJsonInput(inputString);

                // do the matching
                JsonInput input = new JsonIO().loadInput(inputString);
                Assignment assignment = new Matcher(false).match(input);

                // write output data
                writer.writeOutput(assignment, logLevel);

                logger.info("Whole execution took {}ms", System.currentTimeMillis() - start);
            }
            catch (Exception ex) {
                logger.error("Unexpected exception: ", ex);
            }
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
