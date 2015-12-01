package com.suse.matcher.json;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    public Map<Long, String> products = new HashMap<>();
}
