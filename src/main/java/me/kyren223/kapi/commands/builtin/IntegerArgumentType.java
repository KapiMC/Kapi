package me.kyren223.kapi.commands.builtin;

import me.kyren223.kapi.commands.ArgumentType;
import me.kyren223.kapi.commands.SuggestionCommandContext;
import me.kyren223.kapi.data.Result;

public class IntegerArgumentType implements ArgumentType<Integer> {
    
    @Override
    public Result<Integer, String> parse(String input) {
        try {
            return Result.ok(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            return Result.err("Invalid integer: " + input);
        }
    }
    
    @Override
    public void autoSuggest() {
        // Implement this
    }
    
    @Override
    public void getSuggestions(SuggestionCommandContext context) {
        // Implement this
    }
    
}
