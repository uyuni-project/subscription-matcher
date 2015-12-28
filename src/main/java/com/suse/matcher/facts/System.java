package com.suse.matcher.facts;

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
public class System {
    /** The id. */
    public Long id;

    /** The friendly name. */
    public String name;

    /** The populated CPU socket count. */
    public Integer cpus;

    /** <code>true</code> if this is a machine made of metal. */
    public Boolean physical;

    /**
     * Instantiates a new system.
     *
     * @param idIn the id
     * @param nameIn the friendly name
     * @param cpusIn the cpus
     * @param physicalIn true if this system is made of metal
     */
    public System(Long idIn, String nameIn, Integer cpusIn, Boolean physicalIn) {
        id = idIn;
        name = nameIn;
        cpus = cpusIn;
        physical = physicalIn;
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
    public Boolean isPhysical() {
        return physical;
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
        if (!(objIn instanceof System)) {
            return false;
        }
        System other = (System) objIn;
        return new EqualsBuilder()
            .append(id, other.id)
            .isEquals();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .toString();
    }
}
