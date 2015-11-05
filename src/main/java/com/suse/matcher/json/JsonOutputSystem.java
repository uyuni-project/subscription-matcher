package com.suse.matcher.json;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.LinkedList;
import java.util.List;

/**
 * JSON representation of a system in the matcher's output.
 */
public class JsonOutputSystem {

    /** The id. */
    public Long id;

    /** The products installed on this system. */
    public List<JsonOutputProduct> products = new LinkedList<>();

    /**
     * Standard constructor.
     *
     * @param idIn the id
     */
    public JsonOutputSystem(Long idIn) {
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
