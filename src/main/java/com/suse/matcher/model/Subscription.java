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
    // values for virtualizationPolicy
    /**
     * This subscription can exclusively be assigned to a physical system, and
     * virtual machines running on top of it will not get the same subscription
     * for free.
     */
    public static final String PHYSICAL_ONLY = "physical_only";

    /**
     * This subscription can exclusively be assigned to a physical system, and
     * virtual machines running on top of it will automatically get the same
     * subscription for free.
     */
    public static final String UNLIMITED_VIRTUALIZATION = "unlimited_virtualization";

    /**
     * This subscription can either be assigned to a physical system without
     * a SUSE-provided hypervisor or to up to two virtual machines, regardless
     * of the hypervisor they run on.
     */
    public static final String TWO_TWO = "two_two";

    // values for supportLevel
    /** Basic support level. */
    public static final String BASIC = "basic";

    /** Standard support level. */
    public static final String STANDARD = "standard";

    /** Priority support level. */
    public static final String PRIORITY = "priority";



    // JSON fields
    /** The id. */
    public Integer id;

    /** The part number. */
    @SerializedName("part_number")
    public String partNumber;

    /** The count. */
    @SerializedName("system_limit")
    public Integer systemLimit;

    /** Start Date. */
    @SerializedName("starts_at")
    public Date startsAt = new Date(Long.MIN_VALUE);

    /** End Date. */
    @SerializedName("expires_at")
    public Date expiresAt = new Date(Long.MAX_VALUE);

    /** SCC Organization Id. */
    @SerializedName("scc_org_id")
    public String sccOrgId;



    // computed fields
    /** One of PHYSICAL_ONLY, UNLIMITED_VIRTUALIZATION, TWO_TWO or null. */
    public String virtualizationPolicy;

    /** One of BASIC, STANDARD, PRIORITY or null. */
    public String supportLevel;

    /**  Populated CPU sockets or IFLs (s390x architecture), null for "instance subscriptions". */
    public Integer cpus = null;

    /**  Can this subscription be used multiple times on the same system? */
    public Boolean stackable;

    /**  Products that can be licensed with this subscription. */
    public List<Integer> productIds = new LinkedList<Integer>();

    /**  Do product ids contain any RedHat product? */
    public boolean red = false;


    //methods
    /**
     * Checks if is instance subscription.
     *
     * @return the boolean
     */
    public Boolean isInstanceSubscription() {
        return cpus == null;
    }

    /**
     * Match any product on system.
     *
     * @param s the s
     * @return the boolean
     */
    public Boolean matchAnyProductOnSystem(System s) {
        for (Integer p : s.productIds) {
            if (productIds.contains(p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a ranking of the fitness of this subscription to the
     * specified system in the interval [0,1].
     * Higher rank values will be preferred to lower ones when
     * the rule engine will decide upon different possible matches.
     *
     * @param system the system to rank
     * @return a ranking value
     */
    public long computeFitnessTo(System system) {
        // here we compute different scores in the [0, 1] range

        // prefer red subscriptions for red systems
        long rednessScore = (this.isRed() == system.isRed() ? 1 : 0);

        // prefer subscriptions that match (or are close to) the number of needed CPUs
        long cpuScore = 1 - Math.abs(this.cpus - system.cpus) / this.cpus;

        // combine all scores in order of preference
        return (rednessScore * 10 + cpuScore) / 11;
    }

    //getters
    /**
     * Gets the id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
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
     * Gets the SCC org id.
     *
     * @return the SCC org id
     */
    public String getSccOrgId() {
        return sccOrgId;
    }

    /**
     * Gets the virtualization policy.
     *
     * @return the virtualization policy
     */
    public String getVirtualizationPolicy() {
        return virtualizationPolicy;
    }

    /**
     * Gets the support level.
     *
     * @return the support level
     */
    public String getSupportLevel() {
        return supportLevel;
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
     * Gets the stackable attribute.
     *
     * @return the stackable attribute
     */
    public Boolean getStackable() {
        return stackable;
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
     * True if any RedHat product is in this subscription.
     *
     * @return true, if subscription is red
     */
    public boolean isRed() {
        return red;
    }


    // utility methods
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
