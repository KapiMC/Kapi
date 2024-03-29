package me.kyren223.kapi.commands;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.data.Result;

/**
 * Represents a Command Argument Type.
 * @param <T> The java type of the argument.
 */
@Kapi
public interface ArgumentType<T> {
    /**
     * @param input An argument string.
     * @return A Result containing the parsed argument or an error message.
     */
    @Kapi
    Result<T, String> parse(String input);
    
    /**
     * Should NOT be called by the user.<br>
     * <br>
     * This should only be implemented by classes that implement {@link ArgumentType}.<br>
     * To add a suggestion value, you should call the
     * {@link SuggestionCommandContext#addSuggestion(String)} method.<br>
     *
     * @param context The context of the suggestion.
     */
    @Kapi
    void getSuggestions(SuggestionCommandContext context);
}
