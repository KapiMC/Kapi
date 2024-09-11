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
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Parameter;
import java.util.Deque;
import java.util.List;
import java.util.stream.Stream;

/**
 * Argument parser for {@link Material}.
 * Takes precedence over {@link EnumArgumentParser}.
 */
@Kapi
public class MaterialArgumentParser implements ArgumentParser<Material> {
    
    /**
     * The priority of this parser.
     */
    @Kapi
    public static final int PRIORITY = 20000;
    
    /**
     * Singleton instance of this parser.
     */
    @Kapi
    public static final MaterialArgumentParser INSTANCE = new MaterialArgumentParser();
    
    private MaterialArgumentParser() {
    }
    
    @Override
    public Option<Material> parse(Deque<String> args, CommandSender sender, AnnotatedType type) {
        return Option.of(args.peek())
            .andThen(s -> Option.of(Material.matchMaterial(s)))
            .inspect(ignored -> args.pop());
    }
    
    @Override
    public List<String> getSuggestions(Deque<String> args, CommandSender sender, AnnotatedType type) {
        return Stream.of(Material.values())
            .map(Material::name)
            .map(String::toLowerCase)
            .toList();
    }
    
    @Override
    public int getPriority(AnnotatedType type) {
        return PRIORITY;
    }
    
    @Override
    public Option<String> getRepresentation(Parameter parameter) {
        return Option.some("material");
    }
}
