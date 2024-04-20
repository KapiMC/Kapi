package me.kyren223.kapi.data;

import me.kyren223.kapi.annotations.Kapi;

/**
 * A simple pair of two objects.
 * @param <T> The first object
 * @param <U> The second object
 */
@Kapi
public class Pair<T, U> {
    @Kapi public T first;
    @Kapi public U second;
    
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }
    
    public Pair() {
        this(null, null);
    }
    
    @Kapi
    public T getFirst() {
        return first;
    }
    
    @Kapi
    public void setFirst(T first) {
        this.first = first;
    }
    
    @Kapi
    public U getSecond() {
        return second;
    }
    
    @Kapi
    public void setSecond(U second) {
        this.second = second;
    }
    
    /**
     * Creates a new pair with the same values as this pair.<br>
     * Note: This is a shallow copy and does not copy the values themselves.
     *
     * @return A new pair with the same values as this pair
     */
    @Kapi
    public Pair<T, U> copy() {
        return new Pair<>(first, second);
    }
    
    /**
     * Returns a string representation of this pair.<br>
     * The string will be in the format "Pair(first, second)".
     *
     * @return A string representation of this pair
     */
    @Kapi
    @Override
    public String toString() {
        return "Pair(" + first + ", " + second + ")";
    }
    
    /**
     * Creates a new pair with the given values.
     *
     * @param first The first value
     * @param second The second value
     * @param <T> The type of the first value
     * @param <U> The type of the second value
     * @return A new pair with the given values
     */
    @Kapi
    public static <T, U> Pair<T, U> of(T first, U second) {
        return new Pair<>(first, second);
    }
    
}
