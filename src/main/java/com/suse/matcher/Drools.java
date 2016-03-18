package com.suse.matcher;

import com.suse.matcher.facts.Message;

import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.Agenda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Facade on the Drools rule engine.
 *
 * Deduces facts based on some base facts and rules defined ksession-rules.xml.
 */
public class Drools {
    /** Rule groups corresponding to filenames and agenda group names. */
    private static final String[] RULE_GROUPS = {
        "PartNumbers",
        "InputValidation",
        "InputAugmenting",
        "Matchability",
    };

    /** Logger instance. */
    private final Logger logger = LoggerFactory.getLogger(Drools.class);

    /** Deduction resulting fact objects. */
    private Collection<? extends Object> result;

    /**
     * Instantiates a Drools instance with the specified base facts.
     * @param baseFacts fact objects
     */
    public Drools(Collection<Object> baseFacts) {
        // setup engine
        KieServices services = KieServices.Factory.get();
        KieModuleModel module = services.newKieModuleModel();

        // two facts are equal if equals() returns true (do not rely on ==)
        KieBaseModel base = module.newKieBaseModel("rules")
            .addPackage("com.suse.matcher.rules.drools")
            .setEqualsBehavior(EqualityBehaviorOption.EQUALITY);
        base.newKieSessionModel("session").setDefault(true);

        // add rule files to engine
        KieFileSystem kfs = services.newKieFileSystem();
        for (String ruleGroup : RULE_GROUPS) {
            kfs.write(services.getResources().newClassPathResource("com/suse/matcher/rules/drools/" + ruleGroup+ ".drl"));
        }
        kfs.writeKModuleXML(module.toXML());
        services.newKieBuilder(kfs).buildAll();

        // start a new session
        KieSession session = services.newKieContainer(services.getRepository().getDefaultReleaseId()).newKieSession();

        // set rule ordering
        Agenda agenda = session.getAgenda();
        for (int i = RULE_GROUPS.length - 1; i >= 0; i--) {
            agenda.getAgendaGroup(RULE_GROUPS[i]).setFocus();
        }

        // setup logging. This will not really log to the console but to slf4j which
        // in turn delegates to log4j, see log4j.xml for configuration
        KieRuntimeLogger kieLogger = services.getLoggers().newConsoleLogger(session);

        // insert base facts
        for (Object fact : baseFacts) {
            session.insert(fact);
        }

        // start deduction engine
        long start = System.currentTimeMillis();
        session.fireAllRules();
        logger.info("Deduction phase took {}ms", System.currentTimeMillis() - start);

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
