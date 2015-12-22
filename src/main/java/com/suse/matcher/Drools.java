package com.suse.matcher;

import com.suse.matcher.facts.Message;

import org.kie.api.KieServices;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Facade on the Drools rule engine.
 *
 * Deduces facts based on some base facts and rules defined ksession-rules.xml.
 */
public class Drools {

    /** Logger instance. */
    private final Logger logger = LoggerFactory.getLogger(Drools.class);

    /** Deduction resulting fact objects. */
    private Collection<? extends Object> result;

    /**
     * Instantiates a Drools instance with the specified base facts.
     * @param baseFacts fact objects
     */
    public Drools(Collection<Object> baseFacts) {
        // read configuration from kmodule.xml and instantiate the engine
        KieServices factory = KieServices.Factory.get();
        KieContainer container = factory.getKieClasspathContainer();
        KieSession session = container.newKieSession("ksession-rules");

        // setup logging. This will not really log to the console but to slf4j which
        // in turn delegates to log4j, see log4j.xml for configuration
        KieRuntimeLogger kieLogger = factory.getLoggers().newConsoleLogger(session);

        // insert base facts
        for (Object fact : baseFacts) {
            session.insert(fact);
        }

        // start deduction engine
        session.fireAllRules();

        // collect results
        result = session.getObjects();

        // log deducted messages
        result.stream()
            .filter(o -> o instanceof Message)
            .map(m -> (Message) m)
            .filter(m -> m.severity.equals(Message.Level.DEBUG))
            .sorted()
            .forEach(m -> logger.debug("{}: {}", m.type, m.data.toString()));
        ;

        // cleanup
        kieLogger.close();
        session.dispose();
    }

    /**
     * Returns all facts deduced by Drools.
     * @return the deduced facts
     */
    public Collection<? extends Object> getResult() {
        return result;
    }
}
