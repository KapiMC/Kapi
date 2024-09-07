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
 * Represents a command argument type for a boolean.
 */
@Kapi
@ApiStatus.Experimental
public class BooleanArgumentType implements ArgumentType<Boolean> {
    
    private boolean suggest;
    
    private BooleanArgumentType() {
        this.suggest = false;
    }
    
    /**
     * Creates a new boolean argument type.
     *
     * @return the boolean argument type
     */
    @Kapi
    public static BooleanArgumentType bool() {
        return new BooleanArgumentType();
    }
    
    
    @Kapi
    @Override
    public Result<Boolean,String> parse(List<String> arguments) {
        String input = arguments.remove(0);
        if (input.equalsIgnoreCase("true")) {
            return Result.ok(true);
        } else if (input.equalsIgnoreCase("false")) {
            return Result.ok(false);
        } else {
            return Result.err("Invalid boolean: " + input);
        }
    }
    
    /**
     * Adds suggestions for "true" and "false".
     */
    @Kapi
    public void autoSuggest() {
        suggest = true;
    }
    
    @Kapi
    @Override
    public void getSuggestions(SuggestionCommandContext context) {
        if (suggest) {
            context.addSuggestion("true");
            context.addSuggestion("false");
        }
    }
}
