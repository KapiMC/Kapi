/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package me.kyren223.kapi.data;

import me.kyren223.kapi.annotations.Kapi;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * A simple pair of two objects.
 *
 * @param <T> The first object
 * @param <U> The second object
 */
@Kapi
public class Pair<T extends @Nullable Object, U extends @Nullable Object> {
    
    private T first;
    private U second;
    
    private Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }
    
    /**
     * @param first  the first value
     * @param second the second value
     * @param <T>    the type of the first value
     * @param <U>    the type of the second value
     * @return a new pair with the given values
     */
    @Kapi
    public static <T extends @Nullable Object, U extends @Nullable Object> Pair<T,U> of(T first, U second) {
        return new Pair<>(first, second);
    }
    
    /**
     * @param <T> the type of the first value
     * @param <U> the type of the second value
     * @return a new pair with no values (first = null, second = null)
     */
    @Kapi
    public static <T extends @Nullable Object, U extends @Nullable Object> Pair<@Nullable T,@Nullable U> of() {
        return new Pair<>(null, null);
    }
    
    /**
     * @return the first value
     */
    @Kapi
    public T getFirst() {
        return first;
    }
    
    /**
     * @param first the first value
     */
    @Kapi
    public void setFirst(T first) {
        this.first = first;
    }
    
    /**
     * @return the second value
     */
    @Kapi
    public U getSecond() {
        return second;
    }
    
    /**
     * @param second the second value
     */
    @Kapi
    public void setSecond(U second) {
        this.second = second;
    }
    
    /**
     * @return a new pair with the same values as this pair (shallow copy)
     */
    @Kapi
    public Pair<T,U> copy() {
        return new Pair<>(first, second);
    }
    
    /**
     * The string will be in the format "Pair(first, second)".
     *
     * @return a string representation of this pair
     */
    @Kapi
    @Override
    public String toString() {
        return "Pair(" + first + ", " + second + ")";
    }
    
    /**
     * @param o the object to compare this pair to
     * @return true if the object is a Pair and the contained values are equal, false otherwise
     */
    @Kapi
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?,?> pair = (Pair<?,?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }
    
    /**
     * @return the hash code of the pair
     */
    @Kapi
    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
