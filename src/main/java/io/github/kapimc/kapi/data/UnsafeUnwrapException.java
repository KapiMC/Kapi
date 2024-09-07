/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.data;

import io.github.kapimc.kapi.annotations.Kapi;

/**
 * Thrown when an unsafe unwrap operation is performed.
 * <p>
 * This exception is used in scenarios where methods such as {@code unwrap()} or {@code expect(String)}
 * are called on {@link Result Result&lt;T, E&gt;} or {@link Option Option&lt;T&gt;} types and the operation is deemed
 * unsafe.
 * <p>
 * Example:
 * <pre> {@code
 * Result<String, String> result = ...; // some result
 * String value = result.unwrap(); // throws UnsafeUnwrapException if result is Err
 * } </pre>
 */
@Kapi
public final class UnsafeUnwrapException extends RuntimeException {
    
    /**
     * Creates a new UnsafeUnwrapException with the given message.
     *
     * @param message the message of the exception
     */
    @Kapi
    public UnsafeUnwrapException(String message) {
        super(message);
    }
    
}
