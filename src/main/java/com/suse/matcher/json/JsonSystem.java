package com.suse.matcher.json;

import java.util.LinkedList;
import java.util.List;

/**
 * JSON representation of a system.
 */
public class JsonSystem {

    /** The ID */
    public Long id;

    /** The profile name */
    public String name;

    /** The populated CPU socket count */
    public Integer cpus;

    /** True if this system is made of metal */
    public Boolean physical;

    /** Virtual machine IDs */
    public List<Long> virtualSystemIds = new LinkedList<Long>();

    /** Installed product IDs */
    public List<Long> productIds = new LinkedList<Long>();

    /**
     * Standard constructor.
     *
     * @param idIn the id
     * @param nameIn the name
     * @param cpusIn the populated CPU socket count
     * @param physicalIn true if this system is made of metal
     * @param virtualSystemIdsIn the virtual machine IDs
     * @param productIdsIn the installed product IDs
     */
    public JsonSystem(Long idIn, String nameIn, Integer cpusIn, Boolean physicalIn, List<Long> virtualSystemIdsIn,
            List<Long> productIdsIn) {
        id = idIn;
        name = nameIn;
        cpus = cpusIn;
        physical = physicalIn;
        virtualSystemIds = virtualSystemIdsIn;
        productIds = productIdsIn;
    }
}
