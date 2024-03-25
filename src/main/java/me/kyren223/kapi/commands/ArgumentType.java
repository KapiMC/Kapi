package me.kyren223.kapi.commands;

import me.kyren223.kapi.data.Result;

public interface ArgumentType<T> {
    Result<T, String> parse(String input);
    void autoSuggest();
    void getSuggestions(SuggestionCommandContext context);
}
