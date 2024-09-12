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
import java.util.Deque;
import java.util.List;

/**
 * Argument parser for both primitive float and Float object.
 */
@Kapi
public class FloatArgumentParser implements ArgumentParser<Float> {
    
    /**
     * The priority of this parser.
     */
    @Kapi
    public static final int PRIORITY = 7000;
    
    /**
     * The singleton instance of this parser.
     */
    @Kapi
    public static final FloatArgumentParser INSTANCE = new FloatArgumentParser();
    
    private FloatArgumentParser() {
    }
    
    @Override
    public Option<Float> parse(AnnotatedType type, String paramName, CommandSender sender, Deque<String> args) {
        return Option.of(args.peek()).andThen(Utils::parseFloat).inspect(ignored -> args.pop());
    }
    
    @Override
    public List<String> getSuggestions(AnnotatedType type, String paramName, CommandSender sender) {
        return List.of();
    }
    
    @Override
    public int getPriority(AnnotatedType type) {
        return PRIORITY;
    }
    
    @Override
    public Option<ArgumentRepresentation> getRepresentation(AnnotatedType type, String paramName) {
        return Option.some(ArgumentRepresentation.of("<", "number", ">"));
    }
}
