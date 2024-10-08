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

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Argument parser for arrays of any type.
 */
@Kapi
public class ArrayArgumentParser implements ArgumentParser<Object> {
    
    /**
     * The singleton instance of this parser.
     */
    @Kapi
    public static final ArrayArgumentParser INSTANCE = new ArrayArgumentParser();
    
    private ArrayArgumentParser() {
    }
    
    @Override
    public Option<Object> parse(AnnotatedType type, CommandSender sender, Deque<String> args) {
        if (!(type instanceof AnnotatedArrayType arrayType)) {
            return Option.none();
        }
        AnnotatedType componentType = arrayType.getAnnotatedGenericComponentType();
        Class<?> componentClass = getClassFromAnnotatedType(componentType);
        ArgumentParser<?> parser = ArgumentRegistry.getInstance().get(componentClass)
            .expect("THIS Failed to get parser for component type " + componentClass.getSimpleName());
        
        List<Object> parsedArgs = new ArrayList<>();
        while (true) {
            Option<?> parsedArg = parser.parse(componentType, sender, args);
            if (parsedArg.isNone()) {
                break;
            }
            parsedArgs.add(parsedArg.unwrap());
        }
        
        Object array = Array.newInstance(componentClass, parsedArgs.size());
        for (int i = 0; i < parsedArgs.size(); i++) {
            Array.set(array, i, parsedArgs.get(i));
        }
        
        return Option.some(array);
    }
    
    @Override
    public List<String> getSuggestions(AnnotatedType type, CommandSender sender) {
        assert type instanceof AnnotatedArrayType;
        AnnotatedArrayType arrayType = (AnnotatedArrayType) type;
        Class<?> componentType = (Class<?>) arrayType.getAnnotatedGenericComponentType().getType();
        ArgumentParser<?> parser = ArgumentRegistry.getInstance()
            .get(componentType)
            .expect("Failed to get parser for component type " + componentType.getSimpleName());
        return parser.getSuggestions(arrayType.getAnnotatedGenericComponentType(), sender);
    }
    
    @Override
    public int getPriority(AnnotatedType type) {
        final int DIFFERENCE = 100;
        assert type instanceof AnnotatedArrayType;
        AnnotatedArrayType arrayType = (AnnotatedArrayType) type;
        Class<?> componentType = (Class<?>) arrayType.getAnnotatedGenericComponentType().getType();
        ArgumentParser<?> parser = ArgumentRegistry.getInstance()
            .get(componentType)
            .expect("Failed to get parser for component type " + componentType.getSimpleName());
        return parser.getPriority(arrayType.getAnnotatedGenericComponentType()) - DIFFERENCE;
    }
    
    @Override
    public Option<ArgumentRepresentation> getRepresentation(AnnotatedType type) {
        assert type instanceof AnnotatedArrayType;
        AnnotatedArrayType arrayType = (AnnotatedArrayType) type;
        Class<?> componentType = (Class<?>) arrayType.getAnnotatedGenericComponentType().getType();
        ArgumentParser<?> parser = ArgumentRegistry.getInstance()
            .get(componentType)
            .expect("Failed to get parser for component type " + componentType.getSimpleName());
        return parser.getRepresentation(arrayType.getAnnotatedGenericComponentType())
            .map(s -> s.prefix("[").name(s.getName() + "...").suffix("]"));
    }
    
    @Override
    public boolean isParseableOnFailure() {
        return true;
    }
    
    private static Class<?> getClassFromAnnotatedType(AnnotatedType annotatedType) {
        if (annotatedType instanceof AnnotatedParameterizedType parameterizedType) {
            return (Class<?>) parameterizedType.getType();
        }
        if (annotatedType instanceof AnnotatedArrayType arrayType) {
            Class<?> componentClass = getClassFromAnnotatedType(arrayType.getAnnotatedGenericComponentType());
            return Array.newInstance(componentClass, 0).getClass();
        }
        if (annotatedType instanceof AnnotatedTypeVariable) {
            throw new IllegalArgumentException(
                "Cannot get class from type variable (must not contain method generics)");
        }
        return (Class<?>) annotatedType.getType();
    }
}
