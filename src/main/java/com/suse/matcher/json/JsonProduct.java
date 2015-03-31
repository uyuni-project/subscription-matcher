package com.suse.matcher.json;

import com.google.gson.annotations.SerializedName;

/**
 * JSON representation of a product.
 */
public class JsonProduct {

    /** The id. */
    public Long id;

    /** A human-friendly name. */
    @SerializedName("friendly_name")
    public String friendlyName;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the friendly name.
     *
     * @return the friendly name
     */
    public String getFriendlyName() {
        return friendlyName;
    }
}
