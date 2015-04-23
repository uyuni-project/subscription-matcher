package com.suse.matcher.facts;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.kie.api.definition.type.PropertyReactive;

/**
 * Represents a hardware or software entity on which you can install
 * products and that can be assigned {@link Subscription}s.
 */
@PropertyReactive
public class System implements Comparable<System> {
    // constructor-populated fields
    /** The id. */
    public Long id;

    /** The populated CPU socket count. */
    public Integer cpus;

    // rule-computed fields
    /**  <code>true</code> if this is a machine made of metal. */
    public boolean physical = true;

    /**   Do product ids contain any RedHat product?. */
    public boolean red = false;

    /**
     * Instantiates a new system.
     *
     * @param idIn the id
     * @param cpusIn the cpus
     */
    public System(Long idIn, Integer cpusIn) {
        id = idIn;
        cpus = cpusIn;
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
     * Gets the cpus.
     *
     * @return the cpus
     */
    public Integer getCpus() {
        return cpus;
    }

    /**
     * Checks if this system is physical.
     *
     * @return true, if it is physical
     */
    public boolean isPhysical() {
        return physical;
    }

    /**
     * Checks if this system is red.
     *
     * @return true, if it is red
     */
    public boolean isRed() {
        return red;
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(System oIn) {
        return new CompareToBuilder().append(id, oIn.id).toComparison();
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
