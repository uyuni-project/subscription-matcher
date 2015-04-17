package com.suse.matcher;

import static com.suse.matcher.facts.Match.Kind.CONFIRMED;
import static com.suse.matcher.facts.Match.Kind.INVALID;

import com.google.gson.reflect.TypeToken;
import com.suse.matcher.facts.HostGuest;
import com.suse.matcher.facts.Match;
import com.suse.matcher.facts.Subscription;
import com.suse.matcher.facts.SubscriptionProduct;
import com.suse.matcher.facts.System;
import com.suse.matcher.facts.SystemProduct;
import com.suse.matcher.facts.Today;
import com.suse.matcher.json.JsonMatch;
import com.suse.matcher.json.JsonOutput;
import com.suse.matcher.json.JsonOutputError;
import com.suse.matcher.json.JsonOutputProduct;
import com.suse.matcher.json.JsonOutputSubscription;
import com.suse.matcher.json.JsonOutputSystem;
import com.suse.matcher.json.JsonSubscription;
import com.suse.matcher.json.JsonSystem;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;


/**
 * Wraps the rule engine.
 */
public class Matcher implements AutoCloseable {

    /** Filename for internal Drools audit log. */
    public static final String LOG_FILENAME = "drools";

    /** Rule group ordering. */
    private static final String[] RULE_GROUPS = {
        "FirstGroup",
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
            session.insert(new Subscription(subscription.id, subscription.partNumber, subscription.systemLimit.doubleValue(),
                    subscription.startsAt, subscription.expiresAt, subscription.sccOrgId));
            for (Long productId : subscription.productIds) {
                session.insert(new SubscriptionProduct(subscription.id, productId));
            }
        }
    }

    /**
     * Adds the pinned matches.
     *
     * @param pinnedMatches the pinned matches
     */
    public void addPinnedMatches(List<JsonMatch> pinnedMatches) {
        for (JsonMatch match : pinnedMatches) {
            session.insert(new Match(match.systemId, match.productId, match.subscriptionId, null, Match.Kind.USER_PINNED));
        }
    }

    /**
     * Tries to match systems to subscriptions.
     *
     * @return the output
     */
    @SuppressWarnings("unchecked")
    public JsonOutput match() {
        // start engine
        session.fireAllRules();

        // collect facts
        List<System> systems = getFacts(new TypeToken<System>(){});
        List<Long> systemIds = new LinkedList<>();
        for (System system : systems) {
            systemIds.add(system.id);
        }

        List<Match> confirmedMatches = getFacts(new TypeToken<Match>(){}, new Predicate<Match>() {
            @Override
            public boolean evaluate(Match match) {
                return match.kind == CONFIRMED;
            }
        });

        // format result in json format
        JsonOutput output = new JsonOutput();

        Map<Pair<Long, Long>, Match> matchMap = new TreeMap<>();
        for (Match match : confirmedMatches) {
            matchMap.put(new ImmutablePair<>(match.systemId, match.productId), match);
        }

        List<SystemProduct> installations = getFacts(new TypeToken<SystemProduct>(){});

        @SuppressWarnings("rawtypes")
        MultiMap<Long, Long> installationMap = MultiValueMap.multiValueMap(new TreeMap<Long, Collection>(), TreeSet.class);
        for (SystemProduct installation : installations) {
            installationMap.put(installation.systemId, installation.productId);
        }
        for (Match match : confirmedMatches) {
            installationMap.put(match.systemId, match.productId);
        }

        for (Long systemId: systemIds) {
            JsonOutputSystem system = new JsonOutputSystem(systemId);

            boolean compliantProductExists = false;
            boolean allProductsCompliant = true;
            for (Long productId: (Collection<Long>) installationMap.get(systemId)) {
                JsonOutputProduct product = new JsonOutputProduct(productId);

                Match match = matchMap.get(new ImmutablePair<>(system.id, product.id));
                if (match != null) {
                    product.subscriptionId = match.subscriptionId;
                    product.subscriptionQuantity = match.quantity;
                }
                compliantProductExists = compliantProductExists || (match != null);
                allProductsCompliant = allProductsCompliant && (match != null);

                system.products.add(product);
            }

            if (compliantProductExists) {
                if (allProductsCompliant) {
                    output.compliantSystems.add(system);
                }
                else {
                    output.partiallyCompliantSystems.add(system);
                }
            }
            else {
                output.nonCompliantSystems.add(system);
            }
        }

        List<Subscription> subscriptions = getFacts(new TypeToken<Subscription>(){});
        for (Subscription subscription : subscriptions) {
            if (subscription.quantity > 0) {
                output.remainingSubscriptions.add(new JsonOutputSubscription(subscription.id, subscription.quantity));
            }
        }

        List<Match> invalidMatches = getFacts(new TypeToken<Match>(){}, new Predicate<Match>() {
            @Override
            public boolean evaluate(Match match) {
                return match.kind == INVALID;
            }
        });

        for (Match match : invalidMatches) {
            JsonOutputError error = new JsonOutputError("invalid_pinned_match");
            error.data.put("system_id", match.systemId);
            error.data.put("subscription_id", match.subscriptionId);
            error.data.put("product_id", match.productId);
            output.errors.add(error);
        }

        return output;
    }

    /**
     * Gets the facts of a certain type.
     *
     * @param <T> the generic type of fact, must be Comparable
     * @param token wrapped version of T, necessary because of Java generics implementation
     * @return the facts
     */
    @SuppressWarnings("unchecked")
    private <T extends Comparable<? super T>> List<T> getFacts(final TypeToken<T> type){
        return getFacts(type, TruePredicate.INSTANCE);
    }

    /**
     * Gets the facts of a certain type given a certain condition.
     *
     * @param <T> the generic type of fact, must be Comparable
     * @param token wrapped version of T, necessary because of Java generics implementation
     * @param condition an additional filtering condition
     * @return the facts
     */
    @SuppressWarnings("unchecked")
    private <T extends Comparable<? super T>> List<T> getFacts(final TypeToken<T> token, final Predicate<T> condition){

        Collection<T> unsorted = (Collection<T>) session.getObjects(new ObjectFilter() {
            @Override
            public boolean accept(Object fact) {
                return fact.getClass().equals(token.getType()) && condition.evaluate((T)fact);
            }
        });

        List<T> result = new ArrayList<T>(unsorted);
        Collections.sort(result);

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws Exception {
        if (logger != null) {
            logger.close();
        }
        if (session != null) {
            session.dispose();
        }
    }
}
