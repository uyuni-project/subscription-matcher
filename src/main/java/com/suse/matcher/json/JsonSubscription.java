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

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * JSON representation of a subscription.
 * todo remove the unneeded data from the test?
 * Created by franky on 11.08.17.
 */
public class JsonSubscription {

    /** The id. */
    private Long id;

    /** The part numbers. */
    private Set<String> partNumbers;

    /** The friendly name. */
    private String name;

    /** The number of available subscriptions. */
    private Integer systemLimit;

    /** Start date. */
    private Date startsAt;

    /** End date. */
    private Date expiresAt;

    /** SCC username. */
    private String sccUsername;

    /** Provided product ids. */
    private Set<Long> productIds = new LinkedHashSet<>();

    /** Order items **/
    private Set<JsonOrderItem> orderItems = new LinkedHashSet<>();

    /**
     * Standard constructor.
     *
     * @param idIn - the Id
     * @param partNumbersIn - part numbers
     * @param nameIn - subscription name
     * @param systemLimitIn - system limit
     * @param startsAtIn - start of the subscription validity
     * @param expiresAtIn - end of the subscription validity
     * @param sccUsernameIn - scc username
     * @param productIdsIn - product ids
     * @param orderItemsIn - order items
     */
    public JsonSubscription(Long idIn, Set<String> partNumbersIn, String nameIn,
            Integer systemLimitIn, Date startsAtIn, Date expiresAtIn, String sccUsernameIn,
            Set<Long> productIdsIn, Set<JsonOrderItem> orderItemsIn) {
        id = idIn;
        partNumbers = partNumbersIn;
        name = nameIn;
        systemLimit = systemLimitIn;
        startsAt = startsAtIn;
        expiresAt = expiresAtIn;
        sccUsername = sccUsernameIn;
        productIds = productIdsIn;
        orderItems = orderItemsIn;
    }

    /**
     * Gets the id.
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param idIn - the id
     */
    public void setId(Long idIn) {
        id = idIn;
    }

    /**
     * Gets the partNumbers.
     *
     * @return partNumbers
     */
    public Set<String> getPartNumbers() {
        return partNumbers;
    }

    /**
     * Sets the partNumbers.
     *
     * @param partNumbersIn - the partNumbers
     */
    public void setPartNumbers(Set<String> partNumbersIn) {
        partNumbers = partNumbersIn;
    }

    /**
     * Gets the name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param nameIn - the name
     */
    public void setName(String nameIn) {
        name = nameIn;
    }

    /**
     * Gets the systemLimit.
     *
     * @return systemLimit
     */
    public Integer getSystemLimit() {
        return systemLimit;
    }

    /**
     * Sets the systemLimit.
     *
     * @param systemLimitIn - the systemLimit
     */
    public void setSystemLimit(Integer systemLimitIn) {
        systemLimit = systemLimitIn;
    }

    /**
     * Gets the startsAt.
     *
     * @return startsAt
     */
    public Date getStartsAt() {
        return startsAt;
    }

    /**
     * Sets the startsAt.
     *
     * @param startsAtIn - the startsAt
     */
    public void setStartsAt(Date startsAtIn) {
        startsAt = startsAtIn;
    }

    /**
     * Gets the expiresAt.
     *
     * @return expiresAt
     */
    public Date getExpiresAt() {
        return expiresAt;
    }

    /**
     * Sets the expiresAt.
     *
     * @param expiresAtIn - the expiresAt
     */
    public void setExpiresAt(Date expiresAtIn) {
        expiresAt = expiresAtIn;
    }

    /**
     * Gets the sccUsername.
     *
     * @return sccUsername
     */
    public String getSccUsername() {
        return sccUsername;
    }

    /**
     * Sets the sccUsername.
     *
     * @param sccUsernameIn - the sccUsername
     */
    public void setSccUsername(String sccUsernameIn) {
        sccUsername = sccUsernameIn;
    }

    /**
     * Gets the productIds.
     *
     * @return productIds
     */
    public Set<Long> getProductIds() {
        return productIds;
    }

    /**
     * Sets the productIds.
     *
     * @param productIdsIn - the productIds
     */
    public void setProductIds(Set<Long> productIdsIn) {
        productIds = productIdsIn;
    }

    /**
     * Gets the orderItems.
     *
     * @return orderItems
     */
    public Set<JsonOrderItem> getOrderItems() {
        return orderItems;
    }

    /**
     * Sets the orderItems.
     *
     * @param orderItemsIn - the orderItems
     */
    public void setOrderItems(Set<JsonOrderItem> orderItemsIn) {
        orderItems = orderItemsIn;
    }
}
