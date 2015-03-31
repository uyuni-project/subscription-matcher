package com.suse.matcher.facts;

import org.kie.api.definition.type.PropertyReactive;

import java.util.Date;

/**
 * Encapsulates the current date in a fact, so that it does not change during evaluation.
 */
@PropertyReactive
public class Today {

    /** Current timestamp. */
    public Date timestamp = new Date();

    /**
     * Gets the timestamp.
     *
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }
}
