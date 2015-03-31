package com.suse.matcher.json;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * JSON representation of a system.
 */
public class JsonSystem {

    /** The ID */
    public Long id;

    /** The populated CPU socket count */
    public Integer cpus;

    /** Virtual machine IDs */
    @SerializedName("virtual_system_ids")
    public List<Long> virtualSystemIds = new LinkedList<Long>();

    /** Installed product IDs */
    @SerializedName("product_ids")
    public Set<Long> productIds = new HashSet<Long>();
}
