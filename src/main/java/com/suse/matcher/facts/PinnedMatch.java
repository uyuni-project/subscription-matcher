package com.suse.matcher.facts;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Represents a match that the customer would like to see in the solution.
 *
 * @see PossibleMatch
 */
public class PinnedMatch {

    /** The system id. */
    public Long systemId;

    /** The product id. */
    public Long productId;

    /** The subscription id. */
    public Long subscriptionId;

    /**
     * Standard constructor.
     *
     * @param systemIdIn a system id
     * @param productIdIn an id of a product
     * @param subscriptionIdIn an id of subscription assigned to the system
     */
    public PinnedMatch(Long systemIdIn, Long productIdIn, Long subscriptionIdIn) {
        systemId = systemIdIn;
        productId = productIdIn;
        subscriptionId = subscriptionIdIn;
    }

    /**
     * Default constructor.
     */
    public PinnedMatch() {
    }

    /**
     * Gets the system id.
     *
     * @return the system id
     */
    public Long getSystemId() {
        return systemId;
    }

    /**
     * Gets the product id.
     *
     * @return the product id
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * Gets the subscription id.
     *
     * @return the subscription id
     */
    public Long getSubscriptionId() {
        return subscriptionId;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(systemId)
            .append(productId)
            .append(subscriptionId)
            .toHashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object objIn) {
        if (!(objIn instanceof PinnedMatch)) {
            return false;
        }
        PinnedMatch other = (PinnedMatch) objIn;
        return new EqualsBuilder()
            .append(systemId, other.systemId)
            .append(productId, other.productId)
            .append(subscriptionId, other.subscriptionId)
            .isEquals();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("systemId", systemId)
            .append("productId", productId)
            .append("subscriptionId", subscriptionId)
            .toString();
    }
}
