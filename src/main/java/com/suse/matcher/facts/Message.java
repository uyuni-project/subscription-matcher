package com.suse.matcher.facts;

import static java.util.stream.Collectors.toList;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Represents a message to be presented to the user, as a secondary informative output (errors, warnings, etc.)
 */
public class Message implements Comparable<Message> {

    /**
     * Represents the severity level of this message.
     */
    public enum Level {
        /** Relevant for debugging, not to be shown to users in general. */
        DEBUG,
        /** Information for users, normal functioning. */
        INFO,
        /** Warnings about things that went wrong but still don't block the matcher. */
        WARNING
    }

    /** The severity level of this message. */
    public Level severity;

    /** A label identifying the message type. */
    public String type;

    /** Arbitrary data connected to this message. */
    public Map<String, String> data;

    /**
     * Instantiates a new message.
     *
     * @param severityIn the severity
     * @param typeIn the type
     * @param dataIn the data
     */
    public Message(Level severityIn, String typeIn, Map<String, String> dataIn) {
        severity = severityIn;
        type = typeIn;
        data = dataIn;
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(Message oIn) {
        return new CompareToBuilder()
            .append(type, oIn.type)
            .append(data, oIn.data, new Comparator<Map<String, String>>() {
                @Override
                public int compare(Map<String, String> o1In, Map<String, String> o2In) {
                    List<String> keys = o1In.keySet().stream().sorted().collect(toList());
                    for (String key : keys) {
                        int comparison = o1In.get(key).compareTo(o2In.get(key));
                        if (comparison != 0) {
                            return comparison;
                        }
                    }
                    return 0;
                }
            })
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
