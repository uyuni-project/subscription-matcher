package com.suse.matcher.json;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * JSON representation of a subscription in the matcher's output.
 */
public class JsonOutputSubscription {

    /** The id. */
    public Long id;

    /** The quantity. */
    public Double quantity;

    /**
     * Standard constructor.
     *
     * @param idIn the id
     * @param quantityIn the quantity
     */
    public JsonOutputSubscription(Long idIn, Double quantityIn) {
        id = idIn;
        quantity = quantityIn;
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
