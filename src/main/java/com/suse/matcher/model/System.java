package com.suse.matcher.model;

import com.google.gson.annotations.SerializedName;
import com.suse.matcher.ProductData;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.kie.api.definition.type.PropertyReactive;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Represents a hardware or software entity on which you can install
 * {@link Product}s and that can be assigned {@link Subscription}s.
 */
@PropertyReactive
public class System {

    // JSON fields
    /** The id. */
    public Integer id;

    /** The populated CPU socket count. */
    public Integer cpus;

    /** virtual machines */
    @SerializedName("virtual_system_ids")
    public Set<String> virtualSystemIds = new HashSet<String>();

    /** IDs of installed products. */
    @SerializedName("product_ids")
    public List<Integer> productIds = new LinkedList<Integer>();

    /** <code>true</code> if this is a machine made of metal */
    public boolean physical = true;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Gets the cpus.
     *
     * @return the cpus
     */
    public Integer getCpus() {
        return cpus;
    }

    /**
     * Gets the virtual system ids.
     *
     * @return the virtual system ids
     */
    public Set<String> getVirtualSystemIds() {
        return virtualSystemIds;
    }

    /**
     * Gets the product ids.
     *
     * @return the product ids
     */
    public List<Integer> getProductIds() {
        return productIds;
    }

    /**
     * Checks if is physical.
     *
     * @return true, if is physical
     */
    public boolean isPhysical() {
        return physical;
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
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("cpus", cpus)
            .append("virtualSystemIds", virtualSystemIds)
            .append("products", ProductData.getInstance().getFriendlyNames(productIds))
            .toString();
    }
}
