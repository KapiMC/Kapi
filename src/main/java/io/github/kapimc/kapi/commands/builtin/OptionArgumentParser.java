/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands.builtin;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.commands.ArgumentParser;
import io.github.kapimc.kapi.commands.ArgumentRegistry;
import io.github.kapimc.kapi.commands.ArgumentRepresentation;
import io.github.kapimc.kapi.data.Option;
import org.bukkit.command.CommandSender;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.util.Deque;
import java.util.List;

/**
 * Argument parser for {@link Option} objects.
 */
@Kapi
public class OptionArgumentParser implements ArgumentParser<Option<?>> {
    
    /**
     * The singleton instance of this parser.
     */
    @Kapi
    public static final OptionArgumentParser INSTANCE = new OptionArgumentParser();
    
    @Override
    public Option<Option<?>> parse(AnnotatedType type, CommandSender sender, Deque<String> args) {
        ArgumentParser<?> parser = ArgumentRegistry.getInstance().get(get(getInner(type)))
            .expect("Failed to get argument parser for type " + get(getInner(type)).getSimpleName());
        Option<?> parsedArg = parser.parse(getInner(type), sender, args);
        return Option.some(parsedArg);
    }
    
    @Override
    public List<String> getSuggestions(AnnotatedType type, CommandSender sender) {
        ArgumentParser<?> parser = ArgumentRegistry.getInstance().get(get(getInner(type)))
            .expect("Failed to get argument parser for type " + get(getInner(type)).getSimpleName());
        return parser.getSuggestions(getInner(type), sender);
    }
    
    @Override
    public int getPriority(AnnotatedType type) {
        final int DIFFERENCE = 50;
        ArgumentParser<?> parser = ArgumentRegistry.getInstance().get(get(getInner(type)))
            .expect("Failed to get argument parser for type " + get(getInner(type)).getSimpleName());
        return parser.getPriority(getInner(type)) - DIFFERENCE;
    }
    
    @Override
    public Option<ArgumentRepresentation> getRepresentation(AnnotatedType type) {
        AnnotatedType innerType = getInner(type);
        ArgumentParser<?> parser = ArgumentRegistry.getInstance()
            .get(get(innerType))
            .expect("Failed to get parser for generic type " + get(type).getSimpleName());
        return parser.getRepresentation(innerType)
            .map(s -> s.prefix("[").suffix("]"));
    }
    
    private static Class<?> get(AnnotatedType type) {
        assert type.getType() instanceof Class<?>;
        if (type.getType() instanceof Class<?> clazz) {
            return clazz;
        } else if (type.getType() instanceof ParameterizedType parameterizedType) {
            assert parameterizedType.getRawType() instanceof Class<?>;
            return (Class<?>) parameterizedType.getRawType();
        }
        throw new AssertionError("Invalid Type in OptionArgumentParser#get(AnnotatedType)");
    }
    
    private static AnnotatedType getInner(AnnotatedType type) {
        assert type instanceof AnnotatedParameterizedType;
        AnnotatedParameterizedType parameterizedType = (AnnotatedParameterizedType) type;
        AnnotatedType[] generics = parameterizedType.getAnnotatedActualTypeArguments();
        assert generics.length == 1;
        return generics[0];
        
    }
}
