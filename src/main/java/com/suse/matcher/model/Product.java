package com.suse.matcher.model;

import com.google.gson.annotations.SerializedName;

/**
 * A piece of software which can be installed to a {@link System} and requires a
 * {@link Subscription}.
 */
public class Product {

    /** The id. */
    public Integer id;

    /** A human-friendly name. */
    @SerializedName("friendly_name")
    public String friendlyName;
}
