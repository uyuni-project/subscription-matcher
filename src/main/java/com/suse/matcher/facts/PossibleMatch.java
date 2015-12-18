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

    /** A unique identifier for this PossibleMatch. */
    public Long id;

    /** The system id. */
    public Long systemId;

    /** The product id. */
    public Long productId;

    /** The subscription id. */
    public Long subscriptionId;

    /** The number of subscription cents used in this match. */
    public Integer cents;

    /**
     * Standard constructor.
     *
     * @param systemIdIn a system id
     * @param productIdIn an id of a product
     * @param subscriptionIdIn an id of subscription assigned to the system
     * @param centsIn the number of subscription cents used in this match
     */
    public PossibleMatch(Long systemIdIn, Long productIdIn, Long subscriptionIdIn, Integer centsIn) {
        systemId = systemIdIn;
        productId = productIdIn;
        subscriptionId = subscriptionIdIn;
        cents = centsIn;
        id = new Long(this.hashCode());
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
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
     * Gets the number of subscription cents used in this match.
     *
     * @return the cents
     */
    public Integer getCents() {
        return cents;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(systemId)
            .append(productId)
            .append(subscriptionId)
            .append(cents)
            .toHashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object objIn) {
        if (!(objIn instanceof PossibleMatch)) {
            return false;
        }
        PossibleMatch other = (PossibleMatch) objIn;
        return new EqualsBuilder()
            .append(systemId, other.systemId)
            .append(productId, other.productId)
            .append(subscriptionId, other.subscriptionId)
            .append(cents, other.cents)
            .isEquals();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("systemId", systemId)
            .append("productId", productId)
            .append("subscriptionId", subscriptionId)
            .append("cents", cents)
            .toString();
    }
}
