package com.suse.matcher.model;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents match of a subscription to a system requested by the user.
 */
public class Match {

    /** The system id. */
    @SerializedName("system_id")
    public String systemId;

    /** The subscription id. */
    @SerializedName("subscription_id")
    public String subscriptionId;

    /**
     * Standard constructor.
     *
     * @param systemIdIn a system id
     * @param subscriptionIdIn an id of subscription assigned to the system
     */
    public Match(String systemIdIn, String subscriptionIdIn) {
        super();
        systemId = systemIdIn;
        subscriptionId = subscriptionIdIn;
    }

    /**
     * Gets the system id.
     *
     * @return the system id
     */
    public String getSystemId() {
        return systemId;
    }

    /**
     * Sets the system id.
     *
     * @param systemIdIn the new system id
     */
    public void setSystemId(String systemIdIn) {
        systemId = systemIdIn;
    }

    /**
     * Gets the subscription id.
     *
     * @return the subscription id
     */
    public String getSubscriptionId() {
        return subscriptionId;
    }

    /**
     * Sets the subscription id.
     *
     * @param subscriptionIdIn the new subscription id
     */
    public void setSubscriptionId(String subscriptionIdIn) {
        subscriptionId = subscriptionIdIn;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

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
