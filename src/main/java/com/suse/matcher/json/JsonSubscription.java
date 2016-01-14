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
 */
public class JsonSubscription {

    /** The id. */
    private Long id;

    /** The part number. */
    private String partNumber;

    /** The friendly name. */
    private String name;

    /** The number of available subscriptions. */
    private Integer quantity;

    /** Start date. */
    private Date startDate;

    /** End date. */
    private Date endDate;

    /** SCC username. */
    private String sccUsername;

    /** Provided product ids. */
    private Set<Long> productIds = new LinkedHashSet<>();

    /**
     * Standard constructor.
     *
     * @param idIn the id
     * @param partNumberIn the part number
     * @param nameIn the name
     * @param quantityIn the quantity
     * @param startDateIn the start date
     * @param endDateIn the end date
     * @param sccUsernameIn the SCC username
     * @param productIdsIn the product ids
     */
    public JsonSubscription(Long idIn, String partNumberIn, String nameIn,
            Integer quantityIn, Date startDateIn, Date endDateIn,
            String sccUsernameIn, Set<Long> productIdsIn) {
        id = idIn;
        partNumber = partNumberIn;
        name = nameIn;
        quantity = quantityIn;
        startDate = startDateIn;
        endDate = endDateIn;
        sccUsername = sccUsernameIn;
        productIds = productIdsIn;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param idIn the new id
     */
    public void setId(Long idIn) {
        id = idIn;
    }

    /**
     * Gets the part number.
     *
     * @return the part number
     */
    public String getPartNumber() {
        return partNumber;
    }

    /**
     * Sets the part number.
     *
     * @param partNumberIn the new part number
     */
    public void setPartNumber(String partNumberIn) {
        partNumber = partNumberIn;
    }

    /**
     * Gets the friendly name.
     *
     * @return the friendly name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the friendly name.
     *
     * @param nameIn the new friendly name
     */
    public void setName(String nameIn) {
        name = nameIn;
    }

    /**
     * Gets the number of available subscriptions.
     *
     * @return the number of available subscriptions
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets the number of available subscriptions.
     *
     * @param quantityIn the new number of available subscriptions
     */
    public void setQuantity(Integer quantityIn) {
        quantity = quantityIn;
    }

    /**
     * Gets the start date.
     *
     * @return the start date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date.
     *
     * @param startDateIn the new start date
     */
    public void setStartDate(Date startDateIn) {
        startDate = startDateIn;
    }

    /**
     * Gets the end date.
     *
     * @return the end date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date.
     *
     * @param endDateIn the new end date
     */
    public void setEndDate(Date endDateIn) {
        endDate = endDateIn;
    }

    /**
     * Gets the SCC username.
     *
     * @return the SCC username
     */
    public String getSccUsername() {
        return sccUsername;
    }

    /**
     * Sets the SCC username.
     *
     * @param sccUsernameIn the new SCC username
     */
    public void setSccUsername(String sccUsernameIn) {
        sccUsername = sccUsernameIn;
    }

    /**
     * Gets the provided product ids.
     *
     * @return the provided product ids
     */
    public Set<Long> getProductIds() {
        return productIds;
    }

    /**
     * Sets the provided product ids.
     *
     * @param productIdsIn the new provided product ids
     */
    public void setProductIds(Set<Long> productIdsIn) {
        productIds = productIdsIn;
    }
}
