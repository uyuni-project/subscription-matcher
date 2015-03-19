package com.suse.matcher;

import com.suse.matcher.model.Subscription;
import com.suse.matcher.model.System;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;

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
        KieSession session = container.newKieSession("ksession-rules");
        FileLoader loader = new FileLoader();

        // insert facts from json files
        List<System> systems = loader.loadSystems();
        for (System system : systems) {
            session.insert(system);
        }

        for (Subscription subscription : loader.loadSubscriptions()) {
            session.insert(subscription);
        }

        // start forward-chaining inductions
        session.fireAllRules();

        // print results
        java.lang.System.out.println("**Forward-chaining** results:");
        for (System system : systems) {
            for (Subscription subscription : system.applicableSubscriptions) {
                java.lang.System.out.println(subscription.id + " can be used for " + system.id);
            }
        }

        java.lang.System.out.println("**Backward-chaining** results:");
        for (System system : systems) {
            QueryResults results = session.getQueryResults("hasApplicableSubscription", system);
            if (results.size() > 0) {
                java.lang.System.out.println(system.id + " has at least one applicable subscription");
            }
            else {
                java.lang.System.out.println(system.id + " does not have ANY applicable subscription");
            }
        }
    }
}
