/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.data;

import io.github.kapimc.kapi.annotations.Kapi;
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
public final class Result<T, E> {
    
    // Assumptions:
    // if err == null -> Ok variant
    // if err != null -> Err variant
    // if ok == null && err == null -> Err variant (T is Void)
    
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
     * @return a new Ok result containing the given Ok value
     */
    @Kapi
    public static <T, E> Result<T,E> ok(T ok) {
        return new Result<>(ok, null);
    }
    
    /**
     * @param ok  null to indicate a Void Ok type
     * @param <E> the type of the Err value
     * @return a new Ok result containing null (Void)
     */
    @Kapi
    public static <E> Result<Void,E> ok(@Nullable Void ok) {
        return new Result<>(ok, null);
    }
    
    /**
     * @param err the Err value
     * @param <T> the type of the Ok value
     * @param <E> the type of the Err value
     * @return a new Err result containing the given Err value
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
        if (this.err == null) ok.accept(this.ok);
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
        return this.err == null ? ok.apply(this.ok) : err.apply(this.err);
    }
    
    /**
     * @return true if the result is an Ok, false if it's an Err
     */
    @Kapi
    public boolean isOk() {
        return this.err == null;
    }
    
    /**
     * @param predicate the predicate to test the Ok value
     * @return true if the result is Ok and the value inside it matches the predicate, false otherwise
     */
    @Kapi
    @SuppressWarnings("DataFlowIssue")
    public boolean isOkAnd(Predicate<T> predicate) {
        return this.err == null && predicate.test(this.ok);
    }
    
    /**
     * @return True if this result is an Err, false otherwise
     */
    @Kapi
    public boolean isErr() {
        return this.err != null;
    }
    
    /**
     * @param predicate the predicate to test the Err value
     * @return true if the result is Err and the value inside it matches the predicate, false otherwise
     */
    @Kapi
    public boolean isErrAnd(Predicate<E> predicate) {
        return this.err != null && predicate.test(this.err);
    }
    
    /**
     * Because this function may throw, its use is generally discouraged.
     * Instead, prefer to use pattern matching and handle the Err case explicitly,
     * or call {@link #unwrapOr(Object)}, {@link #unwrapOrElse(Supplier)} or {@link #getOk()}.
     *
     * @return the Ok value
     * @throws UnsafeUnwrapException if the Result is an Err
     */
    @Kapi
    @SuppressWarnings("DataFlowIssue")
    public T unwrap() {
        if (this.err == null) return this.ok;
        throw new UnsafeUnwrapException("Called unwrap() on an Err result");
    }
    
    /**
     * @param def the default value to return if this result is an Err
     * @return the contained Ok value, or the default value if it is an Err
     */
    @Kapi
    @SuppressWarnings("DataFlowIssue")
    public T unwrapOr(T def) {
        return this.err == null ? this.ok : def;
    }
    
    /**
     * @param supplier the supplier to get the default value from if this result is an Err
     * @return the contained Ok value, or the supplied value if it is an Err
     */
    @Kapi
    @SuppressWarnings("DataFlowIssue")
    public T unwrapOrElse(Supplier<T> supplier) {
        return this.err == null ? this.ok : supplier.get();
    }
    
    /**
     * Because this function may throw, its use is generally discouraged.
     * Instead, prefer to use pattern matching and handle the Ok case explicitly,
     * or call {@link #getErr()}.
     *
     * @return the Err value if this result is an Err
     * @throws UnsafeUnwrapException if this result is an Ok
     */
    @Kapi
    public E unwrapErr() {
        if (this.err != null) return this.err;
        throw new UnsafeUnwrapException("Called unwrapErr() on an Ok result");
    }
    
    /**
     * Because this function may throw, its use is generally discouraged.
     * Instead, prefer to use pattern matching and handle the Err case explicitly,
     * or call {@link #unwrapOr(Object)}, {@link #unwrapOrElse(Supplier)} or {@link #getErr()}.
     *
     * @param message the message the exception will have if this result is an Err
     * @return the contained Ok value
     * @throws UnsafeUnwrapException if this result is an Err
     */
    @Kapi
    @SuppressWarnings("DataFlowIssue")
    public T expect(String message) {
        if (this.err == null) return this.ok;
        throw new UnsafeUnwrapException(message);
    }
    
    /**
     * Because this function may throw, its use is generally discouraged.
     * Instead, prefer to use pattern matching and handle the Ok case explicitly,
     * or call {@link #getOk()}.
     *
     * @param message the message the exception will have if this result is an Ok value
     * @return the contained Err value
     * @throws UnsafeUnwrapException if this result is an Ok
     */
    @Kapi
    public E expectErr(String message) {
        if (this.err != null) return this.err;
        throw new UnsafeUnwrapException(message);
    }
    
    /**
     * Maps a {@code Result<T,E>} to a {@code Result<U,E>}
     * by applying a function to the contained Ok value (if Ok) or returns Err (if Err).
     *
     * @param mapper the mapper to apply to the Ok value contained in this result
     * @param <U>    the type of the Ok value returned by the mapper
     * @return a new result with the mapped Ok value, or an Err result if this result is an Err
     */
    @Kapi
    @SuppressWarnings("DataFlowIssue")
    public <U> Result<U,E> map(Function<T,U> mapper) {
        return this.err == null ? Result.ok(mapper.apply(this.ok)) : Result.err(this.err);
    }
    
    /**
     * Arguments passed to {@code mapOr} are eagerly evaluated.
     * If you are passing the result of a function call,
     * it's recommended to use {@link #mapOrElse(Supplier, Function)} which is lazily evaluated.
     *
     * @param def    the default value to return if this result is an Err
     * @param mapper the mapper to apply to the Ok value contained in this result
     * @param <U>    the type of the Ok value returned by the mapper
     * @return a new result with the mapped Ok value, or the default value if this result is an Err
     */
    @Kapi
    @SuppressWarnings("DataFlowIssue")
    public <U> U mapOr(U def, Function<T,U> mapper) {
        return this.err == null ? mapper.apply(this.ok) : def;
    }
    
    /**
     * @param def    the supplier to get the default value from if this result is an Err
     * @param mapper the mapper to apply to the Ok value contained in this result
     * @param <U>    the type of the Ok value returned by the mapper
     * @return a new result with the mapped Ok value, or the supplied value if this result is an Err
     */
    @Kapi
    @SuppressWarnings("DataFlowIssue")
    public <U> U mapOrElse(Supplier<U> def, Function<T,U> mapper) {
        return this.err == null ? mapper.apply(this.ok) : def.get();
    }
    
    /**
     * Maps a {@code Result<T,E>} to a {@code Result<T,F>}
     * by applying a function to the contained Err value (if Err) or returns Ok (if Ok).
     *
     * @param mapper the mapper to apply to the Err value contained in this result
     * @param <F>    the type of the Err value returned by the mapper
     * @return a new result with the mapped Err value, or this Ok result if this result is an Ok
     */
    @Kapi
    @SuppressWarnings("DataFlowIssue")
    public <F> Result<T,F> mapErr(Function<E,F> mapper) {
        return this.err == null ? Result.ok(this.ok) : Result.err(mapper.apply(this.err));
    }
    
    /**
     * Arguments passed to {@code and} are eagerly evaluated.<br>
     * if you are passing the result of a function call,
     * it's recommended to use {@link #andThen(Function)} which is lazily evaluated.
     *
     * @param res the result to apply the function to if this result is an Ok
     * @param <U> the type of the value returned by the function
     * @return {@code res} if the result is Ok, otherwise returns the Err value of this result
     */
    @Kapi
    public <U> Result<U,E> and(Result<U,E> res) {
        return this.err == null ? res : Result.err(this.err);
    }
    
    /**
     * This function can be used for control flow based on {@code Result} values.
     *
     * @param f   the function to apply to the Ok value contained in this result
     * @param <U> the type of the value returned by the function
     * @return the result of calling {@code f} if this result is Ok,
     *     otherwise returns the Err value of this result
     */
    @Kapi
    @SuppressWarnings("DataFlowIssue")
    public <U> Result<U,E> andThen(Function<T,Result<U,E>> f) {
        return this.err == null ? f.apply(this.ok) : Result.err(this.err);
    }
    
    /**
     * @return the contained Ok value if this result is Ok, or null if it is Err
     */
    @Kapi
    public @Nullable T getOk() {
        return this.ok;
    }
    
    /**
     * @return the contained Err value if this result is Err, or null if it is Ok
     */
    @Kapi
    public @Nullable E getErr() {
        return this.err;
    }
    
    /**
     * @param consumer the consumer to call if this result is Ok
     * @return this result (for chaining)
     */
    @Kapi
    @SuppressWarnings("DataFlowIssue")
    public Result<T,E> inspect(Consumer<T> consumer) {
        if (this.err == null) consumer.accept(this.ok);
        return this;
    }
    
    /**
     * @param consumer the consumer to call if this result is Err
     * @return this result (for chaining)
     */
    @Kapi
    public Result<T,E> inspectErr(Consumer<E> consumer) {
        if (this.err != null) consumer.accept(this.err);
        return this;
    }
    
    /**
     * @return Option.some(ok) if this result is Ok, or Option.none() if it is Err
     */
    @Kapi
    public Option<T> ok() {
        return Option.of(this.ok);
    }
    
    /**
     * @return Option.some(err) if this result is Err, or Option.none() if it is Ok
     */
    @Kapi
    public Option<E> err() {
        return Option.of(this.err);
    }
    
    /**
     * Arguments passed to {@code or} are eagerly evaluated.
     * If you are passing the result of a function call,
     * it's recommended to use {@link #orElse(Supplier)} which is lazily evaluated.
     *
     * @param res the result to return if this result is Err
     * @param <F> the type of the other result
     * @return this result if it's Ok, or the other result if it's Err
     */
    @Kapi
    @SuppressWarnings("DataFlowIssue")
    public <F> Result<T,F> or(Result<T,F> res) {
        return this.err == null ? Result.ok(this.ok) : res;
    }
    
    /**
     * @param supplier the function for the other result if this result is Err
     * @param <F>      the type of the other result
     * @return this result if it's Ok, or the supplied result if it's Err
     */
    @Kapi
    @SuppressWarnings("DataFlowIssue")
    public <F> Result<T,F> orElse(Supplier<Result<T,F>> supplier) {
        return this.err == null ? Result.ok(this.ok) : supplier.get();
    }
    
    /**
     * @param result the result to unwrap
     * @param <T>    the type of the Ok value
     * @param <E>    the type of the Err value
     * @return the contained Ok value if this result is Ok, or throws the Err value if this result is an Err
     */
    @Kapi
    @SuppressWarnings("DataFlowIssue")
    public static <T, E extends RuntimeException> T unwrapOrThrow(Result<T,E> result) {
        if (result.err != null) throw result.err;
        return result.ok;
    }
    
    /**
     * @return the combined hash code of either Ok or Err and null
     * @see Objects#hash(Object...)
     */
    @Kapi
    @Override
    public int hashCode() {
        return Objects.hash(this.ok, this.err);
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
        return Objects.equals(this.ok, result.ok) && Objects.equals(this.err, result.err);
    }
    
    /**
     * @return "Ok(" + ok + ")" if the result is Ok, or "Err(" + err + ")" if it is Err
     */
    @Kapi
    @Override
    public String toString() {
        return this.err == null ? "Ok(" + this.ok + ")" : "Err(" + this.err + ")";
    }
    
    /**
     * Converts a runtime exception into a Result.
     * If the supplier throws an exception, the Result will be Err.
     *
     * @param supplier the supplier to call
     * @param <T>      the type of the value returned by the supplier
     * @return the value returned by the supplier if it doesn't throw an exception, or Err if it does
     */
    @Kapi
    public static <T> Result<T,RuntimeException> tryCatch(Supplier<T> supplier) {
        try {
            return Result.ok(supplier.get());
        } catch (RuntimeException e) {
            return Result.err(e);
        }
    }
    
}
