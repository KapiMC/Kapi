/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands;

import io.github.kapimc.kapi.data.Option;
import io.github.kapimc.kapi.data.TriFunction;
import org.bukkit.command.CommandSender;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Parameter;
import java.util.Deque;
import java.util.List;
import java.util.function.Function;

public interface ArgumentParser<T> {
    
    Option<T> parse(Deque<String> args, CommandSender sender, AnnotatedType type);
    
    List<String> suggestions(Deque<String> args, CommandSender sender, AnnotatedType type);
    
    int getPriority(AnnotatedType type);
    
    Option<String> representation(Parameter parameter);
    
    static <T> ArgumentParser<T> of(
        TriFunction<Deque<String>,CommandSender,AnnotatedType,Option<T>> parse,
        TriFunction<Deque<String>,CommandSender,AnnotatedType,List<String>> suggest,
        Function<Parameter,Option<String>> representation,
        int priority
    ) {
        return new ArgumentParser<>() {
            @Override
            public Option<T> parse(Deque<String> args, CommandSender sender, AnnotatedType type) {
                return parse.apply(args, sender, type);
            }
            
            @Override
            public List<String> suggestions(Deque<String> args, CommandSender sender, AnnotatedType type) {
                return suggest.apply(args, sender, type);
            }
            
            @Override
            public int getPriority(AnnotatedType type) {
                return priority;
            }
            
            @Override
            public Option<String> representation(Parameter parameter) {
                return representation.apply(parameter);
            }
        };
    }
}
