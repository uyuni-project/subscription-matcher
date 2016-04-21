package com.suse.matcher.facts;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A relationship between a Virtualization Group and one of its Virtual Guest members.
 *
 * A Virtualization Group contains virtual guests which belong to the same cloud, VMWare
 * vCenter, etc.
 */
public class VirtualizationGroupMember {

    /** The virtualization group id. */
    public int virtualizationGroupId;

    /** The guest system id. */
    public Long guestId;

    /**
     * Instantiates a new virtualization group member.
     *
     * @param virtualizationGroupIdIn the virtualization group id in
     * @param guestIdIn the guest id in
     */
    public VirtualizationGroupMember(int virtualizationGroupIdIn, Long guestIdIn) {
        virtualizationGroupId = virtualizationGroupIdIn;
        guestId = guestIdIn;
    }

    /**
     * Gets the virtualization group id.
     *
     * @return the virtualization group id
     */
    public int getVirtualizationGroupId() {
        return virtualizationGroupId;
    }

    /**
     * Gets the guest system id.
     *
     * @return the guest system id
     */
    public Long getGuestId() {
        return guestId;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(virtualizationGroupId)
                .append(guestId)
                .toHashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object objIn) {
        if (!(objIn instanceof HostGuest)) {
            return false;
        }
        VirtualizationGroupMember other = (VirtualizationGroupMember) objIn;
        return new EqualsBuilder()
                .append(virtualizationGroupId, other.virtualizationGroupId)
                .append(guestId, other.guestId)
                .isEquals();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("virtualizationGroupId", virtualizationGroupId)
                .append("guestId", guestId)
                .toString();
    }
}
