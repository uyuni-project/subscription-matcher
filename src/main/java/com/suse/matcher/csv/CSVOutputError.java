package com.suse.matcher.csv;

import com.suse.matcher.json.JsonOutputError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * CSV representation of an Error detected during the match.
 */
public class CSVOutputError {
    /** header for CSV output */
    public static final String [] CSV_HEADER = {"Error Type","Error Key", "Error Value"};

    /** A label identifying the error type. */
    public String type;

    /** Arbitrary data connected to this error. */
    public Map<String, String> data = new TreeMap<>();

    /**
     * Constructor
     */
    public CSVOutputError(JsonOutputError error) {
        this.type = error.type;
        this.data = error.data;
    }

    /**
     * @return rows for the CSV output
     */
    public List<List<String>> getCSVRows() {
        List<List<String>> resultSet = new ArrayList<>();

        List<String> row = new ArrayList<>();
        row.add(type);

        for(Map.Entry<String, String> item : data.entrySet()) {
            row.add(item.getKey());
            row.add(item.getValue());
            resultSet.add(row);
            row = new ArrayList<>();
            row.add("");
        }
        return resultSet;
    }
}
