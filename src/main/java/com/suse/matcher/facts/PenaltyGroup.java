package com.suse.matcher.facts;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents mapping of a virtual guest to a penalty group for 1-2 subscriptions.
 *
 * Penalty Group is a group of virtual guests for which a 1-2 subscription is splittable.
 */
public class PenaltyGroup {

    private int id;
    private Long guestId;

    /**
     * Standard constructor.
     * @param idIn - the id
     * @param guestIdIn - the guest id
     */
    public PenaltyGroup(int idIn, Long guestIdIn) {
        id = idIn;
        guestId = guestIdIn;
    }

    /**
     * Gets the id.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param idIn - the id
     */
    public void setId(int idIn) {
        id = idIn;
    }

    /**
     * Gets the guestId.
     *
     * @return guestId
     */
    public Long getGuestId() {
        return guestId;
    }

    /**
     * Sets the guestId.
     *
     * @param guestIdIn - the guestId
     */
    public void setGuestId(Long guestIdIn) {
        guestId = guestIdIn;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("guestId", guestId)
            .toString();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object objIn) {
        if (!(objIn instanceof PenaltyGroup)) {
            return false;
        }
        PenaltyGroup that = (PenaltyGroup) objIn;
        return new EqualsBuilder()
                .append(id, that.id)
                .append(guestId, that.guestId)
                .isEquals();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(guestId)
                .toHashCode();
    }
}
