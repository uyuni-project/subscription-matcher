package com.suse.matcher.json;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * JSON representation of a match.
 */
public class JsonMatch {

    /** The system id. */
    @SerializedName("system_id")
    public Long systemId;

    /** The subscription id. */
    @SerializedName("subscription_id")
    public Long subscriptionId;

    /** The product id. */
    @SerializedName("product_id")
    public Long productId;

    /** The number of subscriptions used in this match. */
    public Integer quantity;

    /**
     * Instantiates a new match.
     *
     * @param systemIdIn the system id
     * @param subscriptionIdIn the subscription id
     * @param productIdIn an id of a product
     * @param quantityIn the quantity
     */
    public JsonMatch(Long systemIdIn, Long subscriptionIdIn, Long productIdIn, Integer quantityIn) {
        systemId = systemIdIn;
        subscriptionId = subscriptionIdIn;
        productId = productIdIn;
        quantity = quantityIn;
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
