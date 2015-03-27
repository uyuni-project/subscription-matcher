package com.suse.matcher;

import static com.suse.matcher.model.Match.Kind.CONFIRMED;
import static com.suse.matcher.model.Match.Kind.INVALID;
import static com.suse.matcher.model.Match.Kind.USER_PINNED;

import com.suse.matcher.model.Match;
import com.suse.matcher.model.Subscription;
import com.suse.matcher.model.System;
import com.suse.matcher.model.Today;

import org.kie.api.KieServices;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.ObjectFilter;
import org.kie.api.runtime.rule.Agenda;

import java.util.Collection;
import java.util.List;


/**
 * Wraps the rule engine.
 */
public class Matcher {

    /** Filename for internal Drools audit log. */
    public static final String LOG_FILENAME = "drools";

    /** Rule group ordering. */
    private static final String[] RULE_GROUPS = {
        "InputValidation",
        "InputAugmenting",
        "PartNumbers",
        "ProductsInSubscriptions",
        "Matchability",
        "Matching",
        "OutputCollection"
    };

    /** Matching results. */
    private Collection<Match> matches = null;

    /** Invalid pin matches provided by the user. */
    private Collection<Match> invalidPinMatches = null;

    /**
     * Tries to match systems to subscriptions.
     *
     * @param systems the systems
     * @param subscriptions the subscriptions
     * @param pinnedMatches the matches pinned by the user
     */
    @SuppressWarnings("unchecked")
    public void match(List<System> systems, List<Subscription> subscriptions, List<Match> pinnedMatches) {
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
        session.insert(new Today());
        for (Subscription subscription : subscriptions) {
            session.insert(subscription);
        }
        for (System system : systems) {
            session.insert(system);
        }
        for (Match pinnedMatch : pinnedMatches) {
            pinnedMatch.kind = USER_PINNED;
            session.insert(pinnedMatch);
        }

        // start engine
        session.fireAllRules();
        logger.close();

        // gather results
        matches = (Collection<Match>) session.getObjects(new ObjectFilter() {
            @Override
            public boolean accept(Object fact) {
                return fact instanceof Match && ((Match) fact).kind == CONFIRMED;
            }
        });

        invalidPinMatches = (Collection<Match>) session.getObjects(new ObjectFilter() {
            @Override
            public boolean accept(Object fact) {
                return fact instanceof Match && ((Match) fact).kind == INVALID;
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
    public Collection<Match> getInvalidPinnedMatches() {
        return invalidPinMatches;
    }
}
