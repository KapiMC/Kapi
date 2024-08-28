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
 * Represents a value that can be either Ok (value) or Err (error).
 * Similar to Rust's Result type.
 * <p>
 * This class is immutable and thus thread-safe.
 * The contained values themselves may not be thread-safe.
 *
 * @param <T> the type of the Ok value
 * @param <E> the type of the Err value
 */
@Kapi
public class Result<T, E> {
    
    // Assumes that only one of ok and err is non-null and the other is null
    private final @Nullable T ok;
    private final @Nullable E err;
    
    private Result(@Nullable T ok, @Nullable E err) {
        this.ok = ok;
        this.err = err;
    }
    
    /**
     * @param ok  the Ok value
     * @param <T> the type of the Ok value
     * @param <E> the type of the Err value
     * @return a new result that stores the Ok value
     */
    @Kapi
    public static <T, E> Result<T,E> ok(T ok) {
        return new Result<>(ok, null);
    }
    
    /**
     * @param err the Err value
     * @param <T> the type of the Ok value
     * @param <E> the type of the Err value
     * @return a new result that stores the Err value
     */
    @Kapi
    public static <T, E> Result<T,E> err(E err) {
        return new Result<>(null, err);
    }
    
    
    /**
     * Pattern matching to safely handle the result.
     * See {@link #match(Function, Function)} for a version that returns a value.
     *
     * @param ok  the function to call if the result is Ok
     * @param err the function to call if the result is Err
     */
    @Kapi
    @SuppressWarnings("DataFlowIssue")
    public void match(Consumer<T> ok, Consumer<E> err) {
        if (this.ok != null) ok.accept(this.ok);
        else err.accept(this.err);
    }
    
    /**
     * Pattern matching to safely handle the result.
     * See {@link #match(Consumer, Consumer)} for a version that doesn't return a value.
     *
     * @param ok  the function to call if the result is Ok
     * @param err the function to call if the result is Err
     * @param <U> the type of the value returned by the functions
     * @return the value produced by the selected function
     */
    @Kapi
    @SuppressWarnings("DataFlowIssue")
    public <U> U match(Function<T,U> ok, Function<E,U> err) {
        return this.ok != null ? ok.apply(this.ok) : err.apply(this.err);
    }
    
    /**
     * @return true if the result is an Ok, false if it's an Err
     */
    @Kapi
    public boolean isOk() {
        return ok != null;
    }
    
    /**
     * @param predicate the predicate to test the Ok value
     * @return true if the result is Ok and the value inside it matches the predicate, false otherwise
     */
    @Kapi
    public boolean isOkAnd(Predicate<T> predicate) {
        return ok != null && predicate.test(ok);
    }
    
    /**
     * @return True if this result is an Err, false otherwise
     */
    @Kapi
    public boolean isErr() {
        return ok == null;
    }
    
    /**
     * @param predicate the predicate to test the Err value
     * @return true if the result is Err and the value inside it matches the predicate, false otherwise
     */
    @Kapi
    public boolean isErrAnd(Predicate<E> predicate) {
        return err != null && predicate.test(err);
    }
    
    /**
     * Because this function may throw, its use is generally discouraged.
     * Instead, prefer to use pattern matching and handle the Err case explicitly,
     * or call {@link #unwrapOr(Object)}, {@link #unwrapOrElse(Supplier)} or {@link #getOk()}.
     *
     * @return the Ok value
     * @throws NullSafetyException if the Result is an Err
     */
    @Kapi
    public T unwrap() {
        if (ok != null) return ok;
        throw new NullSafetyException("Called unwrap() on an Err result");
    }
    
    /**
     * @param def the default value to return if this result is an Err
     * @return the contained Ok value, or the default value if it is an Err
     */
    @Kapi
    public T unwrapOr(T def) {
        return ok != null ? ok : def;
    }
    
    /**
     * @param supplier the supplier to get the default value from if this result is an Err
     * @return the contained Ok value, or the supplied value if it is an Err
     */
    @Kapi
    public T unwrapOrElse(Supplier<T> supplier) {
        return ok != null ? ok : supplier.get();
    }
    
    /**
     * Because this function may throw, its use is generally discouraged.
     * Instead, prefer to use pattern matching and handle the Ok case explicitly,
     * or call {@link #getErr()}.
     *
     * @return the Err value if this result is an Err
     * @throws NullSafetyException if this result is an Ok
     */
    @Kapi
    public E unwrapErr() {
        if (err != null) return err;
        throw new NullSafetyException("Called unwrapErr() on an Ok result");
    }
    
    /**
     * Because this function may throw, its use is generally discouraged.
     * Instead, prefer to use pattern matching and handle the Err case explicitly,
     * or call {@link #unwrapOr(Object)}, {@link #unwrapOrElse(Supplier)} or {@link #getErr()}.
     *
     * @param message the message the exception will have if this result is an Err
     * @return the contained Ok value
     * @throws NullSafetyException if this result is an Err
     */
    @Kapi
    public T expect(String message) {
        if (ok != null) return ok;
        throw new NullSafetyException(message);
    }
    
    /**
     * Because this function may throw, its use is generally discouraged.
     * Instead, prefer to use pattern matching and handle the Ok case explicitly,
     * or call {@link #getOk()}.
     *
     * @param message the message the exception will have if this result is an Ok value
     * @return the contained Err value
     * @throws NullSafetyException if this result is an Ok
     */
    @Kapi
    public E expectErr(String message) {
        if (err != null) return err;
        throw new NullSafetyException(message);
    }
    
    /**
     * Maps a <code>Result&lt;T,E&gt;</code> to a <code>Result&lt;U,E&gt;</code>
     * by applying a function to the contained Ok value (if Ok) or returns Err (if Err).
     *
     * @param mapper the mapper to apply to the Ok value contained in this result
     * @param <U>    the type of the Ok value returned by the mapper
     * @return a new result with the mapped Ok value, or an Err result if this result is an Err
     */
    @Kapi
    @SuppressWarnings("DataFlowIssue")
    public <U> Result<U,E> map(Function<T,U> mapper) {
        return ok != null ? Result.ok(mapper.apply(ok)) : Result.err(err);
    }
    
    /**
     * Arguments passed to <code>mapOr</code> are eagerly evaluated.
     * If you are passing the result of a function call,
     * it's recommended to use {@link #mapOrElse(Supplier, Function)} which is lazily evaluated.
     *
     * @param def    the default value to return if this result is an Err
     * @param mapper the mapper to apply to the Ok value contained in this result
     * @param <U>    the type of the Ok value returned by the mapper
     * @return a new result with the mapped Ok value, or the default value if this result is an Err
     */
    @Kapi
    public <U> U mapOr(U def, Function<T,U> mapper) {
        return ok != null ? mapper.apply(ok) : def;
    }
    
    /**
     * @param def    the supplier to get the default value from if this result is an Err
     * @param mapper the mapper to apply to the Ok value contained in this result
     * @param <U>    the type of the Ok value returned by the mapper
     * @return a new result with the mapped Ok value, or the supplied value if this result is an Err
     */
    @Kapi
    public <U> U mapOrElse(Supplier<U> def, Function<T,U> mapper) {
        return ok != null ? mapper.apply(ok) : def.get();
    }
    
    /**
     * Maps a <code>Result&lt;T,E&gt;</code> to a <code>Result&lt;T,F&gt;</code>
     * by applying a function to the contained Err value (if Err) or returns Ok (if Ok).
     *
     * @param mapper the mapper to apply to the Err value contained in this result
     * @param <F>    the type of the Err value returned by the mapper
     * @return a new result with the mapped Err value, or this Ok result if this result is an Ok
     */
    @Kapi
    @SuppressWarnings("DataFlowIssue")
    public <F> Result<T,F> mapErr(Function<E,F> mapper) {
        return ok != null ? Result.ok(ok) : Result.err(mapper.apply(err));
    }
    
    /**
     * Arguments passed to <code>and</code> are eagerly evaluated.<br>
     * if you are passing the result of a function call,
     * it's recommended to use {@link #andThen(Function)} which is lazily evaluated.
     *
     * @param res the result to apply the function to if this result is an Ok
     * @param <U> the type of the value returned by the function
     * @return <code>res</code> if the result is Ok, otherwise returns the Err value of this result
     */
    @Kapi
    @SuppressWarnings("DataFlowIssue")
    public <U> Result<U,E> and(Result<U,E> res) {
        return ok != null ? res : Result.err(err);
    }
    
    /**
     * This function can be used for control flow based on <code>Result</code> values.
     *
     * @param f   the function to apply to the Ok value contained in this result
     * @param <U> the type of the value returned by the function
     * @return the result of calling <code>f</code> if this result is Ok,
     *     otherwise returns the Err value of this result
     */
    @Kapi
    @SuppressWarnings("DataFlowIssue")
    public <U> Result<U,E> andThen(Function<T,Result<U,E>> f) {
        return ok != null ? f.apply(ok) : Result.err(err);
    }
    
    /**
     * @param predicate the predicate to test the Ok value with
     * @return Ok if the result is Ok and the predicate returns true, otherwise returns Err
     */
    @Kapi
    public Result<T,E> filter(Predicate<T> predicate) {
        if (ok != null && predicate.test(ok)) {
            return Result.ok(ok);
        } else {
            assert err != null;
            return Result.err(err);
        }
    }
    
    /**
     * @return the contained Ok value if this result is Ok, or null if it is Err
     */
    @Kapi
    public @Nullable T getOk() {
        return ok;
    }
    
    /**
     * @return the contained Err value if this result is Err, or null if it is Ok
     */
    @Kapi
    public @Nullable E getErr() {
        return err;
    }
    
    /**
     * @param consumer the consumer to call if this result is Ok
     * @return this result (for chaining)
     */
    @Kapi
    public Result<T,E> inspect(Consumer<T> consumer) {
        if (ok != null) consumer.accept(ok);
        return this;
    }
    
    /**
     * @param consumer the consumer to call if this result is Err
     * @return this result (for chaining)
     */
    @Kapi
    public Result<T,E> inspectErr(Consumer<E> consumer) {
        if (err != null) consumer.accept(err);
        return this;
    }
    
    /**
     * @return Option.some(ok) if this result is Ok, or Option.none() if it is Err
     */
    @Kapi
    public Option<T> ok() {
        return Option.of(ok);
    }
    
    /**
     * @return Option.some(err) if this result is Err, or Option.none() if it is Ok
     */
    @Kapi
    public Option<E> err() {
        return Option.of(err);
    }
    
    /**
     * Arguments passed to <code>or</code> are eagerly evaluated.
     * If you are passing the result of a function call,
     * it's recommended to use {@link #orElse(Supplier)} which is lazily evaluated.
     *
     * @param res the result to return if this result is Err
     * @param <F> the type of the other result
     * @return this result if it's Ok, or the other result if it's Err
     */
    @Kapi
    public <F> Result<T,F> or(Result<T,F> res) {
        return ok != null ? Result.ok(ok) : res;
    }
    
    /**
     * @param supplier the function for the other result if this result is Err
     * @param <F>      the type of the other result
     * @return this result if it's Ok, or the supplied result if it's Err
     */
    @Kapi
    public <F> Result<T,F> orElse(Supplier<Result<T,F>> supplier) {
        return ok != null ? Result.ok(ok) : supplier.get();
    }
    
    /**
     * Returns the Ok value or throws the Err value if this result is an Err.<br>
     * Error value must be a RuntimeException or a subclass of RuntimeException.<br>
     * If the Err is not a RuntimeException, a NullSafetyException is thrown.
     *
     * @return The Ok value
     * @throws RuntimeException    If this result is an Err and the Err value is a RuntimeException
     * @throws NullSafetyException If this result is an Err and the Err value is not a RuntimeException
     */
    @Kapi
    public T unwrapOrThrow() {
        if (ok != null) return ok;
        if (err instanceof RuntimeException exception) {
            throw exception;
        }
        throw new NullSafetyException("Called unwrapOrThrow on an Err result that is not a RuntimeException");
    }
    
    /**
     * @return the combined hash code of either Ok or Err and null
     * @see Objects#hash(Object...)
     */
    @Kapi
    @Override
    public int hashCode() {
        return Objects.hash(ok, err);
    }
    
    /**
     * @param o the object to compare this result to
     * @return true if the object is a Result and the contained values are equal, false otherwise
     */
    @Kapi
    @Override
    @Contract(pure = true, value = "null -> false")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result<?,?> result = (Result<?,?>) o;
        return Objects.equals(ok, result.ok) && Objects.equals(err, result.err);
    }
    
    /**
     * @return "Ok(" + ok + ")" if the result is Ok, or "Err(" + err + ")" if it is Err
     */
    @Kapi
    @Override
    public String toString() {
        return ok != null ? "Ok(" + ok + ")" : "Err(" + err + ")";
    }
}
