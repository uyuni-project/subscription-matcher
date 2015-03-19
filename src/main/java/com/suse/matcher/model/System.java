package com.suse.matcher.model;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.kie.api.definition.type.PropertyReactive;

import java.util.HashSet;
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
    public String id;

    /** The populated CPU socket count. */
    public Integer cpus;

    /** The arch. */
    public String arch;

    /** The parent id. */
    @SerializedName("parent_id")
    public String parentId;

    /** IDs of installed products. */
    @SerializedName("product_ids")
    public List<String> productIds;

    // computed fields
    /** List of subscriptions applicable to this system. */
    public Set<Subscription> applicableSubscriptions = new HashSet<Subscription>();

    /**
     * Checks if this system is physical (not virtual).
     *
     * @return true, if is physical
     */
    public boolean isPhysical() {
        return parentId == null;
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
}