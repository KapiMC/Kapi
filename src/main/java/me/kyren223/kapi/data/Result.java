/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package me.kyren223.kapi.data;

import me.kyren223.kapi.annotations.Kapi;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A simple result type that can be either an Ok or an Err.<br>
 * Inspired by Rust's Result type.
 *
 * @param <T> The type of the Ok value
 * @param <E> The type of the Err value
 */
@Kapi
@NullMarked
public class Result<T, E> {
    
    // IMPORTANT NOTE: Internal Assumption (ok == null && err == null || ok != null && err != null) == false
    private final @Nullable T ok;
    private final @Nullable E err;
    
    private Result(@Nullable T ok, @Nullable E err) {
        this.ok = ok;
        this.err = err;
    }
    
    /**
     * @param ok    The Ok value
     * @param <Ok>  The type of the Ok value
     * @param <Err> The type of the Err value
     * @return A new result that stores the Ok value
     */
    @Kapi
    public static <Ok, Err> Result<Ok,Err> ok(Ok ok) {
        return new Result<>(ok, null);
    }
    
    /**
     * @param err   The Err value
     * @param <Ok>  The type of the Ok value
     * @param <Err> The type of the Err value
     * @return A new result that stores the Err value
     */
    @Kapi
    public static <Ok, Err> Result<Ok,Err> err(Err err) {
        return new Result<>(null, err);
    }
    
    /**
     * @return True if this result is an Ok, false otherwise
     */
    @Kapi
    public boolean isOk() {
        return ok != null;
    }
    
    /**
     * @param predicate The predicate to test the Ok value
     * @return True if this Result is Ok and the predicate is true, false otherwise
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
     * @param predicate The predicate to test the Err value
     * @return True if this Result is Err and the predicate is true, false otherwise
     */
    @Kapi
    public boolean isErrAnd(Predicate<E> predicate) {
        return err != null && predicate.test(err);
    }
    
    /**
     * Gets the Ok value contained in this result.<br>
     * <br>
     * Because this function may throw a NullSafetyException,
     * its use is generally discouraged.
     * Instead, prefer calling {@link #unwrapOr(Object)}, or {@link #unwrapOrElse(Supplier)},
     * or use {@link #getOk()} and handle the null value yourself.
     *
     * @return The Ok value
     * @throws NullSafetyException If this Result is an Err
     * @see #expect(String)
     */
    @Kapi
    public T unwrap() {
        if (ok != null) return ok;
        throw new NullSafetyException("Called unwrap on an Err result");
    }
    
    /**
     * @param def The default value to return if this result is an Err
     * @return The Ok value contained in this Result, or the default value if it is an Err
     */
    @Kapi
    public T unwrapOr(T def) {
        return ok != null ? ok : def;
    }
    
    /**
     * @param supplier The supplier to get the default value from if this result is an Err
     * @return The Ok value contained in this result, or the supplied value if it is an Err
     */
    @Kapi
    public T unwrapOrElse(Supplier<T> supplier) {
        return ok != null ? ok : supplier.get();
    }
    
    /**
     * @return The Err value if this result is an Err
     * @throws NullSafetyException If this result is an Ok
     * @see #expectErr(String)
     */
    @Kapi
    public E unwrapErr() {
        if (err == null) {
            throw new NullSafetyException("Called unwrapErr on an Ok result");
        }
        return err;
    }
    
    /**
     * Gets the Ok value contained in this result.<br>
     * <br>
     * Because this function may throw a NullSafetyException,
     * its use is generally discouraged.
     * Instead, prefer calling {@link #unwrapOr(Object)}, or {@link #unwrapOrElse(Supplier)},
     * or use {@link #getOk()} and handle the null value yourself.
     *
     * @param message The message the exception will have if this result is an Err
     * @return The Ok value contained in this result
     * @throws NullSafetyException If this result is an Err
     * @see #unwrap()
     */
    @Kapi
    public T expect(String message) {
        if (ok != null) return ok;
        throw new NullSafetyException(message);
    }
    
    /**
     * Gets the Err value contained in this result.
     *
     * @param message The message the exception will have if this result is an Ok value
     * @return The Err value contained in this result
     * @throws NullSafetyException If this result is an Ok
     * @see #unwrapErr()
     */
    @Kapi
    public E expectErr(String message) {
        if (err != null) return err;
        throw new NullSafetyException(message);
    }
    
    /**
     * @param mapper The mapper to apply to the Ok value contained in this result
     * @param <U>    The type of the Ok value returned by the mapper
     * @return A new result with the mapped Ok value, or an Err result if this result is an Err
     * @see #mapOr(Function, Object)
     * @see #mapOrElse(Function, Supplier)
     */
    @Kapi
    public <U> Result<U,E> map(Function<T,U> mapper) {
        if (ok != null) {
            return Result.ok(mapper.apply(ok));
        } else {
            assert err != null;
            return Result.err(err);
        }
    }
    
    /**
     * @param mapper The mapper to apply to the Ok value contained in this result
     * @param def    The default value to return if this result is an Err
     * @param <U>    The type of the Ok value returned by the mapper
     * @return A new result with the mapped Ok value, or the default value if this result is an Err
     * @see #map(Function)
     * @see #mapOrElse(Function, Supplier)
     */
    @Kapi
    public <U> U mapOr(Function<T,U> mapper, U def) {
        return ok != null ? mapper.apply(ok) : def;
    }
    
    /**
     * @param mapper   The mapper to apply to the Ok value contained in this result
     * @param supplier The supplier for the default value if this result is an Err
     * @param <U>      The type of the Ok value returned by the mapper
     * @return A new result with the mapped Ok value, or the supplied value if this result is an Err
     * @see #map(Function)
     * @see #mapOr(Function, Object)
     */
    @Kapi
    public <U> U mapOrElse(Function<T,U> mapper, Supplier<U> supplier) {
        return ok != null ? mapper.apply(ok) : supplier.get();
    }
    
    /**
     * @param mapper The mapper to apply to the Err value contained in this result
     * @param <F>    The type of the Err value returned by the mapper
     * @return A new result with the mapped Err value, or this Ok result if this result is an Ok
     */
    @Kapi
    public <F> Result<T,F> mapErr(Function<E,F> mapper) {
        if (ok != null) {
            return Result.ok(ok);
        } else {
            assert err != null;
            return Result.err(mapper.apply(err));
        }
    }
    
    /**
     * Arguments passed to <code>and</code> are eagerly evaluated.<br>
     * if you are passing the result of a function call,
     * it's recommended to use {@link #andThen(Function)} which is lazily evaluated.
     *
     * @param resultB The result to apply the function to if this result is an Ok
     * @param <U>     The type of the value returned by the function
     * @return None if this option is None, otherwise returns optionB
     */
    @Kapi
    public <U> Result<U,E> and(Result<U,E> resultB) {
        if (ok != null) {
            return resultB;
        } else {
            assert err != null;
            return Result.err(err);
        }
    }
    
    /**
     * Some languages call this operation <code>flatMap</code>.
     *
     * @param mapper The mapper to apply to the Ok value contained in this result
     * @param <U>    The type of the value returned by the mapper
     * @return Err if this result is an Err,
     *         otherwise calls the mapper with the wrapped Ok value and returns the result
     */
    @Kapi
    public <U> Result<U,E> andThen(Function<T,Result<U,E>> mapper) {
        if (ok != null) {
            return mapper.apply(ok);
        } else {
            assert err != null;
            return Result.err(err);
        }
    }
    
    /**
     * Tests if the Ok value contained in this result matches the predicate.<br>
     * Returns Err if this result is Err or if the predicate returns false,
     * otherwise returns Ok.
     *
     * @param predicate The predicate to test the Ok value with
     * @return Ok if this result is Ok and the predicate returns true, Err otherwise
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
     * @return The Ok value contained in this result if it is Ok, or null if it is Err
     */
    @Kapi
    public @Nullable T getOk() {
        return ok;
    }
    
    /**
     * @return The Err value contained in this result if it is Err, or null if it is Ok
     */
    @Kapi
    public @Nullable E getErr() {
        return err;
    }
    
    /**
     * Inspects the Ok value contained in this result if it's Ok.<br>
     * If this result is Err, the consumer is not called.
     *
     * @param consumer The consumer to call if this result is Ok
     * @return This result (for chaining)
     * @see #ifOk(Consumer)
     * @see #ifErr(Consumer)
     * @see #inspectErr(Consumer)
     */
    @Kapi
    public Result<T,E> inspect(Consumer<T> consumer) {
        if (ok != null) consumer.accept(ok);
        return this;
    }
    
    /**
     * Inspects the Err value contained in this result if it's Err.<br>
     * If this result is Ok, the consumer is not called.
     *
     * @param consumer The consumer to call if this result is Err
     * @return This result (for chaining)
     * @see #ifOk(Consumer)
     * @see #ifErr(Consumer)
     * @see #inspect(Consumer)
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
     * @param resultB The result to return if this result is Err
     * @return This result if it's Ok, or the other result if it's Err
     */
    @Kapi
    public <F> Result<T,F> or(Result<T,F> resultB) {
        return ok != null ? Result.ok(ok) : resultB;
    }
    
    /**
     * @param supplier The supplier to get the other result if this result is Err
     * @param <F>      The type of the other result
     * @return This result if it's Ok, or the supplied result if it's Err
     */
    @Kapi
    public <F> Result<T,F> orElse(Supplier<Result<T,F>> supplier) {
        return ok != null ? Result.ok(ok) : supplier.get();
    }
    
    /**
     * @param consumer The consumer to call if this result is Ok
     */
    @Kapi
    public void ifOk(Consumer<T> consumer) {
        if (ok != null) consumer.accept(ok);
    }
    
    /**
     * @param consumer The consumer to call if this result is Err
     */
    @Kapi
    public void ifErr(Consumer<E> consumer) {
        if (err != null) consumer.accept(err);
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
    
    @Kapi
    @Override
    public int hashCode() {
        return Objects.hash(ok, err);
    }
    
    @Kapi
    @Override
    @Contract(pure = true, value = "null -> false")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result<?,?> result = (Result<?,?>) o;
        return Objects.equals(ok, result.ok) && Objects.equals(err, result.err);
    }
    
    @Kapi
    @Override
    public String toString() {
        return ok != null ? "Ok(" + ok + ")" : "Err(" + err + ")";
    }
}
