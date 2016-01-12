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
    public Date startDate;

    /** End Date. */
    public Date endDate;

    /** SCC Username. */
    public String sccUsername;

    /** Provided product IDs */
    public Set<Long> productIds = new HashSet<Long>();

    /**
     * Standard constructor.
     *
     * @param idIn the id
     * @param partNumberIn the part number
     * @param nameIn the name
     * @param quantityIn the number of available subscriptions
     * @param startDateIn the start date
     * @param endDateIn the end date
     * @param sccUsernameIn the SCC Username
     * @param productIdsIn the provided product IDs
     */
    public JsonSubscription(Long idIn, String partNumberIn, String nameIn, Integer quantityIn, Date startDateIn,
            Date endDateIn, String sccUsernameIn, Set<Long> productIdsIn) {
        id = idIn;
        partNumber = partNumberIn;
        name = nameIn;
        quantity = quantityIn;
        startDate = startDateIn;
        endDate = endDateIn;
        sccUsername = sccUsernameIn;
        productIds = productIdsIn;
    }
}
