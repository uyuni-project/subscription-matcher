package com.suse.matcher.json;

/**
 * JSON representation of a product.
 */
public class JsonProduct {

    /** The id. */
    public Long id;

    /** A friendly name. */
    public String name;

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
    public String getName() {
        return name;
    }
}
