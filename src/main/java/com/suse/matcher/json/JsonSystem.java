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

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * JSON representation of a system.
 */
public class JsonSystem {

    /** The id. */
    private Long id;

    /** The profile name. */
    private String name;

    /** The populated CPU socket count. */
    private Integer cpus;

    /** True if this system is made of metal. */
    private Boolean physical;

    /** True if this system is a virtual host. */
    private Boolean virtualHost;

    /** Virtual machine ids. */
    private Set<Long> virtualSystemIds = new LinkedHashSet<>();

    /** Installed product ids. */
    private Set<Long> productIds = new LinkedHashSet<>();

    /**
     * Standard constructor.
     *
     * @param idIn the id
     * @param nameIn the name
     * @param cpusIn the cpus
     * @param physicalIn the physical
     * @param virtualHostIn true if this is a virtual host
     * @param virtualSystemIdsIn the virtual system ids
     * @param productIdsIn the product ids
     */
    public JsonSystem(Long idIn, String nameIn, Integer cpusIn, Boolean physicalIn,
            Boolean virtualHostIn, Set<Long> virtualSystemIdsIn, Set<Long> productIdsIn) {
        id = idIn;
        name = nameIn;
        cpus = cpusIn;
        physical = physicalIn;
        virtualHost = virtualHostIn;
        virtualSystemIds = virtualSystemIdsIn;
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
     * Gets the profile name.
     *
     * @return the profile name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the profile name.
     *
     * @param nameIn the new profile name
     */
    public void setName(String nameIn) {
        name = nameIn;
    }

    /**
     * Gets the populated CPU socket count.
     *
     * @return the populated CPU socket count
     */
    public Integer getCpus() {
        return cpus;
    }

    /**
     * Sets the populated CPU socket count.
     *
     * @param cpusIn the new populated CPU socket count
     */
    public void setCpus(Integer cpusIn) {
        cpus = cpusIn;
    }

    /**
     * Returns true if this system is made of metal.
     *
     * @return true if this system is made of metal
     */
    public Boolean getPhysical() {
        return physical;
    }

    /**
     * Sets the physicality of this system.
     *
     * @param physicalIn true if this system is made of metal
     */
    public void setPhysical(Boolean physicalIn) {
        physical = physicalIn;
    }

    /**
     * Returns true if this system is a virtual host.
     *
     * @return the true if this system is a virtual host
     */
    public Boolean getVirtualHost() {
        return virtualHost;
    }

    /**
     * Set to true if this system is a virtual host.
     *
     * @param virtualHostIn true if this system is a virtual host
     */
    public void setVirtualHost(Boolean virtualHostIn) {
        virtualHost = virtualHostIn;
    }

    /**
     * Gets the virtual machine ids.
     *
     * @return the virtual machine ids
     */
    public Set<Long> getVirtualSystemIds() {
        return virtualSystemIds;
    }

    /**
     * Sets the virtual machine ids.
     *
     * @param virtualSystemIdsIn the new virtual machine ids
     */
    public void setVirtualSystemIds(Set<Long> virtualSystemIdsIn) {
        virtualSystemIds = virtualSystemIdsIn;
    }

    /**
     * Gets the installed product ids.
     *
     * @return the installed product ids
     */
    public Set<Long> getProductIds() {
        return productIds;
    }

    /**
     * Sets the installed product ids.
     *
     * @param productIdsIn the new installed product ids
     */
    public void setProductIds(Set<Long> productIdsIn) {
        productIds = productIdsIn;
    }
}
