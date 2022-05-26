package com.suse.matcher.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Various utility methods to deal with collections
 */
public final class CollectionUtils {

    private CollectionUtils() {
        // Prevent instantiation
    }

    /**
     * Evaluates if a collection is empty or null
     *
     * @param collection the collection
     * @return true if collection is null or is empty
     *
     * @param <T> The type of the collection elements
     */
    public static <T> boolean isEmptyOrNull(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Merges two collection by returning a collection where each element is the result of passing the corresponding
     * element of each of {@code streamA} and {@code streamB} to {@code function}.
     *
     * <pre>{@code
     * CollectionUtils.zip(
     *   List.of("foo1", "foo2", "foo3"),
     *   List.of("bar1", "bar2"),
     *   (arg1, arg2) -> arg1 + ":" + arg2)
     * }</pre>
     *
     * <p>will return a collection containing {@code "foo1:bar1", "foo2:bar2"}.
     *
     * <p>The resulting collection will only be as long as the shorter of the two inputs; if one
     * stream is longer, its extra elements will be ignored.
     *
     * @param collectionA the first collection
     * @param collectionB the second collection
     * @param function the function to apply to merge the two elements
     * @return the collection of the merged elements from collectionA and collectionB applying the given function.
     * @param <T> Type of the first collection elements
     * @param <U> Type of the second collection elements
     * @param <R> Type of the returned collection elements
     */
    public static <T, U, R> Collection<R> zip(Collection<T> collectionA, Collection<U> collectionB, BiFunction<T, U, R> function) {
        if (isEmptyOrNull(collectionA) || isEmptyOrNull(collectionB)) {
            return Collections.emptyList();
        }

        final List<R> zippedList = new ArrayList<>();

        final Iterator<T> iteratorA = collectionA.iterator();
        final Iterator<U> iteratorB = collectionB.iterator();

        while (iteratorA.hasNext() && iteratorB.hasNext()) {
            zippedList.add(function.apply(iteratorA.next(), iteratorB.next()));
        }

        return zippedList;
    }

    /**
     * Custom toList collector that shuffles the list after creating it
     *
     * @param random the random numbers generator
     * @param <T> the type of the list
     * @return the shuffled list
     */
    public static <T> Collector<T, ?, List<T>> toShuffledList(Random random) {
        return Collectors.collectingAndThen(Collectors.toList(), list -> {
            Collections.shuffle(list, random);
            return list;
        });
    }
}
