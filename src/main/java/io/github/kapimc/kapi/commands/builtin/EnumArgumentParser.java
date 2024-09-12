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
import org.bukkit.command.CommandSender;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * Argument parser for all enums.
 */
@Kapi
public class EnumArgumentParser implements ArgumentParser<Enum<?>> {
    
    /**
     * The priority of this parser.
     */
    @Kapi
    public static final int PRIORITY = 11000;
    
    /**
     * The singleton instance of this parser.
     */
    @Kapi
    public static final EnumArgumentParser INSTANCE = new EnumArgumentParser();
    public static final String INVALID_ENUM_CONSTANT = "Enum doesn't have the given constant";
    
    private EnumArgumentParser() {
    }
    
    @Override
    public Option<Enum<?>> parse(AnnotatedType type, CommandSender sender, Deque<String> args) {
        if (args.peek() == null) {
            return Option.none();
        }
        Option<Class<?>> enumClass = getEnumClass(type);
        try {
            return enumClass.map(clazz -> {
                try {
                    Method valueOf = clazz.getMethod("valueOf", String.class);
                    String name = args.pollFirst();
                    assert name != null;
                    return (Enum<?>) valueOf.invoke(null, name.toUpperCase());
                } catch (NoSuchMethodException | IllegalAccessException e) {
                    throw new AssertionError(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(INVALID_ENUM_CONSTANT);
                }
            });
        } catch (RuntimeException e) {
            if (e.getMessage().equals(INVALID_ENUM_CONSTANT)) {
                return Option.none();
            }
            throw e;
        }
    }
    
    @Override
    public List<String> getSuggestions(AnnotatedType type, CommandSender sender) {
        return getEnumClass(type).map(clazz -> {
            try {
                Method values = clazz.getMethod("values");
                Enum<?>[] enums = (Enum<?>[]) values.invoke(null);
                return Arrays.stream(enums).map(Enum::name).toList();
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }).unwrapOrElse(List::of);
    }
    
    @Override
    public int getPriority(AnnotatedType type) {
        return PRIORITY;
    }
    
    @Override
    public Option<ArgumentRepresentation> getRepresentation(AnnotatedType type) {
        return getEnumClass(type)
            .map(clazz -> ArgumentRepresentation.of("<", clazz.getSimpleName(), ">"));
    }
    
    private Option<Class<?>> getEnumClass(AnnotatedType type) {
        if (type.getType() instanceof Class<?> clazz && clazz.isEnum()) {
            return Option.some(clazz);
        }
        return Option.none();
    }
}
