package com.suse.matcher.facts;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.kie.api.definition.type.PropertyReactive;

import java.util.Date;

/**
 * Encapsulates the date when the matching is performed. This wrapper class is needed
 * because the fact object must not change during Drools execution.
 */
@PropertyReactive
public class CurrentTime {

    /** Current timestamp. */
    public Date timestamp;

    /**
     * Instantiates a new current time.
     *
     * @param timestampIn the timestamp
     */
    public CurrentTime(Date timestampIn) {
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
        if (!(objIn instanceof CurrentTime)) {
            return false;
        }
        CurrentTime other = (CurrentTime) objIn;
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
