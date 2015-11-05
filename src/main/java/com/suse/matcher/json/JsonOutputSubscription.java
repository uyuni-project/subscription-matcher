package com.suse.matcher.json;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * JSON representation of a subscription in the matcher's output.
 */
public class JsonOutputSubscription {

    /** The id. */
    public Long id;

    /** The remaining cents. */
    public Integer cents;

    /**
     * Standard constructor.
     *
     * @param idIn the id
     * @param centsIn the number of remaining cents
     */
    public JsonOutputSubscription(Long idIn, Integer centsIn) {
        id = idIn;
        cents = centsIn;
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
