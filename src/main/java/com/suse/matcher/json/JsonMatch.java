/**
 * Copyright (c) 2016 SUSE LLC
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *    * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *    * Neither the name of SUSE LLC nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.suse.matcher.json;

/**
 * JSON representation of a match.
 */
public class JsonMatch {

    /** The system id. */
    private Long systemId;

    /** The subscription id. */
    private Long subscriptionId;

    /** The product id. */
    private Long productId;

    /** The number of subscription cents used in this match. */
    private Integer cents;

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

    /**
     * Gets the system id.
     *
     * @return the system id
     */
    public Long getSystemId() {
        return systemId;
    }

    /**
     * Sets the system id.
     *
     * @param systemIdIn the new system id
     */
    public void setSystemId(Long systemIdIn) {
        systemId = systemIdIn;
    }

    /**
     * Gets the subscription id.
     *
     * @return the subscription id
     */
    public Long getSubscriptionId() {
        return subscriptionId;
    }

    /**
     * Sets the subscription id.
     *
     * @param subscriptionIdIn the new subscription id
     */
    public void setSubscriptionId(Long subscriptionIdIn) {
        subscriptionId = subscriptionIdIn;
    }

    /**
     * Gets the product id.
     *
     * @return the product id
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * Sets the product id.
     *
     * @param productIdIn the new product id
     */
    public void setProductId(Long productIdIn) {
        productId = productIdIn;
    }

    /**
     * Gets the number of subscription cents used in this match.
     *
     * @return the number of subscription cents used in this match
     */
    public Integer getCents() {
        return cents;
    }

    /**
     * Sets the number of subscription cents used in this match.
     *
     * @param centsIn the new number of subscription cents used in this match
     */
    public void setCents(Integer centsIn) {
        cents = centsIn;
    }

}
