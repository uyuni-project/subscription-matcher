package com.suse.matcher.model;


/**
 * Represents match of a subscription to a system requested by the user.
 */
public class PinnedMatch extends Match {

    /**
     * Instantiates a new pinned match.
     *
     * @param systemIdIn the system id in
     * @param subscriptionIdIn the subscription id in
     */
    public PinnedMatch(Integer systemIdIn, Integer subscriptionIdIn) {
        super(systemIdIn, subscriptionIdIn);
    }
}
