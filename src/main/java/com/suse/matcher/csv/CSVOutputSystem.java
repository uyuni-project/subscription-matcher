package com.suse.matcher.csv;

import com.suse.matcher.json.JsonSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * CSV representation of a system
 */
public class CSVOutputSystem {
    /** header for CSV output */
    public static final String [] CSV_HEADER = {"System ID","System Name", "CPUs","Unmatched Product IDs", "Unmatched Product Names"};

    /** The ID */
    public Long id;

    /** The profile name */
    public String name;

    /** The populated CPU socket count */
    public Integer cpus;

    /** Virtual machine IDs */
    public List<Long> virtualSystemIds = new LinkedList<Long>();

    /** Installed product IDs with its names*/
    public Map<Long, String> products = new HashMap<>();

    public CSVOutputSystem(JsonSystem jsonSystem) {
        this.id = jsonSystem.id;
        this.name = jsonSystem.name;
        this.cpus = jsonSystem.cpus;
        this.virtualSystemIds = jsonSystem.virtualSystemIds;
        this.products = jsonSystem.products;
    }

    public List<List<String>> getCSVRows() {
        List<List<String>> resultSet = new ArrayList<>();

        List<String> row = new ArrayList<>();
        row.add(String.valueOf(id));
        row.add(name);
        row.add(String.valueOf(cpus));

        for(Map.Entry<Long, String> product : products.entrySet()) {
            row.add(String.valueOf(product.getKey()));
            row.add(product.getValue());
            resultSet.add(row);
            row = new ArrayList<>();
            row.add("");
            row.add("");
            row.add("");
        }
        return resultSet;
    }
}
