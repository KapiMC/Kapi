/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands;

import io.github.kapimc.kapi.data.Result;
import io.github.kapimc.kapi.data.TriFunction;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Parameter;
import java.util.Deque;
import java.util.List;
import java.util.function.Predicate;

public interface ArgumentParser<T> {
    
    boolean canParse(Deque<String> args);
    
    Result<T,String> parse(Deque<String> args, CommandSender sender, Parameter parameter);
    
    List<String> suggestions(Deque<String> args, CommandSender sender, Parameter parameter);
    
    int priority();
    
    static <T> ArgumentParser<T> of(
        Predicate<Deque<String>> canParse,
        TriFunction<Deque<String>,CommandSender,Parameter,Result<T,String>> parse,
        TriFunction<Deque<String>,CommandSender,Parameter,List<String>> suggest,
        int priority
    ) {
        return new ArgumentParser<>() {
            @Override
            public boolean canParse(Deque<String> args) {
                return canParse.test(args);
            }
            
            @Override
            public Result<T,String> parse(Deque<String> args, CommandSender sender, Parameter parameter) {
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
        };
    }
}
