/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands;

import io.github.kapimc.kapi.data.Option;
import io.github.kapimc.kapi.data.Result;
import io.github.kapimc.kapi.data.TriFunction;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Parameter;
import java.util.Deque;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface ArgumentParser<T> {
    
    Option<T> parse(Deque<String> args, CommandSender sender, Parameter parameter);
    
    List<String> suggestions(Deque<String> args, CommandSender sender, Parameter parameter);
    
    int priority();
    
    Option<String> representation(Parameter parameter);
    
    static <T> ArgumentParser<T> of(
        TriFunction<Deque<String>,CommandSender,Parameter,Option<T>> parse,
        TriFunction<Deque<String>,CommandSender,Parameter,List<String>> suggest,
        Function<Parameter,Option<String>> representation,
        int priority
    ) {
        return new ArgumentParser<>() {
            @Override
            public Option<T> parse(Deque<String> args, CommandSender sender, Parameter parameter) {
                return parse.apply(args, sender, parameter);
            }
            
            @Override
            public List<String> suggestions(Deque<String> args, CommandSender sender, Parameter parameter) {
                return suggest.apply(args, sender, parameter);
            }
            
            @Override
            public int priority() {
                return priority;
            }
            
            @Override
            public Option<String> representation(Parameter parameter) {
                return representation.apply(parameter);
            }
        };
    }
}
