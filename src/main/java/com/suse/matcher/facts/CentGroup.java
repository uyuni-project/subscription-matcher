/**
 * Copyright (c) 2017 SUSE LLC
 * <p>
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 * <p>
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
 * Cent group - expresses the usage of subscription by a partial match(es)
 *
 * More partial matches can share the same cent group. If N partial matches have the same
 * cent group, only `cents` of the subscription are used (instead of N * `cents`).
 */
@PropertyReactive
public class CentGroup {

    /** The id **/
    public int id;

    /** The number of cents consumed by this cent group **/
    public int cents;

    public CentGroup(int id, int cents) {
        this.id = id;
        this.cents = cents;
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
     * Gets the cents.
     *
     * @return cents
     */
    public int getCents() {
        return cents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CentGroup centGroup = (CentGroup) o;

        return new EqualsBuilder()
                .append(id, centGroup.id)
                .append(cents, centGroup.cents)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(cents)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("cents", cents)
                .toString();
    }
}
