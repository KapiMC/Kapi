/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands;

import io.github.kapimc.kapi.data.Option;
import io.github.kapimc.kapi.data.Result;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Parameter;
import java.util.Deque;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface ArgumentParser<T> {
    
    boolean canParse(Deque<String> args);
    
    Result<T,String> parse(Deque<String> args, CommandSender sender, Parameter parameter);
    
    List<String> suggest(Deque<String> args, CommandSender sender, Parameter parameter);
    
    int getPriority();
    
    static <T> ArgumentParser<T> of(
        Predicate<Deque<String>> canParse,
        <Deque<String>,Result<T,String>> parse,
        Function<Deque<String>,List<String>> suggest,
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
            public List<String> suggest(Deque<String> args, CommandSender sender, Parameter parameter) {
                return suggest.apply(args, sender, parameter);
            }
            
            @Override
            public int getPriority() {
                return priority;
            }
        };
    }
}
