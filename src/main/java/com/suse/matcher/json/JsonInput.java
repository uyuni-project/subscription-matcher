package com.suse.matcher.json;

import java.util.List;

/**
 * JSON representation of the matcher's input.
 */
public class JsonInput {

    /** The systems */
    public List<JsonInputSystem> systems;

    /** The products */
    public List<JsonInputProduct> products;

    /** The subscriptions */
    public List<JsonInputSubscription> subscriptions;

    /** The pinned matches */
    public List<JsonInputPinnedMatch> pinnedMatches;
}
