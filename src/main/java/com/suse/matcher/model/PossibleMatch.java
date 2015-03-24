package com.suse.matcher.model;

/**
 * Represents match of a subscription to a system requested by the user.
 */
public class PossibleMatch extends Match {

    /**
     * Instantiates a new possible match.
     *
     * @param systemIdIn the system id in
     * @param subscriptionIdIn the subscription id in
     */
    public PossibleMatch(String systemIdIn, String subscriptionIdIn) {
        super(systemIdIn, subscriptionIdIn);
    }
}
