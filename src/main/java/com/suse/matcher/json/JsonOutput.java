package com.suse.matcher.json;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * JSON representation of the matcher's output.
 */
public class JsonOutput {

    /** Date and time of the match */
    public Date timestamp;

    /** The systems */
    public List<JsonSystem> systems = new LinkedList<>();

    /** The products */
    public List<JsonProduct> products = new LinkedList<>();

    /** The subscriptions */
    public List<JsonSubscription> subscriptions = new LinkedList<>();

    /** The pinned matches */
    public List<JsonMatch> pinnedMatches = new LinkedList<>();

    /** The confirmed matches */
    public List<JsonMatch> confirmedMatches = new LinkedList<>();

    /** The messages */
    public List<JsonMessage> messages = new LinkedList<>();

}
