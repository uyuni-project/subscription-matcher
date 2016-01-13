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
}
