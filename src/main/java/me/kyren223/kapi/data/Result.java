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

import com.sun.jdi.InvalidTypeException;
import me.kyren223.kapi.annotations.Kapi;

import java.util.function.Function;
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
    
    /**
     * Returns the Ok value or throws an exception produced by a supplier if this result is an Err.
     *
     * @param exceptionSupplier The exception supplier (takes the Err value and returns a Throwable)
     * @return The Ok value
     * @throws Throwable If this result is an Err
     */
    @Kapi
    public Ok unwrapOrThrow(Function<Err, Throwable> exceptionSupplier) throws Throwable {
        if (isOk()) return ok;
        throw exceptionSupplier.apply(err);
    }
    
    /**
     * Returns the Ok value or throws the Err value if this result is an Err.
     *
     * @return The Ok value
     * @throws Throwable If this result is an Err<br>
     *                   If the Err value is a Throwable, it will be thrown<br>
     *                   If the Err value is not a Throwable, an InvalidTypeException will be thrown
     */
    @Kapi
    public Ok unwrapOrThrow() throws Throwable {
        if (isOk()) return ok;
        if (err instanceof Throwable exception) {
            throw exception;
        }
        throw new InvalidTypeException("Called unwrapOrThrow on an Err result that is not Throwable");
    }
}
