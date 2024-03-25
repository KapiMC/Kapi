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
    
    @Kapi
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }
    
    @Kapi
    public Pair() {
        this(null, null);
    }
}
