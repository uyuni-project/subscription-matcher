package com.suse.matcher;

import com.suse.matcher.model.InvalidPinnedMatch;
import com.suse.matcher.model.Match;
import com.suse.matcher.model.PinnedMatch;
import com.suse.matcher.model.Subscription;
import com.suse.matcher.model.System;

import org.kie.api.KieServices;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.ObjectFilter;
import org.kie.api.runtime.rule.Agenda;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Wraps the rule engine.
 */
public class Matcher {

    /** Filename for internal Drools audit log. */
    public static final String LOG_FILENAME = "drools";

    /** Rule group ordering. */
    private static final String[] RULE_GROUPS = {"InputAugmenting", "Matchability", "Matching", "OutputCollection"};

    /** Matching results. */
    private Collection<Match> matches = null;

    /** Invalid pin matches provided by the user. */
    private Collection<InvalidPinnedMatch> invalidPinMatches = null;

    /**
     * Tries to match systems to subscriptions.
     *
     * @param systems the systems
     * @param subscriptions the subscriptions
     * @param pinnedMatches the matches pinned by the user
     */
    @SuppressWarnings("unchecked")
    public void match(List<System> systems, List<Subscription> subscriptions, List<PinnedMatch> pinnedMatches) {
        // instantiate engine
        KieServices factory = KieServices.Factory.get();
        KieContainer container = factory.getKieClasspathContainer();
        KieSession session = container.newKieSession("ksession-rules");
        KieRuntimeLogger logger = factory.getLoggers().newFileLogger(session, LOG_FILENAME);

        // set rule ordering
        Agenda agenda = session.getAgenda();
        for (int i = RULE_GROUPS.length - 1; i >= 0; i--) {
            agenda.getAgendaGroup(RULE_GROUPS[i]).setFocus();
        }

        // set up logging
        session.addEventListener(new DebugAgendaEventListener());
        session.addEventListener(new DebugRuleRuntimeEventListener());

        // insert facts
        for (Subscription subscription : subscriptions) {
            /*
             * A rules engine do not like changing facts during execution. To be
             * on the save side, we do not insert subscriptions which are not
             * valid.
             */
            if (subscription.startsAt.before(new Date()) && subscription.expiresAt.after(new Date())) {
                session.insert(subscription);
            }
        }
        for (System system : systems) {
            session.insert(system);
        }
        for (PinnedMatch pinnedMatch : pinnedMatches) {
            session.insert(pinnedMatch);
        }

        // start engine
        session.fireAllRules();
        logger.close();

        // gather results
        matches = (Collection<Match>) session.getObjects(new ObjectFilter() {
            @Override
            public boolean accept(Object fact) {
                return fact.getClass().getSimpleName().equals("Match");
            }
        });

        invalidPinMatches = (Collection<InvalidPinnedMatch>) session.getObjects(new ObjectFilter() {
            @Override
            public boolean accept(Object fact) {
                return fact.getClass().getSimpleName().equals("InvalidPinnedMatch");
            }
        });
    }

    /**
     * Gets the matches.
     *
     * @return the matches
     */
    public Collection<Match> getMatches() {
        return matches;
    }

    /**
     * Gets the invalid pinned matches.
     *
     * @return the invalid pinned matches
     */
    public Collection<InvalidPinnedMatch> getInvalidPinnedMatches() {
        return invalidPinMatches;
    }
}
