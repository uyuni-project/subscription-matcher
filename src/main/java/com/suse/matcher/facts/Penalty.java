package com.suse.matcher.facts;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.kie.api.definition.type.PropertyReactive;

/**
 * When calculating the total amount of subscription cents used, there
 * might be occasions in which a penalty is applied. This fact encapsulates
 * the penalty applied to a certain subscription.
 */
@PropertyReactive
public class Penalty {

    /** The subscription id. */
    public Long subscriptionId;

    /** The host id. */
    public Long hostId;

    /** The penalty cents. */
    public int cents;

    /**
     * Instantiates a new penalty.
     *
     * @param subscriptionIdIn the subscription id
     * @param hostIdIn the host id
     * @param centsIn the penalty cents
     */
    public Penalty(Long subscriptionIdIn, Long hostIdIn, int centsIn) {
        subscriptionId = subscriptionIdIn;
        hostId = hostIdIn;
        cents = centsIn;
    }

    /**
     * Gets the subscription id.
     *
     * @return the subscription id
     */
    public Long getSubscriptionId() {
        return subscriptionId;
    }

    /**
     * Gets the host id.
     *
     * @return the host id
     */
    public Long getHostId() {
        return hostId;
    }

    /**
     * Gets the penalty cents.
     *
     * @return the penalty cents
     */
    public int getCents() {
        return cents;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(subscriptionId)
            .append(hostId)
            .append(cents)
            .toHashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object objIn) {
        if (!(objIn instanceof Penalty)) {
            return false;
        }
        Penalty other = (Penalty) objIn;
        return new EqualsBuilder()
            .append(subscriptionId, other.subscriptionId)
            .append(hostId, other.hostId)
            .append(cents, other.cents)
            .isEquals();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("subscriptionId", subscriptionId)
        .append("penaltyCents", cents)
        .toString();
    }
}
