package com.suse.matcher.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.kie.api.definition.type.PropertyReactive;

/**
 * An entitlement to use one or more {@link Product}s on one or more
 * {@link System}s.
 */
@PropertyReactive
public class Subscription {

    /** The id. */
    public String id;

    /** The type. */
    public String type;

    /** The count. */
    public Integer count;

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
}
