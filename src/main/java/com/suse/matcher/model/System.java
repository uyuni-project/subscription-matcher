package com.suse.matcher.model;

import com.google.gson.annotations.SerializedName;

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
    public String id;

    /** The populated CPU socket count. */
    public Integer cpus;

    /** virtual machines */
    @SerializedName("virtual_system_ids")
    public Set<String> virtualSystemIds = new HashSet<String>();

    /** IDs of installed products. */
    @SerializedName("product_ids")
    public List<String> productIds = new LinkedList<String>();

    // computed fields
    /** List of subscriptions applicable to this system. */
    public Set<Subscription> applicableSubscriptions = new HashSet<Subscription>();

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
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
