package com.suse.matcher.json;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.LinkedList;
import java.util.List;

/**
 * JSON representation of the matcher's output.
 */
public class JsonOutput {

    /** The compliant systems. */
    public List<JsonOutputSystem> compliantSystems = new LinkedList<>();

    /**
     * The partially compliant systems (at least one compliant and one
     * non-compliant product installed).
     */
    public List<JsonOutputSystem> partiallyCompliantSystems = new LinkedList<>();

    /** The non compliant systems. */
    public List<JsonOutputSystem> nonCompliantSystems = new LinkedList<>();

    /** The remaining subscriptions after the matching. */
    public List<JsonOutputSubscription> remainingSubscriptions = new LinkedList<>();

    /** The messages. */
    public List<JsonOutputMessage> messages = new LinkedList<>();

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object objIn) {
        return EqualsBuilder.reflectionEquals(this, objIn);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

