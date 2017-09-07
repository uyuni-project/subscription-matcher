package com.suse.matcher.facts;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.kie.api.definition.type.PropertyReactive;

/**
 * When calculating the total amount of subscription cents used, there
 * might be occasions in which a penalty is applied. This fact encapsulates
 * the penalty applied to a certain subscription and penalty group.
 */
@PropertyReactive
public class Penalty {

    /** The subscription id. */
    public SubscriptionId subscriptionId;

    /** The penalty group id. */
    public int penaltyGroupId;

    /** The penalty cents. */
    public int cents;

    /**
     * Instantiates a new penalty.
     *
     * @param subscriptionIdIn the subscription id
     * @param penaltyGroupIdIn the penalty group id
     * @param centsIn the penalty cents
     */
    public Penalty(SubscriptionId subscriptionIdIn, int penaltyGroupIdIn, int centsIn) {
        subscriptionId = subscriptionIdIn;
        penaltyGroupId = penaltyGroupIdIn;
        cents = centsIn;
    }

    /**
     * Gets the subscription id.
     *
     * @return the subscription id
     */
    public SubscriptionId getSubscriptionId() {
        return subscriptionId;
    }

    /**
     * Gets the penaltyGroupId.
     *
     * @return penaltyGroupId
     */
    public int getPenaltyGroupId() {
        return penaltyGroupId;
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
            .append(penaltyGroupId)
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
            .append(penaltyGroupId, other.penaltyGroupId)
            .append(cents, other.cents)
            .isEquals();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("subscriptionId", subscriptionId)
        .append("penaltyGroupId", penaltyGroupId)
        .append("penaltyCents", cents)
        .toString();
    }
}
