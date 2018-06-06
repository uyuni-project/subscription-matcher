/**
 * Copyright (c) 2018 SUSE LLC
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 * Red Hat trademarks are not licensed under GPLv2. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */

package com.suse.matcher.facts;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Representation of a confirmed match in a penalty group.
 *
 **/
public class ConfirmedMatchInPenaltyGroup {

    public long subscriptionId;
    public int penaltyGroupId;
    public long guestId;

    /**
     * Standard constructor.
     *
     * @param subscriptionIdIn - the subscription id
     * @param penaltyGroupIdIn - the penalty group id
     * @param guestIdIn - the guest id
     */
    public ConfirmedMatchInPenaltyGroup(Long subscriptionIdIn, int penaltyGroupIdIn, long guestIdIn) {
        this.subscriptionId = subscriptionIdIn;
        this.penaltyGroupId = penaltyGroupIdIn;
        this.guestId = guestIdIn;
    }

    /**
     * Gets the subscriptionId.
     *
     * @return subscriptionId
     */
    public Long getSubscriptionId() {
        return subscriptionId;
    }

    /**
     * Gets the penaltyGroupId.
     *
     * @return penaltyGroupId
     */
    public int getPenaltyGroupId() {
        return penaltyGroupId;
    }

    /**
     * Gets the guestId.
     *
     * @return guestId
     */
    public Long getGuestId() {
        return guestId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ConfirmedMatchInPenaltyGroup that = (ConfirmedMatchInPenaltyGroup) o;

        return new EqualsBuilder()
                .append(penaltyGroupId, that.penaltyGroupId)
                .append(guestId, that.guestId)
                .append(subscriptionId, that.subscriptionId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(subscriptionId)
                .append(penaltyGroupId)
                .append(guestId)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("subscriptionId", subscriptionId)
                .append("penaltyGroupId", penaltyGroupId)
                .append("guestId", guestId)
                .toString();
    }
}
