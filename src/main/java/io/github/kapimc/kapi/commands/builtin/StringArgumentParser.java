/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands.builtin;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.commands.ArgumentParser;
import io.github.kapimc.kapi.data.Option;
import org.bukkit.command.CommandSender;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Parameter;
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
    public Option<String> parse(Deque<String> args, CommandSender sender, AnnotatedType type) {
        return Option.of(args.pollFirst());
    }
    
    @Override
    public List<String> getSuggestions(Deque<String> args, CommandSender sender, AnnotatedType type) {
        return List.of();
    }
    
    @Override
    public int getPriority(AnnotatedType type) {
        return PRIORITY;
    }
    
    @Override
    public Option<String> getRepresentation(Parameter parameter) {
        return Option.some("text");
    }
}
