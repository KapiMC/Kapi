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
import io.github.kapimc.kapi.utility.Utils;
import org.bukkit.command.CommandSender;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Parameter;
import java.util.Deque;
import java.util.List;

/**
 * Argument parser for both primitive boolean and Boolean object.
 */
@Kapi
public class BooleanArgumentParser implements ArgumentParser<Boolean> {
    
    /**
     * The priority of this parser.
     */
    @Kapi
    public static final int PRIORITY = 5000;
    
    /**
     * The singleton instance of this parser.
     */
    @Kapi
    public static final BooleanArgumentParser INSTANCE = new BooleanArgumentParser();
    
    private BooleanArgumentParser() {
    }
    
    @Override
    public Option<Boolean> parse(Deque<String> args, CommandSender sender, AnnotatedType type) {
        if (args.peek() == null) {
            return Option.none();
        }
        if (args.peek().equalsIgnoreCase("true")) {
            return Option.some(true);
        } else if (args.peek().equalsIgnoreCase("false")) {
            return Option.some(false);
        }
        return Option.none();
    }
    
    @Override
    public List<String> getSuggestions(Deque<String> args, CommandSender sender, AnnotatedType type) {
        return List.of("true", "false");
    }
    
    @Override
    public int getPriority(AnnotatedType type) {
        return PRIORITY;
    }
    
    @Override
    public Option<String> getRepresentation(Parameter parameter) {
        return Option.some("true|false");
    }
}
