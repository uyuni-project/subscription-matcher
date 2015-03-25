package com.suse.matcher.model;

import java.util.Date;

/**
 * Encapsulates the current date in a fact, so that it does not change during evaluation.
 */
public class Today {

    /** Current timestamp. */
    public Date timestamp = new Date();


    public Date getTimestamp() {
        return timestamp;
    }
}
