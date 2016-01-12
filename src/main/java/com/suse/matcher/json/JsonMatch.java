package com.suse.matcher.json;


/**
 * JSON representation of a match.
 */
public class JsonMatch {

    /** The system id. */
    public Long systemId;


    /** The subscription id. */
    public Long subscriptionId;

    /** The product id. */
    public Long productId;

    /** The number of subscription cents used in this match. */
    public Integer cents;

    /**
     * Pinned match constructor.
     *
     * @param systemIdIn the system id
     * @param subscriptionIdIn the subscription id
     */
    public JsonMatch(Long systemIdIn, Long subscriptionIdIn) {
        this(systemIdIn, subscriptionIdIn, null, null);
    }

    /**
     * Standard constructor.
     *
     * @param systemIdIn the system id
     * @param subscriptionIdIn the subscription id
     * @param productIdIn the product id
     * @param centsIn the number of subscription cents used in this match
     */
    public JsonMatch(Long systemIdIn, Long subscriptionIdIn, Long productIdIn, Integer centsIn) {
        systemId = systemIdIn;
        subscriptionId = subscriptionIdIn;
        productId = productIdIn;
        cents = centsIn;
    }
}
