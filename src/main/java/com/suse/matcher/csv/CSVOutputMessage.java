package com.suse.matcher.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A message from the matcher as represented in a CSV output file.
 */
public class CSVOutputMessage {

    /** Header for the CSV output. */
    public static final String[] CSV_HEADER = {"Message", "Additional data key", "Additional data value"};

    /** A label identifying the message type. */
    private String type;

    /** Arbitrary data connected to this message. */
    private Map<String, String> data = new TreeMap<>();

    /**
     * Instantiates a new CSV output message.
     *
     * @param typeIn the type
     * @param dataIn the data
     */
    public CSVOutputMessage(String typeIn, Map<String, String> dataIn) {
        type = typeIn;
        data = dataIn;
    }

    /**
     * Gets the CSV rows.
     *
     * @return rows for the CSV output
     */
    public List<List<String>> getCSVRows() {
        List<List<String>> resultSet = new ArrayList<>();

        List<String> row = new ArrayList<>();
        row.add(type);

        for (Map.Entry<String, String> item : data.entrySet()) {
            row.add(item.getKey());
            row.add(item.getValue());
            resultSet.add(row);
            row = new ArrayList<>();
            row.add("");
        }
        return resultSet;
    }
}
