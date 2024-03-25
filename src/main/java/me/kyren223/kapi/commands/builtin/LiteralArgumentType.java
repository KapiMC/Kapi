package me.kyren223.kapi.commands.builtin;

import me.kyren223.kapi.commands.ArgumentType;
import me.kyren223.kapi.commands.SuggestionCommandContext;
import me.kyren223.kapi.data.Result;

public class LiteralArgumentType implements ArgumentType<String> {
    
    private final String literal;
    private boolean ignoreCase;
    private boolean suggest;
    
    private LiteralArgumentType(String literal) {
        this.literal = literal;
        this.ignoreCase = false;
    }
    
    public static LiteralArgumentType literal(String literal) {
        return new LiteralArgumentType(literal);
    }
    
    @Override
    public Result<String, String> parse(String input) {
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
    
    @Override
    public void autoSuggest() {
        suggest = true;
    }
    
    @Override
    public void getSuggestions(SuggestionCommandContext context) {
        if (!suggest) return;
        context.getReturnValue().add(literal);
    }
    
    public LiteralArgumentType ignoreCase() {
        this.ignoreCase = true;
        return this;
    }
}
