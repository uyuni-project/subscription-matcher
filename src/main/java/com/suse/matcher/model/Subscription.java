package com.suse.matcher.model;

import com.google.gson.annotations.SerializedName;
import com.suse.matcher.model.System;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.kie.api.definition.type.PropertyReactive;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * An entitlement to use one or more {@link Product}s on one or more
 * {@link System}s.
 */
@PropertyReactive
public class Subscription {

    // JSON fields
    /** The id. */
    public String id;

    /** The type / description. */
    public String type;

    /** The part number */
    @SerializedName("part_number")
    public String partNumber;

    /** The count. */
    @SerializedName("system_limit")
    public Integer systemLimit;

    /** Start Date */
    @SerializedName("starts_at")
    public Date startsAt = new Date(Long.MIN_VALUE);

    /** End Date */
    @SerializedName("expires_at")
    public Date expiresAt = new Date(Long.MAX_VALUE);

    // computed fields
    /** CPUs / socket / IFLs: 0 means instance subscription */
    public Integer cpus;

    /** unlimited virtualization */
    public Boolean unlimitedVirtualization;

    /** stackable */
    public Boolean stackable;

    /** usable for products */
    public List<Integer> usableProductIds = new LinkedList<Integer>();

    /** support type */
    public String supportType;

    /** FIXME: needed? lifetime in years */
    public Integer lifetime;

    public Boolean isInstanceSubscription() {
        return (cpus == 0);
    }

    public Boolean matchAnyProductOnSystem(System s) {
        for (Integer p : s.productIds) {
            if (usableProductIds.contains(p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the part number.
     *
     * @return the part number
     */
    public String getPartNumber() {
        return partNumber;
    }

    /**
     * Gets the system limit.
     *
     * @return the system limit
     */
    public Integer getSystemLimit() {
        return systemLimit;
    }

    /**
     * Gets the start date.
     *
     * @return the starts at
     */
    public Date getStartsAt() {
        return startsAt;
    }

    /**
     * Gets the expiration date.
     *
     * @return the expires at
     */
    public Date getExpiresAt() {
        return expiresAt;
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
     * Gets the unlimited virtualization attribute.
     *
     * @return the unlimited virtualization attribute
     */
    public Boolean getUnlimitedVirtualization() {
        return unlimitedVirtualization;
    }

    /**
     * Gets the stackable attribute.
     *
     * @return the stackable attribute
     */
    public Boolean getStackable() {
        return stackable;
    }

    /**
     * Gets the usable product ids.
     *
     * @return the usable product ids
     */
    public List<Integer> getUsableProductIds() {
        return usableProductIds;
    }

    /**
     * Gets the support type.
     *
     * @return the support type
     */
    public String getSupportType() {
        return supportType;
    }

    /**
     * Gets the lifetime.
     *
     * @return the lifetime
     */
    public Integer getLifetime() {
        return lifetime;
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
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
