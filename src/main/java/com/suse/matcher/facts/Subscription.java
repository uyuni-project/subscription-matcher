package com.suse.matcher.facts;

import org.apache.commons.lang3.builder.CompareToBuilder;
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
public class Subscription implements Comparable<Subscription> {
    /**
     * Encodes virtual machine assignment policies for a {@link Subscription}
     */
    public enum Policy {
        /**
         * This subscription can exclusively be assigned to a physical system, and
         * virtual machines running on top of it will not get the same subscription
         * for free.
         */
        PHYSICAL_ONLY("Physical deployment only"),
        /**
         * This subscription can exclusively be assigned to a physical system, and
         * virtual machines running on top of it will automatically get the same
         * subscription for free.
         */
        UNLIMITED_VIRTUALIZATION("Unlimited Virtual Machines"),
        /**
         * This subscription can either be assigned to a physical system that does
         * not host virtual machines or to up to two virtual machines.
         */
        ONE_TWO("1-2 Sockets or 1-2 Virtual Machines"),
        /**
         * This subscription can either be assigned to a physical system as well
         * as to a virtual system. It is meant for an instance but does not allow
         * any virtualization inheritance
         */
        INSTANCE("Per-instance"),
        /**
         * This subscription refers to an extension product, as such it requires a
         * compatible base product to be installed on the same system. Assuming the
         * base product has a matching subscription, its policy it has will
         * also be applied to the extension product.
         */
        INHERITED_VIRTUALIZATION("Inherited");

        private String description;

        private Policy(String descriptionIn){
            description = descriptionIn;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return description;
        }
    }

    // constructor-populated fields
    /** The id. */
    public Long id;

    /** The part number. */
    public String partNumber;

    /** The friendly name. */
    public String name;

    /** The number of subscription units (usually systems) available in this subscription. */
    public Integer quantity;

    /** Start Date. */
    public Date startDate;

    /** End Date. */
    public Date endDate;

    /** SCC Username. */
    public String sccUsername;

    // rule-computed fields
    /** Virtualization policy. */
    public Policy policy;

    /** Support level identifier. */
    public String supportLevel;

    /**  Populated CPU sockets or IFLs (s390x architecture), null for "instance subscriptions". */
    public Integer cpus = null;

    /**   Can this subscription be used multiple times on the same system?. */
    public Boolean stackable;

    /** Should this subscription be matched at all? Eg. expired subscriptions can be ignored. */
    public Boolean ignored = false;

    /** The Hard Bundle Id that this subscription belongs to. If null, this doesn't belong to any Hard Bundle */
    public Integer hardBundleId;

    /** Does this subscription on its own represent a hard bundle? */
    public Boolean singleSubscriptionHardBundle = false;

    /**
     * Instantiates a new subscription.
     *
     * @param idIn the id
     * @param partNumberIn the part number
     * @param nameIn the friendly name
     * @param quantityIn the quantity
     * @param startDateIn the starts at
     * @param endDateIn the expires at
     * @param sccUsernameIn the scc org id
     */
    public Subscription(Long idIn, String partNumberIn, String nameIn, Integer quantityIn, Date startDateIn, Date endDateIn,
            String sccUsernameIn) {
        id = idIn;
        partNumber = partNumberIn;
        name = nameIn;
        quantity = quantityIn;
        startDate = startDateIn;
        endDate = endDateIn;
        sccUsername = sccUsernameIn;
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
     * Returns true if this subscription has to be ignored for the matching
     * (eg. because it is expired)
     *
     * @return true if the subscription is to be ignored
     */
    public Boolean getIgnored() {
        return ignored;
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
     * Gets the number of subscriptions available.
     *
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Gets the start date.
     *
     * @return the start date
     */
    public Date getStartsAt() {
        return startDate;
    }

    /**
     * Gets the end date.
     *
     * @return the end date
     */
    public Date getExpiresAt() {
        return endDate;
    }

    /**
     * Gets the SCC username.
     *
     * @return the SCC username
     */
    public String getSccUsername() {
        return sccUsername;
    }

    /**
     * Gets the virtualization policy.
     *
     * @return the virtualization policy
     */
    public Policy getPolicy() {
        return policy;
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
     * @return Returns the hardBundleId.
     */
    public Integer getHardBundleId() {
        return hardBundleId;
    }

    /**
     * @return true if this subscription on its own represents a hard bundle
     */
    public Boolean getSingleSubscriptionHardBundle() {
        return singleSubscriptionHardBundle;
    }

    /**
     * @param hardBundleId The hardBundleId to set.
     */
    public void setHardBundleId(Integer hardBundleId) {
        this.hardBundleId = hardBundleId;
    }

    /**
     * Sets the name.
     *
     * @param nameIn - the name
     */
    public void setName(String nameIn) {
        name = nameIn;
    }

    /**
     * Sets the singleSubscriptionHardBundle.
     *
     * @param singleSubscriptionHardBundleIn - the singleSubscriptionHardBundle
     */
    public void setSingleSubscriptionHardBundle(Boolean singleSubscriptionHardBundleIn) {
        singleSubscriptionHardBundle = singleSubscriptionHardBundleIn;
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(Subscription oIn) {
        return new CompareToBuilder().append(id, oIn.id).toComparison();
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
        if (!(objIn instanceof Subscription)) {
            return false;
        }
        Subscription other = (Subscription) objIn;
        return new EqualsBuilder()
            .append(id, other.id)
            .isEquals();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("hardBundleId", hardBundleId)
            .toString();
    }
}
