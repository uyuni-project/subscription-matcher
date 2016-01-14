package com.suse.matcher.json;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JSON representation of a user message generated during the match (error, warning, etc.).
 */
public class JsonMessage {

    /** A label identifying the message type. */
    public String type;

    /** Arbitrary data connected to this message. */
    public Map<String, String> data = new LinkedHashMap<>();

    /**
     * Instantiates a new json output message.
     *
     * @param typeIn the type
     * @param dataIn the data
     */
    public JsonMessage(String typeIn, Map<String, String> dataIn) {
        type = typeIn;
        data = dataIn;
    }
}
