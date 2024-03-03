package me.kyren223.kapi.utility;

public class Pair<T, U> {
    public T first;
    public U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }
    
    public Pair() {
        this(null, null);
    }
}
