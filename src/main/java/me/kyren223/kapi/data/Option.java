/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package me.kyren223.kapi.data;

import me.kyren223.kapi.annotations.Kapi;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Represents a value that can be either Some (value) or None.
 * Similar to Rust's Option type, aims to replace nullable types.
 * <p>
 * This class is immutable and thus thread-safe.
 * The contained value itself may not be thread-safe.
 *
 * @param <T> the type of the value contained in this option
 */
@Kapi
public class Option<T> {
    
    private static final Option<?> NONE = new Option<>(null);
    
    private final @Nullable T value;
    
    private Option(@Nullable T value) {
        this.value = value;
    }
    
    /**
     * @param value the value contained in this option
     * @param <T>   the type of the value contained in this option
     * @return {@link #some(Object)} if the value is not null, and {@link #none()} if it is null
     */
    @Kapi
    @SuppressWarnings("unchecked")
    public static <T> Option<T> of(@Nullable T value) {
        if (value == null) return (Option<T>) NONE;
        return new Option<>(value);
    }
    
    /**
     * Creates a new Option containing the given Non-null value.
     * See {@link #of(Object)} for a version that accepts null values.
     *
     * @param value the value contained in this option
     * @param <T>   the type of the value contained in this option
     * @return a new Option containing the value
     */
    @Kapi
    public static <T> Option<T> some(T value) {
        return new Option<>(value);
    }
    
    /**
     * @param <T> the type of the value contained in this option
     * @return the None variant, similar to null
     * @see #some(Object)
     * @see #of(Object)
     */
    @Kapi
    @SuppressWarnings("unchecked")
    public static <T> Option<T> none() {
        return (Option<T>) NONE;
    }
    
    /**
     * Pattern matching to safely handle the option.
     * See {@link #match(Function, Supplier)} for a version that returns a value.
     *
     * @param some the function to call if the option is Some
     * @param none the function to call if the option is None
     */
    @Kapi
    public void match(Consumer<T> some, Runnable none) {
        if (value != null) some.accept(value);
        else none.run();
    }
    
    /**
     * Pattern matching to safely handle the option.
     * See {@link #match(Consumer, Runnable)} for a version that doesn't return a value.
     *
     * @param some the function to call if the option is Some
     * @param none the function to call if the option is None
     * @param <U>  the type of the value returned by the functions
     * @return the value produced by the selected function
     */
    @Kapi
    public <U> U match(Function<T,U> some, Supplier<U> none) {
        return value != null ? some.apply(value) : none.get();
    }
    
    /**
     * @return true if the option is None, false if it's Some
     * @see #isSome()
     */
    @Kapi
    public boolean isNone() {
        return value == null;
    }
    
    /**
     * @return true if the option is Some, false if it's None
     * @see #isNone()
     */
    @Kapi
    public boolean isSome() {
        return value != null;
    }
    
    /**
     * @param predicate the predicate to test the value with
     * @return true if the value is Some and the value inside it matches the predicate, false otherwise
     */
    @Kapi
    public boolean isSomeAnd(Predicate<T> predicate) {
        return value != null && predicate.test(value);
    }
    
    /**
     * Because this function may throw, its use is generally discouraged.
     * Instead, prefer to use pattern matching and handle the None case explicitly,
     * or call {@link #unwrapOr(Object)}, {@link #unwrapOrElse(Supplier)} or {@link #get()}.
     *
     * @return the contained Some value
     * @throws NullSafetyException if this option is None
     */
    @Kapi
    public T unwrap() {
        if (value != null) return value;
        throw new NullSafetyException("Called unwrap() on a None option");
    }
    
    /**
     * @param def the default value to return if this option is None
     * @return the contained Some value, or the default value if it is None
     */
    @Kapi
    public T unwrapOr(T def) {
        return value != null ? value : def;
    }
    
    /**
     * @param supplier the supplier to get the default value from if this option is None
     * @return the contained Some value, or the supplied value if it is a None
     */
    @Kapi
    public T unwrapOrElse(Supplier<T> supplier) {
        return value != null ? value : supplier.get();
    }
    
    /**
     * Because this function may throw, its use is generally discouraged.
     * Instead, prefer to use pattern matching and handle the None case explicitly,
     * or call {@link #unwrapOr(Object)}, {@link #unwrapOrElse(Supplier)} or {@link #get()}.
     *
     * @param message the message the exception will have if this option is None
     * @return the contained Some value
     * @throws NullSafetyException if this option is None
     */
    @Kapi
    public T expect(String message) {
        if (value != null) return value;
        throw new NullSafetyException(message);
    }
    
    /**
     * Maps an <code>Option&lt;T&gt;</code> to an <code>Option&lt;U&gt;</code>
     * by applying a function to the contained value (if Some) or returns None (if None).
     *
     * @param mapper the mapper to apply to the Some value
     * @param <U>    the type of the mapped value
     * @return a new option with the mapped value, or None if this option is None
     */
    @Kapi
    public <U> Option<U> map(Function<T,U> mapper) {
        return value != null ? Option.some(mapper.apply(value)) : Option.none();
    }
    
    /**
     * Arguments passed to <code>mapOr</code> are eagerly evaluated.
     * If you are passing the result of a function call,
     * it's recommended to use {@link #mapOrElse(Supplier, Function)} which is lazily evaluated.
     *
     * @param def    the default value to return if this option is None
     * @param mapper the function to apply to the value contained in this option
     * @param <U>    the type of the value returned by the mapping function
     * @return the provided default (if None), or applies a function to the contained value (if Some)
     */
    @Kapi
    public <U> U mapOr(U def, Function<T,U> mapper) {
        return value != null ? mapper.apply(value) : def;
    }
    
    /**
     * @param def    the supplier to get the default value from if this option is None
     * @param mapper the function to apply to the value contained in this option if Some
     * @param <U>    the type of the value to return
     * @return computes a default function result (if None),
     *     or applies a different function to the contained value (if Some)
     */
    @Kapi
    public <U> U mapOrElse(Supplier<U> def, Function<T,U> mapper) {
        return value != null ? mapper.apply(value) : def.get();
    }
    
    /**
     * Arguments passed to <code>and</code> are eagerly evaluated.
     * If you are passing the result of a function call,
     * it's recommended to use {@link #andThen(Function)} which is lazily evaluated.
     *
     * @param optB the option to apply the function to if this option is Some
     * @param <U>  the type of the returned value
     * @return None if this option is None, otherwise returns optB
     */
    @Kapi
    public <U> Option<U> and(Option<U> optB) {
        return value != null ? optB : Option.none();
    }
    
    /**
     * Some languages call this operation flatMap.
     *
     * @param f   the function to apply to the value contained in this option
     * @param <U> the type of the returned value
     * @return None if the option is None,
     *     otherwise calls <code>f</code> with the wrapped value and returns the result
     */
    @Kapi
    public <U> Option<U> andThen(Function<T,Option<U>> f) {
        return value != null ? f.apply(value) : Option.none();
    }
    
    /**
     * @param predicate the predicate to test the value with
     * @return Some if the option is Some and the predicate returns true, otherwise returns None
     */
    @Kapi
    public Option<T> filter(Predicate<T> predicate) {
        return value != null && predicate.test(value) ? this : Option.none();
    }
    
    /**
     * @return the contained value if Some, or null if None
     */
    @Kapi
    public @Nullable T get() {
        return value;
    }
    
    /**
     * @param consumer the consumer to call if this option is Some
     * @return this option (for chaining)
     */
    @Kapi
    public Option<T> inspect(Consumer<T> consumer) {
        if (value != null) consumer.accept(value);
        return this;
    }
    
    /**
     * Converts the <code>Option&lt;T&gt;</code> to a <code>Result&lt;T, E&gt;</code>,
     * mapping {@link #some(Object)} to {@link Result#ok(Object)} and {@link #none()} to {@link Result#err(Object)}.
     * Arguments passed to <code>okOr</code> are eagerly evaluated.
     * If you are passing the result of a function call,
     * it's recommended to use {@link #okOrElse(Supplier)} which is lazily evaluated.
     *
     * @param err the error to return if this option is None
     * @param <E> the type of the error
     * @return Result.ok(value) if this option is Some, or Result.err(err) if this option is None
     */
    @Kapi
    public <E> Result<T,E> okOr(E err) {
        return value != null ? Result.ok(value) : Result.err(err);
    }
    
    /**
     * Converts the <code>Option&lt;T&gt;</code> to a <code>Result&lt;T, E&gt;</code>,
     * mapping {@link #some(Object)} to {@link Result#ok(Object)} and {@link #none()} to {@link Result#err(Object)}.
     *
     * @param err the function to get the error from if this option is None
     * @param <E> the type of the error
     * @return Result.ok(value) if this option is Some, or Result.err(err.get()) if this option is None
     */
    @Kapi
    public <E> Result<T,E> okOrElse(Supplier<E> err) {
        return value != null ? Result.ok(value) : Result.err(err.get());
    }
    
    /**
     * Arguments passed to <code>or</code> are eagerly evaluated.
     * If you are passing the result of a function call,
     * it's recommended to use {@link #orElse(Supplier)} which is lazily evaluated.
     *
     * @param optB the option to return if this option is None
     * @return this option if it's Some, or the other option if it's None
     */
    @Kapi
    public Option<T> or(Option<T> optB) {
        return value != null ? this : optB;
    }
    
    /**
     * @param supplier the function for the other option if this option is None
     * @return this option if it's Some, otherwise returns the supplied option
     */
    @Kapi
    public Option<T> orElse(Supplier<Option<T>> supplier) {
        return value != null ? this : supplier.get();
    }
    
    /**
     * @return the hash code of the contained value if Some, or 0 if None
     * @see Objects#hashCode(Object)
     */
    @Kapi
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
    
    /**
     * @param o the object to compare this option to
     * @return true if the object is an Option and the contained values are equal, false otherwise
     */
    @Kapi
    @Override
    @Contract(pure = true, value = "null -> false")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Option<?> option = (Option<?>) o;
        return Objects.equals(value, option.value);
    }
    
    /**
     * @return "Some(" + value + ")" if the option is Some, or "None" if it's None
     */
    @Kapi
    @Override
    public String toString() {
        return value != null ? "Some(" + value + ")" : "None";
    }
}
