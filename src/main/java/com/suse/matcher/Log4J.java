package com.suse.matcher;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Facade on the log4j logging library.
 *
 * Configures logging for the application.
 */
public class Log4J {
    /** Log line layout. */
    private static final Layout LAYOUT = new PatternLayout("%-5p %c{1} - %m%n");

    /** Logger instance. */
    private final static Logger logger = LoggerFactory.getLogger(Log4J.class);

    /**
     * Inits the console logging.
     */
    public static void initConsoleLogging() {
        ConsoleAppender console = new ConsoleAppender(LAYOUT);
        console.setThreshold(Level.INFO);
        console.setTarget("System.err");
        console.activateOptions();

        org.apache.log4j.Logger.getRootLogger().addAppender(console);

        org.apache.log4j.Logger.getLogger("com.suse.matcher").setLevel(Level.DEBUG);
        org.apache.log4j.Logger.getLogger("org.drools").setLevel(Level.WARN);
        org.apache.log4j.Logger.getLogger("org.optaplanner").setLevel(Level.WARN);
    }

    /**
     * Inits the file logging.
     *
     * @param loggingDirectory the logging directory
     */
    public static void initFileLogging(Optional<String> loggingDirectory) {
        loggingDirectory.ifPresent(directory -> {
            try {
                File path = new File(directory, "subscription-matcher.log");
                RollingFileAppender file = new RollingFileAppender(LAYOUT, path.toString());
                file.setMaxBackupIndex(10);
                file.setMaxFileSize("20MB");
                file.setAppend(true);
                file.activateOptions();

                org.apache.log4j.Logger.getRootLogger().addAppender(file);
            }
            catch (IOException e) {
                logger.error("Could not set up file logging", e);
            }
        });
    }
}
