package com.suse.matcher.facts;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.kie.api.definition.type.PropertyReactive;

/**
 * Represents a subscription to product relationship.
 */
@PropertyReactive
public class SubscriptionProduct {

    /** The subscription id. */
    public Long subscriptionId;

    /** The product id. */
    public Long productId;

    /**
     * Instantiates a new installation relationship.
     *
     * @param subscriptionIdIn the subscription id
     * @param productIdIn the product id
     */
    public SubscriptionProduct(Long subscriptionIdIn, Long productIdIn) {
        subscriptionId = subscriptionIdIn;
        productId = productIdIn;
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
     * Gets the product id.
     *
     * @return the product id
     */
    public Long getProductId() {
        return productId;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(subscriptionId)
            .append(productId)
            .toHashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object objIn) {
        if (!(objIn instanceof SubscriptionProduct)) {
            return false;
        }
        SubscriptionProduct other = (SubscriptionProduct) objIn;
        return new EqualsBuilder()
            .append(subscriptionId, other.subscriptionId)
            .append(productId, other.productId)
            .isEquals();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("subscriptionId", subscriptionId)
            .append("productId", productId)
            .toString();
    }
}
