/*
 * Copyright (c) 2024 Kapi Contributors. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted if the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions, the following disclaimer and the list of contributors.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation and/or
 *    other materials provided with the distribution.
 *
 * 3. The buyer of the "Kapi" API is granted the right to use this software
 *    as a dependency in their own software projects. However, the buyer
 *    may not resell or distribute the "Kapi" API, in whole or in part, to other parties.
 *
 * 4. The buyer may include the "Kapi" API in a "fat jar" along with their own code.
 *    The license for the "fat jar" is at the buyer's discretion and may allow
 *    redistribution of the "fat jar", but the "Kapi" API code inside the "fat jar"
 *    must not be modified.
 *
 * 5. Neither the name "Kapi" nor the names of its contributors may be used to endorse
 *    or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY "Kapi" API, AND ITS CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL "Kapi" API, AND CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Kapi Contributors:
 * - Kyren223
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
 * Represents a value that can be either Some (value) or None.<br>
 * Similar to Rust's Option type, aims to replace nullable types.
 *
 * @param <T> The type of the value contained in this option
 */
@Kapi
@NullMarked
public class Option<T> {
    
    private static final Option<?> NONE = new Option<>(null);
    
    private final @Nullable T value;
    
    private Option(@Nullable T value) {
        this.value = value;
    }
    
    /**
     * @param value The value contained in this option
     * @param <T>   The type of the value contained in this option
     * @return {@link #some(Object)} if the value is not null, and {@link #none()} if it is null
     * @see #some(Object)
     * @see #none()
     */
    @Kapi
    @SuppressWarnings("unchecked")
    public static <T> Option<T> of(@Nullable T value) {
        if (value == null) return (Option<T>) NONE;
        return new Option<>(value);
    }
    
    /**
     * Creates a new Option containing the given Non-null value.<br>
     * See also {@link #of(Object)} for a version that accepts null values.
     *
     * @param value The value contained in this option
     * @param <T>   The type of the value contained in this option
     * @return A new Option containing the value
     * @see #none()
     */
    @Kapi
    public static <T> Option<T> some(T value) {
        return new Option<>(value);
    }
    
    /**
     * @param <T> The type of the value contained in this option
     * @return The None option, can be thought of as a null value
     * @see #some(Object)
     * @see #of(Object)
     */
    @Kapi
    @SuppressWarnings("unchecked")
    public static <T> Option<T> none() {
        return (Option<T>) NONE;
    }
    
    /**
     * @return True if this option is None, false if it's Some
     * @see #isSome()
     */
    @Kapi
    public boolean isNone() {
        return value == null;
    }
    
    /**
     * @return True if this option is Some, false if it's None
     * @see #isNone()
     */
    @Kapi
    public boolean isSome() {
        return value != null;
    }
    
    /**
     * @param predicate The predicate to test the value with
     * @return True if the value is Some and the predicate returns true, false otherwise
     */
    @Kapi
    public boolean isSomeAnd(Predicate<T> predicate) {
        return value != null && predicate.test(value);
    }
    
    /**
     * Gets the Some value contained in this option.<br>
     * <br>
     * Because this function may throw a NullSafetyException,
     * its use is generally discouraged.
     * Instead, prefer calling {@link #unwrapOr(Object)}, or {@link #unwrapOrElse(Supplier)},
     * or use {@link #get()} and handle the null value yourself.
     *
     * @return The value contained in this option
     * @throws NullSafetyException If this option is None
     * @see #expect(String)
     */
    @Kapi
    public T unwrap() {
        if (value == null) {
            throw new NullSafetyException("Called unwrap on a None option");
        }
        return value;
    }
    
    /**
     * @param def The default value to return if this option is None
     * @return The value contained in this option, or the default value if it is None
     */
    @Kapi
    public T unwrapOr(T def) {
        return value != null ? value : def;
    }
    
    /**
     * @param supplier The supplier to get the default value from if this option is None
     * @return The value contained in this option, or the supplied value if it is a None
     */
    @Kapi
    public T unwrapOrElse(Supplier<T> supplier) {
        return value != null ? value : supplier.get();
    }
    
    /**
     * Gets the Some value contained in this option.<br>
     * <br>
     * Because this function may throw a NullSafetyException,
     * its use is generally discouraged.
     * Instead, prefer calling {@link #unwrapOr(Object)}, or {@link #unwrapOrElse(Supplier)},
     * or use {@link #get()} and handle the null value yourself.
     *
     * @param message The message the exception will have if this option is None
     * @return The value contained in this option
     * @throws NullSafetyException If this option is None
     * @see #unwrap()
     */
    @Kapi
    public T expect(String message) {
        if (value == null) {
            throw new NullSafetyException(message);
        }
        return value;
    }
    
    /**
     * Maps the current value to a different type.<br>
     * Does nothing if this option is None.
     *
     * @param mapper The mapper to apply to the value contained in this option
     * @param <U>    The type of the value returned by the mapper
     * @return A new option with the mapped value, or None if this option is None
     * @see #mapOr(Function, Object)
     * @see #mapOrElse(Function, Supplier)
     */
    @Kapi
    public <U> Option<U> map(Function<T,U> mapper) {
        return value != null ? Option.some(mapper.apply(value)) : Option.none();
    }
    
    /**
     * Maps the current value to a different type.<br>
     * Returns the default value if this option is None.
     *
     * @param mapper The mapper to apply to the value contained in this option
     * @param def    The default value to return if this option is None
     * @param <U>    The type of the value returned by the mapper
     * @return A new option with the mapped value, or the default value if this option is None
     * @see #map(Function)
     * @see #mapOrElse(Function, Supplier)
     */
    @Kapi
    public <U> Option<U> mapOr(Function<T,U> mapper, U def) {
        return value != null ? Option.some(mapper.apply(value)) : Option.some(def);
    }
    
    /**
     * Maps the current value to a different type.<br>
     * Returns the value supplied by the supplier if this option is None.
     *
     * @param mapper   The mapper to apply to the value contained in this option
     * @param supplier The supplier to get the default value from if this option is None
     * @param <U>      The type of the value returned by the mapper
     * @return A new option with the mapped value, or the default supplied value if this option is None
     */
    @Kapi
    public <U> Option<U> mapOrElse(Function<T,U> mapper, Supplier<U> supplier) {
        return value != null ? Option.some(mapper.apply(value)) : Option.some(supplier.get());
    }
    
    /**
     * Arguments passed to <code>and</code> are eagerly evaluated.<br>
     * if you are passing the result of a function call,
     * it's recommended to use {@link #andThen(Function)} which is lazily evaluated.
     *
     * @param optionB The option to apply the function to if this option is Some
     * @param <U>     The type of the value returned by the function
     * @return None if this option is None, otherwise returns optionB
     */
    @Kapi
    public <U> Option<U> and(Option<U> optionB) {
        return value != null ? optionB : Option.none();
    }
    
    /**
     * Some languages call this operation <code>flatMap</code>.
     *
     * @param mapper The mapper to apply to the value contained in this option
     * @param <U>    The type of the value returned by the mapper
     * @return None if this option is None,
     *         otherwise calls the mapper with the wrapped value and returns the result
     */
    @Kapi
    public <U> Option<U> andThen(Function<T,Option<U>> mapper) {
        return value != null ? mapper.apply(value) : Option.none();
    }
    
    /**
     * Tests if the value contained in this option matches the predicate.<br>
     * Returns None if this option is None or if the predicate returns false,
     * otherwise returns Some.
     *
     * @param predicate The predicate to test the value with
     * @return Some if this option is Some and the predicate returns true, None otherwise
     */
    @Kapi
    public Option<T> filter(Predicate<T> predicate) {
        return value != null && predicate.test(value) ? Option.some(value) : Option.none();
    }
    
    /**
     * @return The value contained in this option if Some, or null if None
     */
    @Kapi
    public @Nullable T get() {
        return value;
    }
    
    /**
     * Inspects the value contained in this option if it's Some.<br>
     * If this option is None, the consumer is not called.
     *
     * @param consumer The consumer to call if this option is Some
     * @return This option (for chaining)
     * @see #ifSome(Consumer)
     * @see #ifNone(Runnable)
     */
    @Kapi
    public Option<T> inspect(Consumer<T> consumer) {
        if (value != null) consumer.accept(value);
        return this;
    }
    
    /**
     * Converts this option to a Result type.
     *
     * @param err   The error to return if this option is None
     * @param <Err> The type of the error
     * @return An Ok result if this option is Some, or an Err result if this option is None
     */
    @Kapi
    public <Err> Result<T,Err> okOr(Err err) {
        return value != null ? Result.ok(value) : Result.err(err);
    }
    
    /**
     * Converts this option to a Result type.
     *
     * @param supplier The supplier to get the error from if this option is None
     * @param <Err>    The type of the error
     * @return An Ok result if this option is Some, or an Err result if this option is None
     */
    @Kapi
    public <Err> Result<T,Err> okOrElse(Supplier<Err> supplier) {
        return value != null ? Result.ok(value) : Result.err(supplier.get());
    }
    
    /**
     * @param optionB The option to return if this option is None
     * @return This option if it's Some, or the other option if it's None
     */
    @Kapi
    public Option<T> or(Option<T> optionB) {
        return value != null ? this : optionB;
    }
    
    /**
     * @param supplier The supplier for the other option if this option is None
     * @return This option if it's Some, or the supplied option if it's None
     */
    @Kapi
    public Option<T> orElse(Supplier<Option<T>> supplier) {
        return value != null ? this : supplier.get();
    }
    
    /**
     * @param consumer The consumer to call if this option is Some
     */
    @Kapi
    public void ifSome(Consumer<T> consumer) {
        if (value != null) consumer.accept(value);
    }
    
    /**
     * @param runnable The runnable to call if this option is None
     */
    @Kapi
    public void ifNone(Runnable runnable) {
        if (value == null) runnable.run();
    }
    
    @Kapi
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
    
    @Kapi
    @Override
    @Contract(pure = true, value = "null -> false")
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Option<?> option = (Option<?>) obj;
        return Objects.equals(value, option.value);
    }
    
    @Kapi
    @Override
    public String toString() {
        return value != null ? "Some(" + value + ")" : "None";
    }
}
