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
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * Represents a command argument type for a literal.
 */
@Kapi
@ApiStatus.Experimental
public class LiteralArgumentType implements ArgumentType<String> {
    
    private final String literal;
    private boolean ignoreCase;
    private boolean suggest;
    
    private LiteralArgumentType(String literal) {
        this.literal = literal;
        this.ignoreCase = false;
    }
    
    /**
     * Creates a new literal argument type.
     *
     * @param literal the literal
     * @return the literal argument type
     */
    @Kapi
    public static LiteralArgumentType literal(String literal) {
        return new LiteralArgumentType(literal);
    }
    
    /**
     * Ignores the case of the input.
     *
     * @return this, for chaining
     */
    @Kapi
    public LiteralArgumentType ignoreCase() {
        this.ignoreCase = true;
        return this;
    }
    
    @Kapi
    @Override
    public Result<String,String> parse(List<String> arguments) {
        String input = arguments.remove(0);
        if (ignoreCase) {
            if (input.equalsIgnoreCase(literal)) {
                return Result.ok(literal);
            } else {
                return Result.err("Invalid literal: " + input);
            }
        } else {
            if (input.equals(literal)) {
                return Result.ok(literal);
            } else {
                return Result.err("Invalid literal: " + input);
            }
        }
    }
    
    /**
     * Adds the literal as a suggestion.
     */
    @Kapi
    public void autoSuggest() {
        suggest = true;
    }
    
    @Kapi
    @Override
    public void getSuggestions(SuggestionCommandContext context) {
        if (!suggest) return;
        context.addSuggestion(literal);
    }
    
}
