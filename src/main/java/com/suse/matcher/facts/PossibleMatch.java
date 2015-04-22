package com.suse.matcher.facts;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.kie.api.definition.type.PropertyReactive;

/**
 *
 * A match associates an installation to a {@link Subscription} (an installation
 * is a ({@link System}, product) couple).
 *
 * A match is possible when the {@link Subscription} is applicable to the
 * {@link System} and the product.
 *
 * Note that the above definition does not take other matches into account - a
 * match is possible when it makes sense on its own.
 *
 * The "correct" mix of PossibleMatches, if it exists, is determined in an
 * {@link com.suse.matcher.solver.Assignment}.
 */
@PropertyReactive
public class PossibleMatch {

    /** The system id. */
    public Long systemId;

    /** The product id. */
    public Long productId;

    /** The subscription id. */
    public Long subscriptionId;

    /** The number of subscriptions used in this match. */
    public Double quantity;

    /**
     * Standard constructor.
     *
     * @param systemIdIn a system id
     * @param productIdIn an id of a product
     * @param subscriptionIdIn an id of subscription assigned to the system
     * @param quantityIn the number of subscriptions used in this match
     */
    public PossibleMatch(Long systemIdIn, Long productIdIn, Long subscriptionIdIn, Double quantityIn) {
        systemId = systemIdIn;
        productId = productIdIn;
        subscriptionId = subscriptionIdIn;
        quantity = quantityIn;
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

    /**
     * Gets the quantity.
     *
     * @return the quantity
     */
    public Double getQuantity() {
        return quantity;
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
