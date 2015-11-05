package com.suse.matcher.json;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * JSON representation of a match.
 */
public class JsonMatch {

    /** The system id. */
    public Long systemId;

    /** The product id. */
    public Long productId;

    /** The subscription id. */
    public Long subscriptionId;

    /**
     * Instantiates a new match.
     *
     * @param systemIdIn the system id
     * @param productIdIn an id of a product
     * @param subscriptionIdIn the subscription id
     */
    public JsonMatch(Long systemIdIn, Long productIdIn, Long subscriptionIdIn) {
        systemId = systemIdIn;
        productId = productIdIn;
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
