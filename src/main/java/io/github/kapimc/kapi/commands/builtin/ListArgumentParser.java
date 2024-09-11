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

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Argument parser for lists of any type.
 */
@Kapi
public class ListArgumentParser implements ArgumentParser<List<?>> {
    
    /**
     * The singleton instance of this parser.
     */
    @Kapi
    public static final ListArgumentParser INSTANCE = new ListArgumentParser();
    
    private ListArgumentParser() {
    }
    
    @Override
    public Option<List<?>> parse(AnnotatedType type, String paramName, Deque<String> args, CommandSender sender) {
        List<Object> parsedArgs = new ArrayList<>();
        
        ArgumentParser<?> parser = ArgumentRegistry.getInstance().get(get(getInner(type)))
            .expect("Failed to get argument parser for type " + get(getInner(type)).getSimpleName());
        
        while (true) {
            Option<?> parsedArg = parser.parse(getInner(type), paramName, args, sender);
            if (parsedArg.isNone()) {
                break;
            }
            parsedArgs.add(parsedArg.unwrap());
        }
        
        return Option.some(parsedArgs);
    }
    
    @Override
    public List<String> getSuggestions(
        AnnotatedType type, String paramName, Deque<String> args, CommandSender sender
    ) {
        ArgumentParser<?> parser = ArgumentRegistry.getInstance().get(get(getInner(type)))
            .expect("Failed to get argument parser for type " + get(getInner(type)).getSimpleName());
        return parser.getSuggestions(getInner(type), paramName, args, sender);
    }
    
    @Override
    public int getPriority(AnnotatedType type) {
        final int DIFFERENCE = 100;
        ArgumentParser<?> parser = ArgumentRegistry.getInstance().get(get(getInner(type)))
            .expect("Failed to get argument parser for type " + get(getInner(type)).getSimpleName());
        return parser.getPriority(getInner(type)) - DIFFERENCE;
    }
    
    @Override
    public Option<ArgumentRepresentation> getRepresentation(AnnotatedType type, String paramName) {
        AnnotatedType innerType = getInner(type);
        ArgumentParser<?> parser = ArgumentRegistry.getInstance()
            .get(get(innerType))
            .expect("Failed to get parser for component type " + get(type).getSimpleName());
        return parser.getRepresentation(innerType, paramName)
            .map(s -> s.prefix("[").name(s.getName() + "...").suffix("]"));
    }
    
    private static Class<?> get(AnnotatedType type) {
        assert type.getType() instanceof Class<?>;
        return (Class<?>) type.getType();
    }
    
    private static AnnotatedType getInner(AnnotatedType type) {
        assert type instanceof AnnotatedParameterizedType;
        AnnotatedParameterizedType parameterizedType = (AnnotatedParameterizedType) type;
        AnnotatedType[] generics = parameterizedType.getAnnotatedActualTypeArguments();
        assert generics.length == 1;
        return generics[0];
        
    }
}
