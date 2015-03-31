package com.suse.matcher.json;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * JSON representation of a subscription.
 */
public class JsonSubscription {

    /** The id. */
    public Long id;

    /** The part number. */
    @SerializedName("part_number")
    public String partNumber;

    /** The count. */
    @SerializedName("system_limit")
    public Integer systemLimit;

    /** Start Date. */
    @SerializedName("starts_at")
    public Date startsAt = new Date(Long.MIN_VALUE);

    /** End Date. */
    @SerializedName("expires_at")
    public Date expiresAt = new Date(Long.MAX_VALUE);

    /** SCC Organization Id. */
    @SerializedName("scc_org_id")
    public String sccOrgId;
}
