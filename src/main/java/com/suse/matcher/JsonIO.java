package com.suse.matcher;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.suse.matcher.json.JsonInput;
import com.suse.matcher.json.JsonOutput;

/**
 * Serializes and deserializes objects from and to JSON.
 */
public class JsonIO {

    /** Deserializer instance. */
    private Gson gson;

    /** Default constructor. */
    public JsonIO() {
        gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .enableComplexMapKeySerialization()
            .create();
    }

    /**
     * Load input JSON from a string.
     *
     * @param string the JSON string
     * @return the input data
     * @throws JsonSyntaxException in case JSON does not have correct syntax
     */
    public JsonInput loadInput(String string) throws JsonSyntaxException {
        return gson.fromJson(string, new TypeToken<JsonInput>() { }.getType());
    }

    /**
     * Load output JSON from a string.
     *
     * @param string the JSON string
     * @return the output
     * @throws JsonIOException in case the file cannot be read correctly
     * @throws JsonSyntaxException in case JSON does not have correct syntax
     */
    public JsonOutput loadOutput(String string) {
        return gson.fromJson(string, new TypeToken<JsonOutput>() { }.getType());
    }

    /**
     * Converts an object to a JSON string.
     * @param o an object
     * @return the corresponding JSON string
     */
    public String toJson(Object o){
        return gson.toJson(o);
    }
}
