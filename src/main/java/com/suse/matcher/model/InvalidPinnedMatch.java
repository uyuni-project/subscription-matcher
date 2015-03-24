package com.suse.matcher.model;


/**
 * Represents match of a subscription to a system requested by the user, but invalid.
 */
public class InvalidPinnedMatch extends Match {

    /**
     * Instantiates a new pinned match.
     *
     * @param systemIdIn the system id in
     * @param subscriptionIdIn the subscription id in
     */
    public InvalidPinnedMatch(String systemIdIn, String subscriptionIdIn) {
        super(systemIdIn, subscriptionIdIn);
    }
}
