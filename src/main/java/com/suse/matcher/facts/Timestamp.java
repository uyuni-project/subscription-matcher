package com.suse.matcher.facts;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.kie.api.definition.type.PropertyReactive;

import java.util.Date;

/**
 * Encapsulates the date and time when the matching is performed. This wrapper
 * class is needed because we might want to simulate a match in the past
 * (typically for testing and debugging) or future (simulations).
 */
@PropertyReactive
public class Timestamp {

    /** Current timestamp. */
    public Date timestamp;

    /**
     * Instantiates a new current time.
     *
     * @param timestampIn the timestamp
     */
    public Timestamp(Date timestampIn) {
        timestamp = timestampIn;
    }

    /**
     * Gets the timestamp.
     *
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(timestamp)
            .toHashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object objIn) {
        if (!(objIn instanceof Timestamp)) {
            return false;
        }
        Timestamp other = (Timestamp) objIn;
        return new EqualsBuilder()
            .append(timestamp, other.timestamp)
            .isEquals();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("timestamp", timestamp)
        .toString();
    }
}
