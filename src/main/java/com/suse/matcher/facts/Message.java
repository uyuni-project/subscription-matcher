package com.suse.matcher.facts;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Map;

/**
 * Represents a message to be presented to the user, as a secondary informative output (errors, warnings, etc.)
 */
public class Message implements Comparable<Message> {

    /** A label identifying the message type. */
    public String type;

    /** Arbitrary data connected to this message. */
    public Map<String, String> data;

    /**
     * Instantiates a new message.
     *
     * @param typeIn the type
     * @param dataIn the data
     */
    public Message(String typeIn, Map<String, String> dataIn) {
        type = typeIn;
        data = dataIn;
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(Message oIn) {
        return new CompareToBuilder()
            .append(type, oIn.type)
            // we don't really care about the ordering at this point, provided
            // it's consistent between runs. Hash code does the trick
            .append(data.hashCode(), oIn.data.hashCode())
            .toComparison();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(type)
            .toHashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object objIn) {
        if (!(objIn instanceof Message)) {
            return false;
        }
        Message other = (Message) objIn;
        return new EqualsBuilder()
            .append(type, other.type)
            .append(data, other.data)
            .isEquals();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("type", type)
            .append("data", data)
            .toString();
    }
}
