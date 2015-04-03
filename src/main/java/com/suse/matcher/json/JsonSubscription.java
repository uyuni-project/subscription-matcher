package com.suse.matcher.json;

import java.util.Date;

/**
 * JSON representation of a subscription.
 */
public class JsonSubscription {

    /** The id. */
    public Long id;

    /** The part number. */
    public String partNumber;

    /** The count. */
    public Integer systemLimit;

    /** Start Date. */
    public Date startsAt = new Date(Long.MIN_VALUE);

    /** End Date. */
    public Date expiresAt = new Date(Long.MAX_VALUE);

    /** SCC Organization Id. */
    public String sccOrgId;
}
