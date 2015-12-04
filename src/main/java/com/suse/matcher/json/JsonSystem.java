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

    /** Virtual machine IDs */
    public List<Long> virtualSystemIds = new LinkedList<Long>();

    /** Installed product IDs with its names */
    public List<JsonProduct> products = new LinkedList<JsonProduct>();
}
