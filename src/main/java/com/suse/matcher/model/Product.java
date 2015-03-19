package com.suse.matcher.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.kie.api.definition.type.PropertyReactive;

/**
 * A piece of software which can be installed to a {@link System} and requires a
 * {@link Subscription}.
 */
@PropertyReactive
public class Product {

    /** The id. */
    public String id;

    /** The description. */
    public String description;

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
