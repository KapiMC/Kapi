/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands.builtin;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.annotations.Literal;
import io.github.kapimc.kapi.commands.ArgumentParser;
import io.github.kapimc.kapi.commands.ArgumentRepresentation;
import io.github.kapimc.kapi.data.Option;
import org.bukkit.command.CommandSender;

import java.lang.reflect.AnnotatedType;
import java.util.Deque;
import java.util.List;

/**
 * Argument parser for String object.
 */
@Kapi
public class StringArgumentParser implements ArgumentParser<String> {
    
    /**
     * The priority of this parser.
     */
    @Kapi
    public static final int PRIORITY = 4000;
    
    /**
     * The singleton instance of this parser.
     */
    @Kapi
    public static final StringArgumentParser INSTANCE = new StringArgumentParser();
    
    private StringArgumentParser() {
    }
    
    @Override
    public Option<String> parse(AnnotatedType type, CommandSender sender, Deque<String> args) {
        String arg = args.pollFirst();
        if (type.isAnnotationPresent(Literal.class)) {
            Literal literal = type.getAnnotation(Literal.class);
            if (literal.caseSensitive()) {
                for (String alias : literal.aliases()) {
                    if (!alias.equals(arg)) {
                        return Option.none();
                    }
                }
                if (!literal.value().equals(arg)) {
                    return Option.none();
                }
            } else {
                for (String alias : literal.aliases()) {
                    if (!alias.equalsIgnoreCase(arg)) {
                        return Option.none();
                    }
                }
                if (!literal.value().equalsIgnoreCase(arg)) {
                    return Option.none();
                }
            }
        }
        return Option.of(arg);
    }
    
    @Override
    public List<String> getSuggestions(AnnotatedType type, CommandSender sender) {
        if (type.isAnnotationPresent(Literal.class)) {
            return List.of(type.getAnnotation(Literal.class).value());
        }
        return List.of();
    }
    
    @Override
    public int getPriority(AnnotatedType type) {
        if (type.isAnnotationPresent(Literal.class)) {
            return Integer.MAX_VALUE - 100;
        }
        return PRIORITY;
    }
    
    @Override
    public Option<ArgumentRepresentation> getRepresentation(AnnotatedType type) {
        if (type.isAnnotationPresent(Literal.class)) {
            return Option.some(ArgumentRepresentation.of(type.getAnnotation(Literal.class).value()));
        }
        return Option.some(ArgumentRepresentation.of("<", "text", ">"));
    }
    
}
