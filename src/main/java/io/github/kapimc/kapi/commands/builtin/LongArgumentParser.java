/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands.builtin;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.commands.ArgumentParser;
import io.github.kapimc.kapi.commands.ArgumentRepresentation;
import io.github.kapimc.kapi.data.Option;
import io.github.kapimc.kapi.utility.Utils;
import org.bukkit.command.CommandSender;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Parameter;
import java.util.Deque;
import java.util.List;

/**
 * Argument parser for both primitive long and Long object.
 */
@Kapi
public class LongArgumentParser implements ArgumentParser<Long> {
    
    /**
     * The priority of this parser.
     */
    @Kapi
    public static final int PRIORITY = 8000;
    
    /**
     * The singleton instance of this parser.
     */
    @Kapi
    public static final LongArgumentParser INSTANCE = new LongArgumentParser();
    
    private LongArgumentParser() {
    }
    
    @Override
    public Option<Long> parse(AnnotatedType type, String paramName, Deque<String> args, CommandSender sender) {
        return Option.of(args.peek()).andThen(Utils::parseLong).inspect(ignored -> args.pop());
    }
    
    @Override
    public List<String> getSuggestions(
        AnnotatedType type, String paramName, Deque<String> args, CommandSender sender
    ) {
        return List.of();
    }
    
    @Override
    public int getPriority(AnnotatedType type) {
        return PRIORITY;
    }
    
    @Override
    public Option<ArgumentRepresentation> getRepresentation(AnnotatedType type, String paramName) {
        return Option.some(ArgumentRepresentation.of("<", "integer", ">"));
    }
}
