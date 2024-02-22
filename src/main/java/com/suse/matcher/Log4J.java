package com.suse.matcher;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.impl.Log4jContextFactory;
import org.apache.logging.log4j.spi.LoggerContextFactory;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * Facade on the log4j logging library.
 * Configures logging for the application.
 */
public class Log4J {

    private Log4J() {
        // Prevent instantiation
    }

    /**
     * Initialize the Log4j 2 configuration.
     *
     * @param level user chosen level
     * @param loggingDirectory directory for file logging
     *
     * @return the Log4j 2 {@link LoggerContext}
     */
    public static LoggerContext initialize(Optional<Level> level, Optional<String> loggingDirectory) {
        final ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

        builder.setStatusLevel(Level.INFO);
        builder.setConfigurationName("DefaultConfiguration");

        // Build  the root logger
        final RootLoggerComponentBuilder rootLogger = builder.newRootLogger(level.orElse(Level.INFO));

        // Build the default console appender
        final AppenderComponentBuilder consoleAppender = builder.newAppender("Stdout", "CONSOLE");

        consoleAppender.addAttribute("target", ConsoleAppender.Target.SYSTEM_ERR)
                       .add(builder.newLayout("PatternLayout")
                                   .addAttribute("pattern", "%-5p %c{1} - %m%n"));

        builder.add(consoleAppender);
        rootLogger.add(builder.newAppenderRef("Stdout"));

        // Build the rolling file appender if the path is available
        loggingDirectory.map(directory -> {
            AppenderComponentBuilder fileAppender = builder.newAppender("Rolling", "RollingFile");

            fileAppender.addAttribute("fileName", Paths.get(directory, "subscription-matcher.log").toString())
                        .addAttribute("filePattern", Paths.get(directory, "subscription-matcher.log.%i").toString())
                        .addAttribute("append", true)
                        .addComponent(builder.newComponent("DefaultRolloverStrategy")
                                             .addAttribute("max", 10))
                        .addComponent(builder.newComponent("SizeBasedTriggeringPolicy")
                                             .addAttribute("size", "20MB"))
                        .add(builder.newLayout("PatternLayout")
                                    .addAttribute("pattern", "%d %-5p %c{1} - %m%n"));

            return fileAppender;
        }).ifPresent(fileAppender -> {
            builder.add(fileAppender);
            rootLogger.add(builder.newAppenderRef("Rolling"));
        });

        builder.add(rootLogger);

        // Package specific loggers
        builder.add(builder.newLogger("com.suse.matcher", level.orElse(Level.DEBUG)));
        builder.add(builder.newLogger("org.drools", level.orElse(Level.WARN)));
        builder.add(builder.newLogger("org.optaplanner", level.orElse(Level.WARN)));
        builder.add(builder.newLogger("org.kie", level.orElse(Level.WARN)));
        // DefaultAgenda is VERY noisy, let's override the user settings
        builder.add(builder.newLogger("org.drools.core.common.DefaultAgenda", Level.WARN));

        Configuration configuration = builder.build();
        LoggerContext context = Configurator.initialize(configuration);

        // Update the configuration in the running contexts. There might be multiple contexts because slf4j binds
        // loggers by name, so the log4j compatibility layer cannot use the classloader to define the context
        LoggerContextFactory contextFactory = LogManager.getFactory();
        if (contextFactory instanceof Log4jContextFactory) {
            List<LoggerContext> contexts = ((Log4jContextFactory) contextFactory).getSelector().getLoggerContexts();
            contexts.stream()
                .distinct()
                .filter(ctx -> ctx != context)
                .forEach(ctx -> ctx.updateLoggers(configuration));
        }

        return context;
    }

}
