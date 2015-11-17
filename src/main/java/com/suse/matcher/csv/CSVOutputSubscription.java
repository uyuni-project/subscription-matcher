package com.suse.matcher.csv;

import com.suse.matcher.json.JsonSubscription;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CSVOutputSubscription {

    /** The id. */
    public Long id;

    /** The part number. */
    public String partNumber;

    /** The count. */
    public Integer systemLimit;

    /** The consumed. */
    public Integer consumed;

    /** Start Date. */
    public Date startsAt = new Date(Long.MIN_VALUE);

    /** End Date. */
    public Date expiresAt = new Date(Long.MAX_VALUE);

    /** SCC Organization Id. */
    public String sccOrgId;

    /** Provided product IDs */
    public Set<Long> productIds = new HashSet<Long>();

    public CSVOutputSubscription(JsonSubscription s) {
        this.id = s.id;
        this.partNumber = s.partNumber;
        this.systemLimit = s.systemLimit;
        this.consumed = 0;
        this.startsAt = s.startsAt;
        this.expiresAt = s.expiresAt;
        this.sccOrgId = s.sccOrgId;
        this.productIds = s.productIds;
    }

    public void consume(int count) {
        if (consumed != null) {
            consumed = consumed + count;
        }
        else {
            consumed = new Integer(count);
        }
    }
    
    public List<String> getCSVRow() {
        List<String> row = new ArrayList<>();
        row.add(String.valueOf(id));
        row.add(partNumber);
        row.add(String.valueOf(systemLimit));
        row.add(String.valueOf(consumed));
        Format df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        row.add(df.format(startsAt));
        row.add(df.format(expiresAt));
        return row;
    }
}
