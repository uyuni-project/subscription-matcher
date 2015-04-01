package com.suse.matcher.facts;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.kie.api.definition.type.PropertyReactive;

import java.util.Date;

/**
 * An entitlement to use one or more products on one or more
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


    // constructor-populated fields
    /** The id. */
    public Long id;

    /** The part number. */
    public String partNumber;

    /** The count. */
    public Integer systemLimit;

    /** Start Date. */
    public Date startsAt = new Date(Long.MIN_VALUE);

    /** End Date. */
    public Date expiresAt = new Date(Long.MAX_VALUE);

    /** SCC Organization Id. */
    public String sccOrgId;

    // rule-computed fields
    /** One of PHYSICAL_ONLY, UNLIMITED_VIRTUALIZATION, TWO_TWO or null. */
    public String virtualizationPolicy;

    /** Support level identifier. */
    public String supportLevel;

    /**  Populated CPU sockets or IFLs (s390x architecture), null for "instance subscriptions". */
    public Integer cpus = null;

    /**   Can this subscription be used multiple times on the same system?. */
    public Boolean stackable;

    /**   Do product ids contain any RedHat product?. */
    public boolean red = false;


    /**
     * Instantiates a new subscription.
     *
     * @param idIn the id
     * @param partNumberIn the part number
     * @param systemLimitIn the system limit
     * @param startsAtIn the starts at
     * @param expiresAtIn the expires at
     * @param sccOrgIdIn the scc org id
     */
    public Subscription(Long idIn, String partNumberIn, Integer systemLimitIn, Date startsAtIn, Date expiresAtIn, String sccOrgIdIn) {
        id = idIn;
        partNumber = partNumberIn;
        systemLimit = systemLimitIn;
        startsAt = startsAtIn;
        expiresAt = expiresAtIn;
        sccOrgId = sccOrgIdIn;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
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
     * @return the start date
     */
    public Date getStartsAt() {
        return startsAt;
    }

    /**
     * Gets the end date.
     *
     * @return the end date
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
     * Gets the stackable.
     *
     * @return the stackable
     */
    public Boolean getStackable() {
        return stackable;
    }

    /**
     * Checks if is red.
     *
     * @return true, if is red
     */
    public boolean isRed() {
        return red;
    }

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
        long rednessScore = (this.red == system.red ? 1 : 0);

        // prefer subscriptions that match (or are close to) the number of needed CPUs
        long cpuScore = 1 - Math.abs(this.cpus - system.cpus) / this.cpus;

        // combine all scores in order of preference
        return (rednessScore * 10 + cpuScore) / 11;
    }

    /**
     * Returns the maximum number of instances of this subscriptions that can be
     * used on the system.
     *
     * This method only makes sense on stackable subscriptions for physical
     * systems.
     *
     * @param system the system
     * @param alreadyLicensedCpus the number of CPUs that are already licensed
     * @return the integer
     */
    public Integer computeQuantityFor(System system, Number alreadyLicensedCpus) {
        Integer necessaryCpus = system.cpus - alreadyLicensedCpus.intValue();
        Integer necessarySubscriptions = necessaryCpus/this.cpus + necessaryCpus % this.cpus;
        return Math.min(systemLimit, necessarySubscriptions);
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
