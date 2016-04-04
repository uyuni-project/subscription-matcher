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
import java.util.List;

/**
 * JSON representation of the matcher's input.
 */
public class JsonInput {

    /** Date and time of the match (as it influences subscriptions). */
    private Date timestamp;

    /** The systems */
    private List<JsonSystem> systems;

    /** The products */
    private List<JsonProduct> products;

    /** The subscriptions */
    private List<JsonSubscription> subscriptions;

    /** The pinned matches */
    private List<JsonMatch> pinnedMatches;

    /**
     * Standard constructor.
     *
     * @param systemsIn the systems
     * @param productsIn the products
     * @param subscriptionsIn the subscriptions
     * @param pinnedMatchesIn the pinned matches
     */
    public JsonInput(List<JsonSystem> systemsIn, List<JsonProduct> productsIn,
            List<JsonSubscription> subscriptionsIn, List<JsonMatch> pinnedMatchesIn) {
        systems = systemsIn;
        products = productsIn;
        subscriptions = subscriptionsIn;
        pinnedMatches = pinnedMatchesIn;
    }

    /**
     * Gets the systems.
     *
     * @return the systems
     */
    public List<JsonSystem> getSystems() {
        return systems;
    }

    /**
     * Sets the systems.
     *
     * @param systemsIn the new systems
     */
    public void setSystems(List<JsonSystem> systemsIn) {
        systems = systemsIn;
    }

    /**
     * Gets the products.
     *
     * @return the products
     */
    public List<JsonProduct> getProducts() {
        return products;
    }

    /**
     * Sets the products.
     *
     * @param productsIn the new products
     */
    public void setProducts(List<JsonProduct> productsIn) {
        products = productsIn;
    }

    /**
     * Gets the subscriptions.
     *
     * @return the subscriptions
     */
    public List<JsonSubscription> getSubscriptions() {
        return subscriptions;
    }

    /**
     * Sets the subscriptions.
     *
     * @param subscriptionsIn the new subscriptions
     */
    public void setSubscriptions(List<JsonSubscription> subscriptionsIn) {
        subscriptions = subscriptionsIn;
    }

    /**
     * Gets the pinned matches.
     *
     * @return the pinned matches
     */
    public List<JsonMatch> getPinnedMatches() {
        return pinnedMatches;
    }

    /**
     * Sets the pinned matches.
     *
     * @param pinnedMatchesIn the new pinned matches
     */
    public void setPinnedMatches(List<JsonMatch> pinnedMatchesIn) {
        pinnedMatches = pinnedMatchesIn;
    }

    /**
     * Gets the date and time of the match.
     *
     * @return the date and time of the match
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the date and time of the match.
     *
     * @param timestampIn the new date and time of the match
     */
    public void setTimestamp(Date timestampIn) {
        timestamp = timestampIn;
    }
}
