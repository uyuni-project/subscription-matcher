package com.suse.matcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.suse.matcher.model.Match;
import com.suse.matcher.model.Product;
import com.suse.matcher.model.Subscription;
import com.suse.matcher.model.System;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.List;

/**
 * Loads facts from JSON resources.
 */
public class Loader {

    /** Deserializer instance. */
    private Gson gson;

    /** Default constructor. */
    public Loader() {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
    }

    /**
     * Load a system list from a JSON file.
     *
     * @param reader the reader object
     * @return the list
     * @throws FileNotFoundException the file is not in the expected path
     * @throws JsonIOException in case the file cannot be read correctly
     * @throws JsonSyntaxException in case JSON does not have correct syntax
     */
    public List<System> loadSystems(Reader reader) throws FileNotFoundException, JsonIOException, JsonSyntaxException {
        return gson.fromJson(reader, new TypeToken<List<System>>() { }.getType());
    }

    /**
     * Load a subscription list from a JSON file.
     *
     * @param reader the reader object
     * @return the list
     * @throws FileNotFoundException the file is not in the expected path
     * @throws JsonIOException in case the file cannot be read correctly
     * @throws JsonSyntaxException in case JSON does not have correct syntax
     */
    public List<Subscription> loadSubscriptions(Reader reader) throws FileNotFoundException, JsonIOException, JsonSyntaxException {
        return gson.fromJson(reader, new TypeToken<List<Subscription>>() { }.getType());
    }

    /**
     * Load matches from a JSON file.
     *
     * @param reader the reader object
     * @return the list
     * @throws FileNotFoundException the file is not in the expected path
     * @throws JsonIOException in case the file cannot be read correctly
     * @throws JsonSyntaxException in case JSON does not have correct syntax
     */
    public List<Match> loadMatches(Reader reader) throws FileNotFoundException, JsonIOException, JsonSyntaxException {
        return gson.fromJson(reader, new TypeToken<List<Match>>() { }.getType());
    }

    /**
     * Load a product list from a JSON file.
     *
     * @param reader the reader object
     * @return the list
     * @throws FileNotFoundException the file is not in the expected path
     * @throws JsonIOException in case the file cannot be read correctly
     * @throws JsonSyntaxException in case JSON does not have correct syntax
     */
    public List<Product> loadProducts(Reader reader) throws FileNotFoundException, JsonIOException, JsonSyntaxException {
        return gson.fromJson(reader, new TypeToken<List<Product>>() { }.getType());
    }
}
