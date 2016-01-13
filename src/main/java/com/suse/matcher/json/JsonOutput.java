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

    /** The confirmed matches */
    public List<JsonMatch> confirmedMatches = new LinkedList<>();

    /** The messages */
    public List<JsonMessage> messages = new LinkedList<>();

}
