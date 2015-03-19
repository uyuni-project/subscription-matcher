package com.suse.matcher;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.suse.matcher.model.Subscription;
import com.suse.matcher.model.System;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * The Class FileLoader.
 */
public class FileLoader {

    /** The system file name. */
    private static final String SYSTEM_FILE = "systems.json";

    /** The system file expected content type. */
    private static final Type SYSTEM_FILE_CONTENT_TYPE = new TypeToken<List<System>>() { }.getType();

    /** The subscription file name. */
    private static final String SUBSCRIPTION_FILE = "subscriptions.json";

    /** The subscription file expected content type. */
    private static final Type SUBSCRIPTION_FILE_CONTENT_TYPE = new TypeToken<List<Subscription>>() { }.getType();

    /**
     * Load a system list from a JSON file.
     *
     * @return the list
     * @throws FileNotFoundException the file is not in the expected path
     * @throws JsonIOException in case the file cannot be read correctly
     * @throws JsonSyntaxException in case JSON does not have correct syntax
     */
    public List<System> loadSystems() throws FileNotFoundException, JsonIOException, JsonSyntaxException {
        return new Gson().fromJson(new FileReader(SYSTEM_FILE), SYSTEM_FILE_CONTENT_TYPE);
    }

    /**
     * Load a subscription list from a JSON file.
     *
     * @return the list
     * @throws FileNotFoundException the file is not in the expected path
     * @throws JsonIOException in case the file cannot be read correctly
     * @throws JsonSyntaxException in case JSON does not have correct syntax
     */
    public List<Subscription> loadSubscriptions() throws FileNotFoundException, JsonIOException, JsonSyntaxException {
        return new Gson().fromJson(new FileReader(SUBSCRIPTION_FILE), SUBSCRIPTION_FILE_CONTENT_TYPE);
    }
}
