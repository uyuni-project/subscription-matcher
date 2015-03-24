package com.suse.matcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.suse.matcher.model.PinnedMatch;
import com.suse.matcher.model.Product;
import com.suse.matcher.model.Subscription;
import com.suse.matcher.model.System;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Loads facts from JSON resources.
 */
public class Loader {

    /** The system file expected content type. */
    private static final Type SYSTEM_FILE_CONTENT_TYPE = new TypeToken<List<System>>() { }.getType();

    /** The subscription file expected content type. */
    private static final Type SUBSCRIPTION_FILE_CONTENT_TYPE = new TypeToken<List<Subscription>>() { }.getType();

    /** The preferences file expected content type. */
    private static final Type PINNED_MATCH_FILE_CONTENT_TYPE = new TypeToken<List<PinnedMatch>>() { }.getType();

    /** The product file expected content type. */
    private static final Type PRODUCT_FILE_CONTENT_TYPE = new TypeToken<List<Product>>() { }.getType();

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
        return gson.fromJson(reader, SYSTEM_FILE_CONTENT_TYPE);
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
        return gson.fromJson(reader, SUBSCRIPTION_FILE_CONTENT_TYPE);
    }

    /**
     * Load pinned matches from a JSON file.
     *
     * @param reader the reader object
     * @return the list
     * @throws FileNotFoundException the file is not in the expected path
     * @throws JsonIOException in case the file cannot be read correctly
     * @throws JsonSyntaxException in case JSON does not have correct syntax
     */
    public List<PinnedMatch> loadPinnedMatches(Reader reader) throws FileNotFoundException, JsonIOException, JsonSyntaxException {
        return gson.fromJson(reader, PINNED_MATCH_FILE_CONTENT_TYPE);
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
        return gson.fromJson(reader, PRODUCT_FILE_CONTENT_TYPE);
    }
}
