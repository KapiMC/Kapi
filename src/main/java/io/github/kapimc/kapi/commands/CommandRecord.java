/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands;

import io.github.kapimc.kapi.data.Option;
import io.github.kapimc.kapi.data.Pair;
import io.github.kapimc.kapi.utility.Log;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public record CommandRecord(Command instance, List<Method> methods) {
    
    // IntelliJ doesn't support Jspecify's generics.
    // So we suppress it, can be removed once IntelliJ supports Jspecify's generics
    @SuppressWarnings("DataFlowIssue")
    public void onCommand(CommandSender sender, String[] arguments) {
        methods.forEach(method -> {
            Log.debug("Recognized command " + method.getName());
        });
        
        Deque<String> args = new ArrayDeque<>(Arrays.asList(arguments));
        List<Pair<Method,List<Object>>> methods = new ArrayList<>();
        
        for (Method method : this.methods) {
            final boolean[] canParseMethod = {true};
            List<Object> parsedArgs = new ArrayList<>();
            Deque<String> argsCopy = new ArrayDeque<>(args);
            
            // TODO: handle CommandSender
            parsedArgs.add(sender);
            
            // Skipping the first parameter, which is the CommandSender
            for (int i = 1; i < method.getParameterCount(); i++) {
                Parameter parameter = method.getParameters()[i];
                ArgumentParser<?> parser = getParser(parameter)
                    .expect("Failed to get parser for parameter " + parameter.getType().getName());
                Option<?> option = parser.parse(argsCopy, sender, parameter);
                option.match(parsedArgs::add, () -> {
                    canParseMethod[0] = false;
                });
                if (!canParseMethod[0]) {
                    break;
                }
            }
            
            // Empty deque means all args were parsed (no leftovers)
            if (canParseMethod[0] && argsCopy.isEmpty()) {
                methods.add(Pair.of(method, parsedArgs));
            }
        }
        
        if (methods.isEmpty()) {
            // TODO: handle no method (show all potential methods)
            Log.error("No signature matches the input!", sender);
            return;
        }
        
        methods.sort((a, b) -> compare(a.getFirst(), b.getFirst()));
        Method method = methods.get(0).getFirst();
        List<Object> parsedArgs = methods.get(0).getSecond();
        if (methods.size() > 1 && compare(method, methods.get(1).getFirst()) == 0) {
            throw new IllegalStateException(
                "Multiple methods with the same priority. input: " +
                    String.join(" ", args));
        }
        
        // Execute the command
        try {
            method.invoke(instance, parsedArgs.toArray());
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.error("An error occurred while executing the command!", sender);
        }
    }
    
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
    
    private Option<Method> getMethod(CommandSender sender, Deque<String> args) {
        List<Pair<Method,List<Object>>> methods = new ArrayList<>();
        
        for (Method method : this.methods) {
            final boolean[] canParseMethod = {true};
            List<Object> parsedArgs = new ArrayList<>();
            Deque<String> argsCopy = new ArrayDeque<>(args);
            
            // TODO: handle CommandSender
            
            // Skipping the first parameter, which is the CommandSender
            for (int i = 1; i < method.getParameterCount(); i++) {
                Parameter parameter = method.getParameters()[i];
                ArgumentParser<?> parser = getParser(parameter)
                    .expect("Failed to get parser for parameter " + parameter.getType().getName());
                Option<?> option = parser.parse(argsCopy, sender, parameter);
                option.match(parsedArgs::add, () -> {
                    canParseMethod[0] = false;
                });
                if (!canParseMethod[0]) {
                    break;
                }
            }
            
            if (canParseMethod[0]) {
                methods.add(Pair.of(method, parsedArgs));
            }
        }
        
        if (methods.isEmpty()) {
            return Option.none();
        }
        
        methods.sort((a, b) -> compare(a.getFirst(), b.getFirst()));
        Method method = methods.get(0).getFirst();
        if (methods.size() > 1 && compare(method, methods.get(1).getFirst()) == 0) {
            throw new IllegalStateException(
                "Multiple methods with the same priority. input: " +
                    String.join(" ", args));
        }
        return Option.of(method);
    }
    
    private static int compare(Method m1, Method m2) {
        for (int i = 1; i < m1.getParameterCount(); i++) {
            Parameter p1 = m1.getParameters()[i];
            Parameter p2 = m2.getParameters()[i];
            ArgumentParser<?> parser1 =
                getParser(p1).expect("Failed to get parser for parameter " + p1.getType().getName());
            ArgumentParser<?> parser2 =
                getParser(p2).expect("Failed to get parser for parameter " + p2.getType().getName());
            // Flip so the highest priority is first
            int compare = -(parser1.priority() - parser2.priority());
            if (compare != 0) {
                return compare;
            }
        }
        return 0;
    }
    
    private static Option<ArgumentParser<?>> getParser(Parameter parameter) {
        ArgumentRegistry registry = ArgumentRegistry.getInstance();
        return registry.get(parameter.getType());
    }
}
