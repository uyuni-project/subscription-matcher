package com.suse.matcher.facts;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.kie.api.definition.type.PropertyReactive;

/**
 * Represents the association of a {@link Subscription} to an installation of a
 * product, that is, to a ({@link System}, product) couple.
 */
@PropertyReactive
public class Match implements Comparable<Match> {

    /**
     * Kind of this match
     */
    public enum Kind {
        /** Rule engine established that this match can be used */
        POSSIBLE,

        /** Rule engine established it will be part of the result */
        CONFIRMED,

        /** User wants this match, rule engine did not yet confirm it is valid */
        USER_PINNED,

        /** User wanted this match but rule engine denies it's OK to use */
        INVALID
    }

    /** The system id. */
    public Long systemId;

    /** The subscription id. */
    public Long subscriptionId;

    /** The product id. */
    public Long productId;

    /** The number of subscriptions used in this match. */
    public Integer quantity;

    /** The kind. */
    public Kind kind;

    /**
     * Standard constructor.
     *
     * @param systemIdIn a system id
     * @param subscriptionIdIn an id of subscription assigned to the system
     * @param productIdIn an id of a product
     * @param quantityIn the number of subscriptions used in this match
     * @param kindIn the match kind
     */
    public Match(Long systemIdIn, Long subscriptionIdIn, Long productIdIn, Integer quantityIn, Kind kindIn) {
        systemId = systemIdIn;
        subscriptionId = subscriptionIdIn;
        productId = productIdIn;
        kind = kindIn;
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

    /**
     * Gets the quantity.
     *
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Gets the kind.
     *
     * @return the kind
     */
    public Kind getKind() {
        return kind;
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(Match oIn) {
        return CompareToBuilder.reflectionCompare(this, oIn);
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
