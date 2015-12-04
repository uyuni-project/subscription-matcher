package com.suse.matcher.json;

import java.util.List;

/**
 * JSON representation of the matcher's input.
 */
public class JsonInput {

    /** The systems */
    public List<JsonSystem> systems;

    /** The products */
    public List<JsonProduct> products;

    /** The subscriptions */
    public List<JsonSubscription> subscriptions;

    /** The pinned matches */
    public List<JsonMatch> pinnedMatches;
}
