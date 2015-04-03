package com.suse.matcher;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.suse.matcher.json.JsonMatch;
import com.suse.matcher.json.JsonOutput;
import com.suse.matcher.json.JsonProduct;
import com.suse.matcher.json.JsonSubscription;
import com.suse.matcher.json.JsonSystem;

import java.io.Reader;
import java.util.List;

/**
 * Reads and writes JSON resources.
 */
public class JsonConverter {

    /** Deserializer instance. */
    private Gson gson;

    /** Default constructor. */
    public JsonConverter() {
        gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .create();
    }

    /**
     * Load a system list from a JSON file.
     *
     * @param reader the reader object
     * @return the list
     * @throws JsonIOException in case the file cannot be read correctly
     * @throws JsonSyntaxException in case JSON does not have correct syntax
     */
    public List<JsonSystem> loadSystems(Reader reader) throws JsonIOException, JsonSyntaxException {
        return gson.fromJson(reader, new TypeToken<List<JsonSystem>>() { }.getType());
    }

    /**
     * Load a subscription list from a JSON file.
     *
     * @param reader the reader object
     * @return the list
     * @throws JsonIOException in case the file cannot be read correctly
     * @throws JsonSyntaxException in case JSON does not have correct syntax
     */
    public List<JsonSubscription> loadSubscriptions(Reader reader) throws JsonIOException, JsonSyntaxException {
        return gson.fromJson(reader, new TypeToken<List<JsonSubscription>>() { }.getType());
    }

    /**
     * Load matches from a JSON file.
     *
     * @param reader the reader object
     * @return the list
     * @throws JsonIOException in case the file cannot be read correctly
     * @throws JsonSyntaxException in case JSON does not have correct syntax
     */
    public List<JsonMatch> loadMatches(Reader reader) throws JsonIOException, JsonSyntaxException {
        return gson.fromJson(reader, new TypeToken<List<JsonMatch>>() { }.getType());
    }

    /**
     * Load a product list from a JSON file.
     *
     * @param reader the reader object
     * @return the list
     * @throws JsonIOException in case the file cannot be read correctly
     * @throws JsonSyntaxException in case JSON does not have correct syntax
     */
    public List<JsonProduct> loadProducts(Reader reader) throws JsonIOException, JsonSyntaxException {
        return gson.fromJson(reader, new TypeToken<List<JsonProduct>>() { }.getType());
    }

    /**
     * Load a matcher's output from a JSON file.
     *
     * @param reader the reader object
     * @return the output
     * @throws JsonIOException in case the file cannot be read correctly
     * @throws JsonSyntaxException in case JSON does not have correct syntax
     */
    public JsonOutput loadOutput(Reader reader) {
        return gson.fromJson(reader, new TypeToken<JsonOutput>() { }.getType());
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
