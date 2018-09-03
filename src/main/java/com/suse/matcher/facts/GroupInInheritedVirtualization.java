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

import org.kie.api.definition.type.PropertyReactive;

/**
 * Marker class expressing that a group of matches was generated from Inherited virtualization
 * matches. This is to stop infinite re-activation.
 */
@PropertyReactive
public class GroupInInheritedVirtualization {

    private int groupId;

    /**
     * Standard constructor.
     *
     * @param groupIdIn - the group id
     */
    public GroupInInheritedVirtualization(int groupIdIn) {
        this.groupId = groupIdIn;
    }

    /**
     * Gets the groupId.
     *
     * @return groupId
     */
    public int getGroupId() {
        return groupId;
    }

    /**
     * Sets the groupId.
     *
     * @param groupIdIn - the groupId
     */
    public void setGroupId(int groupIdIn) {
        groupId = groupIdIn;
    }
}
