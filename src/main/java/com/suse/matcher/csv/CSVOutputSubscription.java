package com.suse.matcher.csv;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * A subscription as represented in a CSV output file.
 */
public class CSVOutputSubscription {

    /** Header for the CSV output. */
    public static final String[] CSV_HEADER = { "Part Number", "Description",
            "Policy", "Total Quantity", "Matched Quantity", "Start Date", "End Date"};

    /** The part number. */
    private String partNumber;

    /** The subscription name. */
    private String name;

    /** The subscription policy. */
    private String policy;

    /** The quantity. */
    private Integer quantity;

    /** Number of subscriptions matched. */
    private int matched;

    /** The start date. */
    private Date startDate;

    /** The end date. */
    private Date endDate;

    /**
     * Instantiates a new CSV output subscription.
     *
     * @param partNumberIn the part number
     * @param nameIn the name
     * @param policyIn the policy
     * @param quantityIn the quantity
     * @param startDateIn the start date
     * @param endDateIn the end date
     */
    public CSVOutputSubscription(String partNumberIn, String nameIn, String policyIn,
            Integer quantityIn, Date startDateIn, Date endDateIn) {
        partNumber = partNumberIn;
        name = nameIn;
        policy = policyIn;
        quantity = quantityIn;
        startDate = startDateIn;
        endDate = endDateIn;

        matched = 0;
    }

    /**
     * Sets the count of matched subscriptions of this type.
     * @param matchedIn the count
     */
    public void setMatched(int matchedIn) {
        this.matched = matchedIn;
    }

    /**
     * Gets the CSV row.
     * @return the CSV row
     */
    public List<String> getCSVRow() {
        List<String> row = new ArrayList<>();
        row.add(partNumber);
        row.add(name);
        row.add(policy);
        row.add(String.valueOf(quantity));
        row.add(String.valueOf(matched));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        if (startDate != null) {
            row.add(df.format(startDate));
        }
        if (endDate != null) {
            row.add(df.format(endDate));
        }
        return row;
    }
}
