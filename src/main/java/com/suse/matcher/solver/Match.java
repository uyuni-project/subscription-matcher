package com.suse.matcher.solver;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

/**
 * Represents the association of a subscription to an installation of a
 * product, that is, to a (system, product) couple.
 */
@PlanningEntity
public class Match implements Comparable<Match> {

    /** A unique identifier for this Match. */
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
     * True if this match is taken by the planner, false if it is possible but
     * not taken, null if the planner did not evaluate this match yet.
     */
    public Boolean confirmed;

    /**
     * Standard constructor.
     *
     * @param idIn the id
     * @param systemIdIn a system id
     * @param productIdIn an id of a product
     * @param subscriptionIdIn an id of subscription assigned to the system
     * @param centsIn the number of subscription cents used in this match
     * @param confirmedIn confirmation status
     */
    public Match(Long idIn, Long systemIdIn, Long productIdIn, Long subscriptionIdIn, Integer centsIn, Boolean confirmedIn) {
        id = idIn;
        systemId = systemIdIn;
        productId = productIdIn;
        subscriptionId = subscriptionIdIn;
        cents = centsIn;
        confirmed = confirmedIn;
    }

    /**
     * Default constructor.
     */
    public Match() {
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

    /**
     * Checks if is confirmed.
     *
     * @return the boolean
     */
    @PlanningVariable(valueRangeProviderRefs = {"booleanRange"})
    public Boolean getConfirmed() {
        return confirmed;
    }

    /**
     * Sets this match confirmed.
     *
     * @param confirmedIn the new confirmed value
     */
    public void setConfirmed(Boolean confirmedIn) {
        confirmed = confirmedIn;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(systemId)
            .append(productId)
            .append(subscriptionId)
            .append(cents)
            // confirmed is a planning variable, so it must not be in hashCode
            .toHashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object objIn) {
        if (!(objIn instanceof Match)) {
            return false;
        }
        Match other = (Match) objIn;
        return new EqualsBuilder()
            .append(systemId, other.systemId)
            .append(productId, other.productId)
            .append(subscriptionId, other.subscriptionId)
            .append(cents, other.cents)
            // confirmed is a planning variable, so it must not be in equals
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

    /** {@inheritDoc} */
    @Override
    public int compareTo(Match other) {
        return new CompareToBuilder()
            .append(systemId, other.systemId)
            .append(productId, other.productId)
            .append(subscriptionId, other.subscriptionId)
            .append(cents, other.cents)
            .toComparison();
    }
}
