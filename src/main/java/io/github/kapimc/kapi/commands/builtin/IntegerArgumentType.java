/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.commands.builtin;

import io.github.kapimc.kapi.commands.ArgumentType;
import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.commands.SuggestionCommandContext;
import io.github.kapimc.kapi.data.Result;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

/**
 * Represents a command argument type for an integer.
 */
@Kapi
@ApiStatus.Experimental
public class IntegerArgumentType implements ArgumentType<Integer> {
    
    private boolean suggest;
    private int min;
    private int max;
    private @Nullable Predicate<Integer> predicate;
    private String errorMessage;
    
    private IntegerArgumentType() {
        this.suggest = false;
        this.min = Integer.MIN_VALUE;
        this.max = Integer.MAX_VALUE;
        this.predicate = null;
        this.errorMessage = "Integer %d is invalid";
    }
    
    /**
     * Creates a new integer argument type.
     *
     * @return the integer argument type
     */
    @Kapi
    public static IntegerArgumentType integer() {
        return new IntegerArgumentType();
    }
    
    /**
     * Sets the minimum value (inclusive) for the integer.
     *
     * @param min the minimum value
     * @return this, for chaining
     */
    @Kapi
    public IntegerArgumentType min(int min) {
        this.min = min;
        return this;
    }
    
    /**
     * Sets the maximum value (inclusive) for the integer.
     *
     * @param max the maximum value
     * @return this, for chaining
     */
    @Kapi
    public IntegerArgumentType max(int max) {
        this.max = max;
        return this;
    }
    
    /**
     * Sets the range for the integer (inclusive).
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return this, for chaining
     */
    @Kapi
    public IntegerArgumentType range(int min, int max) {
        this.min = min;
        this.max = max;
        return this;
    }
    
    /**
     * Sets the predicate for the integer.<br>
     * The predicate should return true if the integer is valid, false otherwise.<br>
     * See {@link #predicate(Predicate, String)} for a version with an error message.
     *
     * @param predicate the predicate
     * @return this, for chaining
     */
    @Kapi
    public IntegerArgumentType predicate(Predicate<Integer> predicate) {
        this.predicate = predicate;
        return this;
    }
    
    /**
     * Sets the predicate for the integer.<br>
     * The predicate should return true if the integer is valid, false otherwise.<br>
     * See {@link #predicate(Predicate)} for a version without an error message.
     *
     * @param predicate    the predicate
     * @param errorMessage the error message if the predicate fails, use "%d" as a placeholder for the integer
     * @return this, for chaining
     */
    @Kapi
    public IntegerArgumentType predicate(Predicate<Integer> predicate, @NotNull String errorMessage) {
        this.predicate = predicate;
        this.errorMessage = errorMessage;
        return this;
    }
    
    @Kapi
    @Override
    public Result<Integer,String> parse(List<String> arguments) {
        String input = arguments.remove(0);
        int output;
        try {
            output = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return Result.err("Invalid integer: " + input);
        }
        
        if (output < min) {
            return Result.err(String.format("Integer %d is less than the minimum value %d", output, min));
        }
        
        if (output > max) {
            return Result.err(String.format("Integer %d is greater than the maximum value %d", output, max));
        }
        
        if (predicate != null && !predicate.test(output)) {
            return Result.err(String.format(errorMessage, output));
        }
        
        return Result.ok(output);
    }
    
    /**
     * Automatically suggests options for the integer.<br>
     * The suggestions will be all integers within the range that pass the predicate.
     */
    @Kapi
    public void autoSuggest() {
        this.suggest = true;
    }
    
    @Kapi
    @Override
    public void getSuggestions(SuggestionCommandContext context) {
        if (!suggest) return;
        for (int i = min; i <= max; i++) {
            if (predicate != null && !predicate.test(i)) continue;
            context.addSuggestion(String.valueOf(i));
        }
    }
    
}
