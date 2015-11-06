package com.suse.matcher.facts;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.kie.api.definition.type.PropertyReactive;

/**
 * Represents a virtual host-to-guest relationship.
 */
@PropertyReactive
public class HostGuest {

    /** The host system id. */
    public Long hostId;

    /** The guest system id. */
    public Long guestId;

    /**
     * Instantiates a new host-to-guest relationship object.
     *
     * @param hostIdIn the host id
     * @param guestIdIn the guest id
     */
    public HostGuest(Long hostIdIn, Long guestIdIn) {
        hostId = hostIdIn;
        guestId = guestIdIn;
    }

    /**
     * Gets the host id.
     *
     * @return the host id
     */
    public Long getHostId() {
        return hostId;
    }

    /**
     * Gets the guest id.
     *
     * @return the guest id
     */
    public Long getGuestId() {
        return guestId;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(hostId)
            .append(guestId)
            .toHashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object objIn) {
        if (!(objIn instanceof HostGuest)) {
            return false;
        }
        HostGuest other = (HostGuest) objIn;
        return new EqualsBuilder()
            .append(hostId, other.hostId)
            .append(guestId, other.guestId)
            .isEquals();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("hostId", hostId)
            .append("guestId", guestId)
            .toString();
    }
}
