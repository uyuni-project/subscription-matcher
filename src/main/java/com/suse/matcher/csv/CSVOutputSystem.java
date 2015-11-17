package com.suse.matcher.csv;

import com.suse.matcher.json.JsonSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * CSV representation of a system
 */
public class CSVOutputSystem {
    /** The ID */
    public Long id;

    /** The populated CPU socket count */
    public Integer cpus;

    /** Virtual machine IDs */
    public List<Long> virtualSystemIds = new LinkedList<Long>();

    /** Installed product IDs */
    public Set<Long> productIds = new HashSet<Long>();

    public CSVOutputSystem(JsonSystem jsonSystem) {
        this.id = jsonSystem.id;
        this.cpus = jsonSystem.cpus;
        this.virtualSystemIds = jsonSystem.virtualSystemIds;
        this.productIds = jsonSystem.productIds;
    }

    public List<String> getCSVRow() {
        List<String> row = new ArrayList<>();
        row.add(String.valueOf(id));
        row.add(String.valueOf(cpus));
        row.add(String.join(" ", Arrays.toString(productIds.toArray())));
        return row;
    }
}
