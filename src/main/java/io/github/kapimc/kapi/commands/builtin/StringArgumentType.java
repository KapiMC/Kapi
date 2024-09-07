/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.commands.builtin;

import io.github.kapimc.kapi.commands.ArgumentType;
import io.github.kapimc.kapi.commands.SuggestionCommandContext;
import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.data.Result;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

/**
 * Represents a command argument type for a string.
 */
@Kapi
@ApiStatus.Experimental
public class StringArgumentType implements ArgumentType<String> {
    
    private boolean toLowerCase;
    private boolean toUpperCase;
    
    private StringArgumentType() {
        this.toLowerCase = false;
        this.toUpperCase = false;
    }
    
    /**
     * Creates a new string argument type.
     *
     * @return the string argument type
     */
    @Kapi
    public static StringArgumentType string() {
        return new StringArgumentType();
    }
    
    /**
     * Converts the input to lowercase.
     *
     * @return this, for chaining
     * @throws IllegalStateException if already converting to uppercase
     */
    @Kapi
    public StringArgumentType toLowerCase() {
        if (toUpperCase) {
            throw new IllegalStateException("Cannot convert to lower case if already converting to upper case.");
        }
        this.toLowerCase = true;
        return this;
    }
    
    /**
     * Converts the input to uppercase.
     *
     * @return this, for chaining
     * @throws IllegalStateException if already converting to lowercase
     */
    @Kapi
    public StringArgumentType toUpperCase() {
        if (toLowerCase) {
            throw new IllegalStateException("Cannot convert to upper case if already converting to lower case.");
        }
        this.toUpperCase = true;
        return this;
    }
    
    @Kapi
    @Override
    public Result<String,String> parse(List<String> arguments) {
        String input = arguments.remove(0);
        String output;
        if (toLowerCase) {
            output = input.toLowerCase();
        } else if (toUpperCase) {
            output = input.toUpperCase();
        } else {
            output = input;
        }
        return Result.ok(output);
    }
    
    @Kapi
    @Override
    public void getSuggestions(SuggestionCommandContext context) {
        // Does nothing
    }
}
