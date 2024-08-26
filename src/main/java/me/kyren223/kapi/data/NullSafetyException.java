/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package me.kyren223.kapi.data;

import me.kyren223.kapi.annotations.Kapi;
import org.jspecify.annotations.NullMarked;

/**
 * Used in {@link Result} and {@link Option},
 * this exception is thrown when the assumption that a value is not null is violated.<br>
 * For example, in {@link Result#unwrap()} when the result is Err.
 */
@Kapi
@NullMarked
public class NullSafetyException extends RuntimeException {
    
    public NullSafetyException(String message) {
        super(message);
    }
    
    public static NullSafetyException dueTo(String message) {
        return new NullSafetyException(message);
    }
    
    public static void throwDueTo(String message) {
        throw dueTo(message);
    }
    
}
