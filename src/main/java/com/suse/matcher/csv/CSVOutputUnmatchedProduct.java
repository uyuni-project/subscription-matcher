package com.suse.matcher.csv;

import com.suse.matcher.facts.System;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A unmatched product as represented in a CSV output file.
 */
public class CSVOutputUnmatchedProduct {

    /**  Header for the CSV output. */
    public static final String[] CSV_HEADER = { "Unmatched Product Name", "System Name","System ID", "CPUs"};

    /** The product name. */
    private String productName;

    /** Unmatched unmatchedSystems corresponding to product */
    private List<System> unmatchedSystems;

    /**
     * Standard constructor.
     *
     * @param  productNameIn - product name
     * @param systemsIn - unmatched unmatchedSystems
     */
    public CSVOutputUnmatchedProduct(String productNameIn, List<System> systemsIn) {
        productName = productNameIn;
        unmatchedSystems = systemsIn;
    }

    /**
     * Gets the unmatchedSystems.
     *
     * @return unmatchedSystems
     */
    public List<System> getUnmatchedSystems() {
        return unmatchedSystems;
    }

    /**
     * Gets the CSV rows.
     * @return the CSV rows
     */
    public List<List<String>> getCSVRows() {
        List<List<String>> resultSet = new LinkedList<>();

        List<String> row = new LinkedList<>();
        row.add(productName);

        for (System system : unmatchedSystems) {
            row.add(system.name);
            row.add(String.valueOf(system.id));
            row.add(String.valueOf(system.cpus));
            resultSet.add(row);
            row = new ArrayList<>();
            row.add("");
        }
        return resultSet;
    }
}
