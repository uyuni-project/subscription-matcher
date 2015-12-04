package com.suse.matcher.json;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * JSON representation of a subscription.
 */
public class JsonSubscription {

    /** The id. */
    public Long id;

    /** The part number. */
    public String partNumber;

    /** The friendly name. */
    public String name;

    /** The number of available subscriptions. */
    public Integer quantity;

    /** Start Date. */
    public Date startDate = new Date(Long.MIN_VALUE);

    /** End Date. */
    public Date endDate = new Date(Long.MAX_VALUE);

    /** SCC Username. */
    public String sccUsername;

    /** Provided product IDs */
    public Set<Long> productIds = new HashSet<Long>();
}
