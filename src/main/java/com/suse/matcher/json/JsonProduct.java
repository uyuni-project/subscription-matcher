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
     * Standard constructor.
     *
     * @param idIn the id
     * @param nameIn the name
     */
    public JsonProduct(Long idIn, String nameIn) {
        id = idIn;
        name = nameIn;
    }
}
