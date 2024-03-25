package me.kyren223.kapi.data;

import me.kyren223.kapi.annotations.Kapi;

import java.util.function.Supplier;

/**
 * A simple result type that can be either an Ok or an Err.<br>
 * Inspired by Rust's Result type.
 *
 * @param <Ok> The type of the Ok value
 * @param <Err> The type of the Err value
 */
@Kapi
public class Result<Ok, Err> {
    
    private final Ok ok;
    private final Err err;
    
    private Result(Ok ok, Err err) {
        this.ok = ok;
        this.err = err;
    }
    
    /**
     * Returns a result that stores an Ok value.
     *
     * @param ok The Ok value
     * @param <Ok> The type of the Ok value
     * @param <Err> The type of the Err value
     * @return A result that stores an Ok value
     */
    @Kapi
    public static <Ok, Err> Result<Ok, Err> ok(Ok ok) {
        return new Result<>(ok, null);
    }
    
    /**
     * Returns a result that stores an Err value.
     *
     * @param err The Err value
     * @param <Ok> The type of the Ok value
     * @param <Err> The type of the Err value
     * @return A result that stores an Err value
     */
    @Kapi
    public static <Ok, Err> Result<Ok, Err> err(Err err) {
        return new Result<>(null, err);
    }
    
    /**
     * Returns whether this result is an Ok.
     *
     * @return Whether this result is an Ok
     */
    @Kapi
    public boolean isOk() {
        return ok != null;
    }
    
    /**
     * Returns whether this result is an Err.
     *
     * @return Whether this result is an Err
     */
    @Kapi
    public boolean isErr() {
        return err != null;
    }
    
    /**
     * Returns the Ok value.
     *
     * @return The Ok value or null if this result is an Err
     */
    @Kapi
    public Ok getOk() {
        return ok;
    }
    
    /**
     * Returns the Err value.
     *
     * @return The Err value or null if this result is an Ok
     */
    @Kapi
    public Err getErr() {
        return err;
    }
    
    /**
     * Returns the Ok value or throws an exception if this result is an Err.
     *
     * @return The Ok value
     * @throws IllegalStateException If this result is an Err
     */
    @Kapi
    public Ok unwrap() {
        if (isOk()) return ok;
        throw new IllegalStateException("Called unwrap on an Err result");
    }
    
    /**
     * Returns the Ok value or a default value if this result is an Err.
     *
     * @param def The default value
     * @return The Ok value or the default value if this result is an Err
     */
    @Kapi
    public Ok unwrapOr(Ok def) {
        return isOk() ? ok : def;
    }
    
    /**
     * Returns the Ok value or a value produced by a supplier if this result is an Err.
     *
     * @param supplier The supplier
     * @return The Ok value or a value produced by the supplier if this result is an Err
     */
    @Kapi
    public Ok unwrapOrGet(Supplier<Ok> supplier) {
        return isOk() ? ok : supplier.get();
    }
    
    /**
     * Returns the Ok value or throws an exception with a custom message if this result is an Err.
     *
     * @param message The message
     * @return The Ok value
     * @throws IllegalStateException If this result is an Err
     */
    @Kapi
    public Ok expect(String message) {
        if (isOk()) return ok;
        throw new IllegalStateException(message);
    }
}
