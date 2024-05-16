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
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

/**
 * A simple result type that can be either an Ok or an Err.<br>
 * Inspired by Rust's Result type.
 *
 * @param <Ok>  The type of the Ok value
 * @param <Err> The type of the Err value
 */
@Kapi
@NullMarked
// TODO Finish Result
public class Result<Ok, Err> {
    
    private final @Nullable Ok ok;
    private final @Nullable Err err;
    
    private Result(@Nullable Ok ok, @Nullable Err err) {
        this.ok = ok;
        this.err = err;
    }
    
    /**
     * Returns a result that stores an Ok value.
     *
     * @param ok    The Ok value
     * @param <Ok>  The type of the Ok value
     * @param <Err> The type of the Err value
     * @return A result that stores an Ok value
     */
    @Kapi
    public static <Ok, Err> Result<Ok,Err> ok(Ok ok) {
        return new Result<>(ok, null);
    }
    
    /**
     * Returns a result that stores an Err value.
     *
     * @param err   The Err value
     * @param <Ok>  The type of the Ok value
     * @param <Err> The type of the Err value
     * @return A result that stores an Err value
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
     * @return True if this result is an Err, false otherwise
     */
    @Kapi
    public boolean isErr() {
        return ok == null;
    }
    
    /**
     * @return The Ok value or null if this result is an Err
     */
    @Kapi
    public @Nullable Ok getOkOrNull() {
        return ok;
    }
    
    /**
     * @return The Err value or null if this result is an Ok
     */
    @Kapi
    public @Nullable Err getErrOrNull() {
        return err;
    }
    
    /**
     * Returns the Ok value or throws an exception if this result is an Err.
     *
     * @return The Ok value
     * @throws NullSafetyException If this result is an Err
     */
    @Kapi
    public Ok unwrap() {
        if (ok != null) return ok;
        throw new NullSafetyException("Called unwrap on an Err result");
    }
    
    /**
     * Returns the Ok value or a default value if this result is an Err.
     *
     * @param def The default value
     * @return The Ok value or the default value if this result is an Err
     */
    @Kapi
    public Ok unwrapOr(Ok def) {
        return ok != null ? ok : def;
    }
    
    /**
     * Returns the Ok value or a default value if this result is an Err.
     *
     * @param def The default value
     * @return The Ok value or the default value if this result is an Err
     */
    @Kapi
    public @Nullable Ok unwrapOrNullable(@Nullable Ok def) {
        return ok != null ? ok : def;
    }
    
    
    /**
     * Returns the Ok value or a value produced by a supplier if this result is an Err.
     *
     * @param supplier The supplier
     * @return The Ok value or a value produced by the supplier if this result is an Err
     */
    @Kapi
    public Ok unwrapOrGet(Supplier<Ok> supplier) {
        return ok != null ? ok : supplier.get();
    }
    
    /**
     * Returns the Ok value or throws an exception with a custom message if this result is an Err.
     *
     * @param message The message that will be thrown
     * @return The Ok value
     * @throws NullSafetyException If this result is an Err
     */
    @Kapi
    public Ok expect(String message) {
        if (ok != null) return ok;
        throw new NullSafetyException(message);
    }
    
    /**
     * Returns the Ok value or throws the Err value if this result is an Err.<br>
     * Error value must be a RuntimeException or a subclass of RuntimeException.
     *
     * @return The Ok value
     * @throws RuntimeException    If this result is an Err and the Err value is a RuntimeException
     * @throws NullSafetyException If this result is an Err and the Err value is not a RuntimeException
     */
    @Kapi
    public Ok unwrapOrThrow() {
        if (ok != null) return ok;
        if (err instanceof RuntimeException exception) {
            throw exception;
        }
        throw new NullSafetyException("Called unwrapOrThrow on an Err result that is not a RuntimeException");
    }
}
