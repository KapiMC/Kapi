/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package me.kyren223.kapi.commands.builtin;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.commands.ArgumentType;
import me.kyren223.kapi.commands.SuggestionCommandContext;
import me.kyren223.kapi.data.Result;
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
