/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package me.kyren223.kapi.commands;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.data.Result;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

/**
 * Represents a Command Argument Type.
 *
 * @param <T> The java type of the argument (cannot be null).
 */
@Kapi
@ApiStatus.Experimental
public interface ArgumentType<T> {
    /**
     * For classes that implement {@link ArgumentType}, this method parses the argument.<br>
     *
     * @param arguments The list of command arguments that haven't been parsed yet<br>
     *                  A list is being used because the argument may be a multi-word string.<br>
     *                  Guaranteed to have at least one element.<br>
     *                  <br>
     *                  Note: This is a mutable list, you should remove all the arguments you've parsed.<br>
     *                  Removing the parsed arguments ensures that the next argument type
     *                  won't parse the same arguments.<br>
     * @return A Result containing the parsed argument or an error message.
     */
    @Kapi
    Result<T,String> parse(List<String> arguments);
    
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
