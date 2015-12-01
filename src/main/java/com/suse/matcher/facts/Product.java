package com.suse.matcher.facts;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.kie.api.definition.type.PropertyReactive;

/**
 * Represents a product that can be matched.
 */
@PropertyReactive
public class Product {

    /** The product id. */
    public Long id;

    /** The friendly name. */
    public String name;

    /**
     * Instantiates a new product.
     *
     * @param idIn the product id
     * @param nameIn the friendly name
     */
    public Product(Long idIn, String nameIn) {
        id = idIn;
        name = nameIn;
    }

    /**
     * Gets the product id.
     *
     * @return the product id
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the friendly name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(id)
            .toHashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object objIn) {
        if (!(objIn instanceof Product)) {
            return false;
        }
        Product other = (Product) objIn;
        return new EqualsBuilder()
            .append(id, other.id)
            .isEquals();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", id)
        .append("name", name)
        .toString();
    }
}
