/**
 * Copyright (c) 2017 SUSE LLC
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

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Representation of matcher subscription id.
 */
public class SubscriptionId implements Comparable<SubscriptionId> {

    /** subscription id in SCC */
    private Long sccSubscriptionId;
    /** order item id in SCC */
    private Long sccOrderItemId;

    /**
     * Standard constructor.
     *
     * @param sccSubscriptionId - scc subscription id
     * @param sccOrderItemId - scc subscription
     */
    public SubscriptionId(Long sccSubscriptionId, Long sccOrderItemId) {
        this.sccSubscriptionId = sccSubscriptionId;
        this.sccOrderItemId = sccOrderItemId;
    }

    /**
     * Gets the sccSubscriptionId.
     *
     * @return sccSubscriptionId
     */
    public Long getSccSubscriptionId() {
        return sccSubscriptionId;
    }

    /**
     * Sets the sccSubscriptionId.
     *
     * @param sccSubscriptionIdIn - the sccSubscriptionId
     */
    public void setSccSubscriptionId(Long sccSubscriptionIdIn) {
        sccSubscriptionId = sccSubscriptionIdIn;
    }

    /**
     * Gets the sccOrderItemId.
     *
     * @return sccOrderItemId
     */
    public Long getSccOrderItemId() {
        return sccOrderItemId;
    }

    /**
     * Sets the sccOrderItemId.
     *
     * @param sccOrderItemIdIn - the sccOrderItemId
     */
    public void setSccOrderItemId(Long sccOrderItemIdIn) {
        sccOrderItemId = sccOrderItemIdIn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SubscriptionId that = (SubscriptionId) o;

        return new EqualsBuilder()
                .append(sccSubscriptionId, that.sccSubscriptionId)
                .append(sccOrderItemId, that.sccOrderItemId)
                .isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(sccSubscriptionId)
                .append(sccOrderItemId)
                .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "sccSubscriptionId:" + sccSubscriptionId +
                ",sccOrderItemId:" + sccOrderItemId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(SubscriptionId other) {
        return (new CompareToBuilder())
                .append(this.getSccSubscriptionId(), other.getSccSubscriptionId())
                .append(this.getSccOrderItemId(), other.getSccOrderItemId())
                .toComparison();
    }
}

