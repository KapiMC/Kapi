/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.annotations.SubCommand;
import io.github.kapimc.kapi.commands.builtin.ListArgumentParser;
import io.github.kapimc.kapi.data.Option;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.lang.reflect.AnnotatedType;
import java.util.Deque;
import java.util.List;

/**
 * An interface for defining parsers to command arguments.
 * <p>
 * Parers are registered in the {@link ArgumentRegistry}.
 * The registry allows looking up parsers for specific types.
 * This is the logic used to determine how to parse a {@link SubCommand}
 * method's parameter from a string.
 * <p>
 * This class is also responsible for other aspects related to parsing arguments,
 * such as suggestions and representations.
 *
 * @param <T> the type of the argument
 */
@Kapi
public interface ArgumentParser<T> {
    
    /**
     * Parses the given argument.
     * <p>
     * For those who wish to implement their own parsers,
     * the following information may be useful:
     * <p>
     * The type may be useful for getting annotations from the type,
     * this can be used to add a special annotation such as
     * {@code @Range(min=0, max=100)} to the argument, and then
     * this parser can use the annotation to add some additional logic.
     * Another reason is for generic or array types, where you may need to get
     * the "inner" type of the array or generic class and potentially even
     * the annotations of the inner type.
     * <p>
     * The sender may be used in certain cases, for example,
     * a {@link Location} argument may need to get the world from the sender,
     * or alternatively, to support relative coordinates such as {@code ~10},
     * the sender's location may be necessary.
     * Note that although not strictly prohibited, you should never
     * modify the sender in any way, as usage of this method may be called
     * in places where you wouldn't expect, or even called multiple times.
     * <p>
     * The arguments deque should be used to pop the strings that have been parsed,
     * in most cases this will only be the first string in the deque.
     * But you may pop any number of strings, for example, when parsing a list of arguments
     * like what {@link ListArgumentParser} does.
     * This deque is safe to modify even when parsing fails and a None is returned,
     * this is due to the deque always being a copy of the original deque.
     * Popping the last string from the deque, although possible, is not recommended,
     * there shouldn't be any reason to do so, and it may cause unexpected behavior.
     * <p>
     * The return value should be either a Some option containing the parsed argument,
     * or a None if the argument can't be parsed.
     *
     * @param type      the type of the argument
     * @param paramName the name of the parameter that is being parsed
     * @param sender    the sender of the command
     * @param args      the arguments of the command
     * @return the parsed argument or None if the arguments can't be parsed
     */
    @Kapi
    Option<T> parse(AnnotatedType type, String paramName, CommandSender sender, Deque<String> args);
    
    /**
     * Gets suggestions for the argument.
     * This is used for tab completion, and is called when the user presses tab.
     * <p>
     * For more detailed information on each parameter of this method,
     * see {@link #parse(AnnotatedType, String, CommandSender, Deque)}'s
     * documentation.
     *
     * @param type      the type of the argument
     * @param paramName the name of the parameter that is being parsed
     * @param sender    the sender of the command
     * @return a list of suggestions for the argument (can be empty)
     */
    @Kapi
    List<String> getSuggestions(AnnotatedType type, String paramName, CommandSender sender);
    
    /**
     * Gets the priority of the argument.
     * <p>
     * Recommended priority guidelines:
     * <ul>
     *     <li>0-10000 for built-in java types
     *     <li>20000-30000 for Minecraft/Spigot API types
     *     <li>40000-50000 for Kapi types
     *     <li>100000+ for custom user-defined types (such as your own classes)
     * </ul>
     * For generic types such as lists, pairs, options, etc. it's recommended to use
     * the priority of the inner type/s + some modifier.
     * For example, the priority of a list is determined by the inner type minus 100.
     * <p>
     * The annotated type may be useful in cases where
     * the priority may be determined by an annotation on the type,
     * or to access the inner type of a generic type.
     *
     * @param type the type of the argument
     * @return the priority of the argument
     */
    int getPriority(AnnotatedType type);
    
    /**
     * Gets the representation of the argument.
     * <p>
     * This information may be used for displaying information about each
     * subcommand method.
     * For example, in cases such as the user typing something
     * that matches no subcommand method, a list of possible subcommands
     * may be shown, where each parameter of the subcommand method is represented
     * by this representation.
     * <p>
     * The convention is to use '&lt;' and '&gt;' for the prefix and suffix of the representation
     * if the argument is "required" or '[' and ']' if it is "optional".
     *
     * @param type      the type of the argument
     * @param paramName the name of the parameter that is being
     * @return the representation of the argument, or None for no representation
     * @see ArgumentRepresentation
     */
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
    @Kapi
    default boolean isParseableOnFailure() {
        return false;
    }
    
}
