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
        Deque<String> args = new ArrayDeque<>(Arrays.asList(arguments));
        List<Pair<Method,List<Object>>> methods = new ArrayList<>();
        
        for (Method method : this.methods) {
            final boolean[] canParseMethod = {true};
            List<Object> parsedArgs = new ArrayList<>();
            Deque<String> argsCopy = new ArrayDeque<>(args);
            
            Class<?> senderClass = method.getParameters()[0].getType();
            if (!senderClass.isInstance(sender)) {
                continue;
            }
            parsedArgs.add(sender);
            
            // Skipping the first parameter, which is the CommandSender
            for (int i = 1; i < method.getParameterCount(); i++) {
                Parameter parameter = method.getParameters()[i];
                ArgumentParser<?> parser = getParser(parameter)
                    .expect("Failed to get parser for parameter " + parameter.getType().getName());
                Option<?> option = parser.parse(parameter.getAnnotatedType(), parameter.getName(), argsCopy, sender);
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
            instance.onNoMethodMatches(sender, arguments, this.methods);
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
    
    public List<String> onTabComplete(CommandSender sender, String[] arguments) {
        List<String> completions = new ArrayList<>();
        Deque<String> args = new ArrayDeque<>(Arrays.asList(arguments));
        
        for (Method method : this.methods) {
            Deque<String> argsCopy = new ArrayDeque<>(args);
            
            Class<?> senderClass = method.getParameters()[0].getType();
            if (!senderClass.isInstance(sender)) {
                continue;
            }
            
            // Skipping the first parameter, which is the CommandSender
            for (int i = 1; i < method.getParameterCount(); i++) {
                Parameter parameter = method.getParameters()[i];
                ArgumentParser<?> parser = getParser(parameter)
                    .expect("Failed to get parser for parameter " + parameter.getType().getName());
                
                if (argsCopy.isEmpty()) {
                    completions.addAll(
                        parser.getSuggestions(parameter.getAnnotatedType(), parameter.getName(), argsCopy, sender));
                    break;
                }
                Deque<String> argsCopyCopy = new ArrayDeque<>(argsCopy);
                Option<?> option =
                    parser.parse(parameter.getAnnotatedType(), parameter.getName(), argsCopyCopy, sender);
                if (option.isNone()) {
                    completions.addAll(
                        parser.getSuggestions(parameter.getAnnotatedType(), parameter.getName(), argsCopy, sender));
                    break;
                }
            }
        }
        
        return completions;
    }
    
    private static int compare(Method m1, Method m2) {
        Class<?> sender1 = m1.getParameters()[0].getType();
        Class<?> sender2 = m2.getParameters()[0].getType();
        
        // Check which sender is more specific.
        // More specific means if it is a subclass of the other sender
        boolean isSender1subclass = !sender1.isAssignableFrom(sender2); // is sender1 = sender2 not fine
        boolean isSender2subclass = !sender2.isAssignableFrom(sender1); // is sender2 = sender1 not fine
        assert isSender1subclass || isSender2subclass; // both can't be false at the same time
        if (isSender1subclass != isSender2subclass) {
            return isSender1subclass ? -1 : 1;
        }
        
        // If they are the same, we check other parameters
        for (int i = 1; i < m1.getParameterCount(); i++) {
            Parameter p1 = m1.getParameters()[i];
            if (m2.getParameterCount() >= i) {
                return -1; // We prioritize the first parameter if the second one is missing
            }
            Parameter p2 = m2.getParameters()[i];
            ArgumentParser<?> parser1 = getParser(p1)
                .expect("Failed to get parser for parameter " + p1.getType().getName());
            ArgumentParser<?> parser2 = getParser(p2)
                .expect("Failed to get parser for parameter " + p2.getType().getName());
            // Flip so the highest priority is first
            int compare = -(parser1.getPriority(p1.getAnnotatedType()) - parser2.getPriority(p2.getAnnotatedType()));
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
