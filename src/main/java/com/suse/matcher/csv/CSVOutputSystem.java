package com.suse.matcher.csv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<Long, String> products = new HashMap<>();

    /**
     * Instantiates a new CSV output system.
     *
     * @param idIn the id
     * @param nameIn the name
     * @param cpusIn the populated CPU socket count
     * @param productsIn the products id to name map
     */
    public CSVOutputSystem(Long idIn, String nameIn, Integer cpusIn, Map<Long, String> productsIn) {
        id = idIn;
        name = nameIn;
        cpus = cpusIn;
        products = productsIn;
    }

    /**
     * Gets the CSV rows.
     * @return the CSV rows
     */
    public List<List<String>> getCSVRows() {
        List<List<String>> resultSet = new ArrayList<>();

        List<String> row = new ArrayList<>();
        row.add(String.valueOf(id));
        row.add(name);
        row.add(String.valueOf(cpus));

        for (Map.Entry<Long, String> product : products.entrySet()) {
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
