package com.suse.matcher.json;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Map;

/**
 * JSON representation of the an error detected during the match.
 */
public class JsonOutputError {

    /** A label identifying the error type. */
    public String type;

    /** Arbitrary data connected to this error. */
    public Map<String, String> data;

    /**
     * Instantiates a new json output error.
     *
     * @param typeIn the type
     * @param dataIn the data
     */
    public JsonOutputError(String typeIn, Map<String, String> dataIn) {
        type = typeIn;
        data = dataIn;
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
