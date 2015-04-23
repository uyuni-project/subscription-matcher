package com.suse.matcher.json;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * JSON representation of a product in the matcher's output.
 */
public class JsonOutputProduct {

    /** The id. */
    public Long id;

    /** A matching subscription ID or null. */
    public Long subscriptionId;

    /** The amount of used subscription cents or null */
    public Integer subscriptionCents;

    /**
     * Standard constructor.
     *
     * @param idIn the id
     */
    public JsonOutputProduct(Long idIn) {
        id = idIn;
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
