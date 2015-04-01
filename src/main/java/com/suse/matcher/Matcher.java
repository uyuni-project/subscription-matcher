package com.suse.matcher;

import static com.suse.matcher.facts.Match.Kind.CONFIRMED;
import static com.suse.matcher.facts.Match.Kind.INVALID;

import com.suse.matcher.facts.HostGuest;
import com.suse.matcher.facts.Match;
import com.suse.matcher.facts.Subscription;
import com.suse.matcher.facts.SystemProduct;
import com.suse.matcher.facts.Today;
import com.suse.matcher.facts.System;
import com.suse.matcher.json.JsonMatch;
import com.suse.matcher.json.JsonSubscription;
import com.suse.matcher.json.JsonSystem;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.kie.api.KieServices;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.ObjectFilter;
import org.kie.api.runtime.rule.Agenda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Wraps the rule engine.
 */
public class Matcher implements AutoCloseable {

    /** Filename for internal Drools audit log. */
    public static final String LOG_FILENAME = "drools";

    /** Rule group ordering. */
    private static final String[] RULE_GROUPS = {
        "InputValidation",
        "InputAugmenting",
        "PartNumbers",
        "ProductIds",
        "Matchability",
        "Matching",
        "OutputCollection"
    };

    /** The session. */
    private KieSession session;

    /** The logger. */
    private KieRuntimeLogger logger;

    /**
     * Instantiates a new matcher.
     */
    public Matcher() {
        KieServices factory = KieServices.Factory.get();
        KieContainer container = factory.getKieClasspathContainer();
        session = container.newKieSession("ksession-rules");
        logger = factory.getLoggers().newFileLogger(session, LOG_FILENAME);

        // set rule ordering
        Agenda agenda = session.getAgenda();
        for (int i = RULE_GROUPS.length - 1; i >= 0; i--) {
            agenda.getAgendaGroup(RULE_GROUPS[i]).setFocus();
        }

        // set up logging
        session.addEventListener(new DebugAgendaEventListener());
        session.addEventListener(new DebugRuleRuntimeEventListener());

        session.insert(new Today());
    }

    /**
     * Adds the systems.
     *
     * @param systems the systems
     */
    public void addSystems(List<JsonSystem> systems) {
        for (JsonSystem system : systems) {
            session.insert(new System(system.id, system.cpus));
            for (Long guestId : system.virtualSystemIds) {
                session.insert(new HostGuest(system.id, guestId));
            }
            for (Long productId : system.productIds) {
                session.insert(new SystemProduct(system.id, productId));
            }
        }
    }

    /**
     * Adds the subscriptions.
     *
     * @param subscriptions the subscriptions
     */
    public void addSubscriptions(List<JsonSubscription> subscriptions) {
        for (JsonSubscription subscription : subscriptions) {
            session.insert(new Subscription(subscription.id, subscription.partNumber, subscription.systemLimit, subscription.startsAt,
                    subscription.expiresAt, subscription.sccOrgId));
        }
    }

    /**
     * Adds the pinned matches.
     *
     * @param pinnedMatches the pinned matches
     */
    public void addPinnedMatches(List<JsonMatch> pinnedMatches) {
        for (JsonMatch match : pinnedMatches) {
            session.insert(new Match(match.systemId, match.subscriptionId, match.productId, match.quantity, Match.Kind.USER_PINNED));
        }
    }

    /**
     * Tries to match systems to subscriptions.
     */
    public void match() {
        // start engine
        session.fireAllRules();
        logger.close();
    }

    /**
     * Gets the resulting matches.
     *
     * @return the matches
     */
    public Collection<JsonMatch> getMatches() {
        @SuppressWarnings("unchecked")
        List<Match> matches = new ArrayList<Match>((Collection<Match>) session.getObjects(new ObjectFilter() {
            @Override
            public boolean accept(Object fact) {
                return fact instanceof Match && ((Match) fact).kind == CONFIRMED;
            }
        }));
        Collections.sort(matches);

        return transform(matches);
    }

    /**
     * Gets the resulting invalid pinned matches.
     *
     * @return the invalid pinned matches
     */
    public Collection<JsonMatch> getInvalidPinnedMatches() {
        @SuppressWarnings("unchecked")
        List<Match>invalidPinnedMatches = new ArrayList<Match>((Collection<Match>) session.getObjects(new ObjectFilter() {
            @Override
            public boolean accept(Object fact) {
                return fact instanceof Match && ((Match) fact).kind == INVALID;
            }
        }));
        Collections.sort(invalidPinnedMatches);

        return transform(invalidPinnedMatches);
    }

    /**
     * Transforms a collection of Match objects to JsonMatch objects.
     *
     * @param matches the matches
     * @return the json matches
     */
    private Collection<JsonMatch> transform(List<Match> matches) {
        return CollectionUtils.collect(matches, new Transformer<Match, JsonMatch>(){
            @Override
            public JsonMatch transform(Match match) {
                return new JsonMatch(match.systemId, match.subscriptionId, match.productId, match.quantity);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws Exception {
        if (session != null) {
            session.dispose();
        }
    }
}
