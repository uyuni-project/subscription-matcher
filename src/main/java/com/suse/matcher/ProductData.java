package com.suse.matcher;

import com.suse.matcher.model.Product;
import com.suse.matcher.model.System;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Static product data container.
 */
public class ProductData {

    /** Singleton instance. */
    private static ProductData instance = null;

    /** Map from product IDs to friendly names. */
    private Map<Integer, String> friendlyNames;

    /** Private constructor, use <code>getInstance()</code> */
    private ProductData() {
    }

    /** Private constructor, use <code>getInstance()</code> */
    private ProductData(Map<Integer, String> friendlyNamesIn) {
        friendlyNames = friendlyNamesIn;
    }

    /**
     * Returns an instance of this class.
     *
     * @return an instance
     */
    public static ProductData getInstance() {
        if (instance == null) {
            Map<Integer, String> friendlyNames = new HashMap<>();
            try {
                Reader reader = new InputStreamReader(System.class.getResourceAsStream("/products.json"));
                List<Product> products = new Loader().loadProducts(reader);
                MapUtils.populateMap(friendlyNames, products,
                    new Transformer<Product, Integer>(){
                        @Override
                        public Integer transform(Product product) {
                            return product.id;
                        }
                    },
                    new Transformer<Product, String>(){
                        @Override
                        public String transform(Product product) {
                            return product.friendlyName;
                        }
                    }
               );
            }
            catch (Exception e) {
                // never happens
                e.printStackTrace();
            }
            instance = new ProductData(friendlyNames);
        }
        return instance;
    }

    /**
     * Gets a collection of friendly names from a collection of product ids.
     *
     * @param productIds the product ids
     * @return the friendly names
     */
    public Collection<String> getFriendlyNames(List<Integer> productIds) {
        return CollectionUtils.collect(productIds, new Transformer<Integer,String>(){
            @Override
            public String transform(Integer id) {
                if (friendlyNames.containsKey(id)) {
                    return friendlyNames.get(id);
                }
                else {
                    return "Unknown id " + id;
                }
            }
        });
    }
}
