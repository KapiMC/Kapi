/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands.builtin;

import io.github.kapimc.kapi.commands.ArgumentParser;
import io.github.kapimc.kapi.commands.ArgumentRegistry;
import io.github.kapimc.kapi.data.Option;
import io.github.kapimc.kapi.utility.Log;
import org.bukkit.command.CommandSender;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ArrayArgumentParser implements ArgumentParser<Object> {
    
    @Override
    public Option<Object> parse(Deque<String> args, CommandSender sender, AnnotatedType type) {
        Log.debug("Parsing array argument", sender);
        Class<?> clazz = getClassFromAnnotatedType(type);
        Log.debug("Class is " + clazz.getSimpleName(), sender);
        if (!(type instanceof AnnotatedArrayType arrayType)) {
            return Option.none();
        }
        Log.debug("Annotated array type", sender);
        AnnotatedType componentType = arrayType.getAnnotatedGenericComponentType();
        Class<?> componentClass = getClassFromAnnotatedType(componentType);
        Log.debug("Component class is " + componentClass.getSimpleName(), sender);
        ArgumentParser<?> parser = ArgumentRegistry.getInstance().get(componentClass)
            .expect("THIS Failed to get parser for component type " + componentClass.getSimpleName());
        
        List<Object> parsedArgs = new ArrayList<>();
        while (true) {
            Option<?> parsedArg = parser.parse(args, sender, componentType);
            if (parsedArg.isNone()) {
                Log.debug("No more args", sender);
                break;
            }
            Log.debug("Parsed arg " + parsedArg.unwrap(), sender);
            parsedArgs.add(parsedArg.unwrap());
        }
        
        Object array = Array.newInstance(componentClass, parsedArgs.size());
        for (int i = 0; i < parsedArgs.size(); i++) {
            Array.set(array, i, parsedArgs.get(i));
        }
        
        Log.debug("Parsed args size: " + parsedArgs.size(), sender);
        return Option.some(array);
    }
    
    @Override
    public List<String> suggestions(Deque<String> args, CommandSender sender, AnnotatedType type) {
        return List.of();
    }
    
    @Override
    public int priority() {
        return 50;
    }
    
    @Override
    public Option<String> representation(Parameter parameter) {
        return Option.some(parameter.getType().getSimpleName() + "[]");
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
