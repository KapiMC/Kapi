/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands;

import io.github.kapimc.kapi.data.Option;
import org.bukkit.command.CommandSender;

import java.lang.reflect.AnnotatedType;
import java.util.Deque;
import java.util.List;

public interface ArgumentParser<T> {
    
    Option<T> parse(AnnotatedType type, String paramName, CommandSender sender, Deque<String> args);
    
    List<String> getSuggestions(AnnotatedType type, String paramName, CommandSender sender);
    
    int getPriority(AnnotatedType type);
    
    Option<ArgumentRepresentation> getRepresentation(AnnotatedType type, String paramName);
    
    /**
     * Whether this parser can parse the given type on failure.
     * <p>
     * Parseable on failure means that the parser may sometimes output a Some option
     * even when the given input is invalid.
     * <p>
     * For example, arrays and lists can be empty, so the moment the input is not a valid
     * array or list of the given type, the parser will still output a Some option.
     * Another example is an {@link Option} type, which will always output a Some option
     * whether the input is valid or not.
     *
     * @return true if it is parseable on failure, otherwise false
     */
    default boolean isParseableOnFailure() {
        return false;
    }
    
}
