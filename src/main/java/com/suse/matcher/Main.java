package com.suse.matcher;

import com.suse.matcher.model.Subscription;
import com.suse.matcher.model.System;

import org.apache.commons.io.FilenameUtils;
import org.kie.api.KieServices;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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
        // load engine objects
        KieServices factory = KieServices.Factory.get();
        KieContainer container = factory.getKieClasspathContainer();
        List<String> scenarios = new LinkedList<String>();
        scenarios.add("./tests/scenario1/");
        scenarios.add("./tests/scenario2/");
        scenarios.add("./tests/scenario3/");
        scenarios.add("./tests/scenario4/");

        for (String scenario : scenarios) {
            KieSession session = container.newKieSession("ksession-rules");

            // The application can also setup listeners
            session.addEventListener( new DebugAgendaEventListener() );
            session.addEventListener( new DebugRuleRuntimeEventListener() );
            // To setup a file based audit logger, uncomment the next line
            String log = FilenameUtils.concat(scenario, "matcher");
            KieRuntimeLogger logger = factory.getLoggers().newFileLogger( session, log );

            // To setup a ThreadedFileLogger, so that the audit view reflects events whilst debugging,
            // uncomment the next line
            // KieRuntimeLogger logger = factory.getLoggers().newThreadedFileLogger( session, "./log/matcher", 1000 );

            FileLoader loader = new FileLoader(scenario);

            // insert facts from json files

            for (Subscription subscription : loader.loadSubscriptions()) {
            	/* A rules engine do not like changing facts during execution.
            	 * To be on the save side, we do not insert subscriptions
            	 * which are not valid.
            	 */
            	if (subscription.getStartsAt().before(new Date()) &&
            	    subscription.getExpiresAt().after(new Date())) {
                        session.insert(subscription);
                }
            }
            List<System> systems = loader.loadSystems();
            for (System system : systems) {
                session.insert(system);
            }

            // start forward-chaining inductions
            session.fireAllRules();
            logger.close();

            // print results
            java.lang.System.out.println("**Forward-chaining** results:");
            for (System system : systems) {
            	for (Subscription subscription : system.applicableSubscriptions) {
            		java.lang.System.out.println(subscription.id + " can be used for " + system.id);
            	}
            }
        }
    }
}
