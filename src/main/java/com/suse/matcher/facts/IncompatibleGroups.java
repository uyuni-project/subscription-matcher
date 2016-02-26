/**
 * Copyright (c) 2016 SUSE LLC
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * aint with this software; if not, see
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
import org.apache.commons.lang3.builder.ToStringStyle;
import org.kie.api.definition.type.PropertyReactive;

/**
 * Represents two {@link PartialMatch} groups which are not compatible, ie,
 * that can't be simultaneously confirmed.
 */
@PropertyReactive
public class IncompatibleGroups {

    /** The group id1. */
    public int groupId1;

    /** The group id2. */
    public int groupId2;

    /**
     * Instantiates a new match collision.
     *
     * @param groupId1In the group id1 in
     * @param groupId2In the group id2 in
     */
    public IncompatibleGroups(int groupId1In, int groupId2In) {
        groupId1 = groupId1In;
        groupId2 = groupId2In;
    }

    /**
     * Gets the group id1.
     *
     * @return the group id1
     */
    public int getGroupId1() {
        return groupId1;
    }

    /**
     * Gets the group id2.
     *
     * @return the group id2
     */
    public int getGroupId2() {
        return groupId2;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(groupId1).append(groupId2).toHashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object objIn) {
        if (!(objIn instanceof IncompatibleGroups)) {
            return false;
        }
        IncompatibleGroups other = (IncompatibleGroups) objIn;
        return new EqualsBuilder().append(groupId1, other.groupId1).append(groupId2, other.groupId2).isEquals();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("groupId1", groupId1)
                .append("groupId2", groupId2).toString();
    }
}
