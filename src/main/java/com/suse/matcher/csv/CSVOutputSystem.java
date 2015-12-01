package com.suse.matcher.csv;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A system as represented in a CSV output file.
 */
public class CSVOutputSystem {

    /**  Header for the CSV output. */
    public static final String[] CSV_HEADER = {"System ID", "System Name", "CPUs", "Unmatched Product Names"};

    /** The system id. */
    private Long id;

    /** The profile name. */
    private String name;

    /** The populated CPU socket count. */
    private Integer cpus;

    /** Installed product IDs with their names. */
    private List<String> productNames = new LinkedList<>();

    /**
     * Instantiates a new CSV output system.
     *
     * @param idIn the id
     * @param nameIn the name
     * @param cpusIn the populated CPU socket count
     * @param productNamesIn names of products to be displayed
     */
    public CSVOutputSystem(Long idIn, String nameIn, Integer cpusIn, List<String> productNamesIn) {
        id = idIn;
        name = nameIn;
        cpus = cpusIn;
        productNames = productNamesIn;
    }

    /**
     * Gets the CSV rows.
     * @return the CSV rows
     */
    public List<List<String>> getCSVRows() {
        List<List<String>> resultSet = new LinkedList<>();

        List<String> row = new LinkedList<>();
        row.add(String.valueOf(id));
        row.add(name);
        row.add(String.valueOf(cpus));

        for (String productName : productNames) {
            row.add(productName);
            resultSet.add(row);
            row = new ArrayList<>();
            row.add("");
            row.add("");
            row.add("");
        }
        return resultSet;
    }
}
