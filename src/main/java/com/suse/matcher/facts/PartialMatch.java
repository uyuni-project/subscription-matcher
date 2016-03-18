/**
 * Copyright (c) 2016 SUSE LLC
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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.kie.api.definition.type.PropertyReactive;

/**
 * A PartialMatch is a potential application of a {@link Subscription} to a {@link System}
 * and a {@link Product}.
 *
 * Such a fact is generated if the Subscription-System-Product triple is a legal one on its
 * own, that is, without taking any other PartialMatches into account.
 *
 * PartialMatches can be grouped together. PartialMatches in such groups must either be
 * be all matched or none of them can be matched.
 */
@PropertyReactive
public class PartialMatch implements Comparable<PartialMatch> {

    /** The system id. */
    public long systemId;

    /** The product id. */
    public long productId;

    /** The subscription id. */
    public long subscriptionId;

    /** The number of subscription cents used in this match. */
    public int cents;

    /** The group id. */
    public int groupId;

    /**
     * Standard constructor.
     *
     * @param systemIdIn a system id
     * @param productIdIn an id of a product
     * @param subscriptionIdIn an id of subscription assigned to the system
     * @param centsIn the number of subscription cents used in this match
     * @param groupIdIn the group id
     */
    public PartialMatch(long systemIdIn, long productIdIn, long subscriptionIdIn, int centsIn, int groupIdIn) {
        systemId = systemIdIn;
        productId = productIdIn;
        subscriptionId = subscriptionIdIn;
        cents = centsIn;
        groupId = groupIdIn;
    }

    /**
     * Gets the system id.
     *
     * @return the system id
     */
    public long getSystemId() {
        return systemId;
    }

    /**
     * Gets the product id.
     *
     * @return the product id
     */
    public long getProductId() {
        return productId;
    }

    /**
     * Gets the subscription id.
     *
     * @return the subscription id
     */
    public long getSubscriptionId() {
        return subscriptionId;
    }

    /**
     * Gets the number of subscription cents used in this match.
     *
     * @return the cents
     */
    public int getCents() {
        return cents;
    }

    /**
     * Gets the group id.
     *
     * @return the group id
     */
    public int getGroupId() {
        return groupId;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(systemId)
            .append(productId)
            .append(subscriptionId)
            .append(groupId)
            .append(cents)
            .toHashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object objIn) {
        if (!(objIn instanceof PartialMatch)) {
            return false;
        }
        PartialMatch other = (PartialMatch) objIn;
        return new EqualsBuilder()
            .append(systemId, other.systemId)
            .append(productId, other.productId)
            .append(subscriptionId, other.subscriptionId)
            .append(groupId, other.groupId)
            .append(cents, other.cents)
            .isEquals();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("systemId", systemId)
            .append("productId", productId)
            .append("subscriptionId", subscriptionId)
            .append("cents", cents)
            .append("groupId", groupId)
            .toString();
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(PartialMatch other) {
        return new CompareToBuilder()
            .append(systemId, other.systemId)
            .append(productId, other.productId)
            .append(subscriptionId, other.subscriptionId)
            .append(cents, other.cents)
            .append(groupId, other.groupId)
            .toComparison();
    }
}
