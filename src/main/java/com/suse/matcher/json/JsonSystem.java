package com.suse.matcher.json;

import java.util.LinkedHashSet;
import java.util.Set;

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
    public Set<Long> virtualSystemIds = new LinkedHashSet<>();

    /** Installed product IDs */
    public Set<Long> productIds = new LinkedHashSet<>();
}
