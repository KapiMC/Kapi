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

/**
 * Represents a record of an immutable command,
 * used to execute a command.
 *
 * @param instance the command instance
 * @param methods  the methods of the command
 */
public record CommandRecord(Command instance, List<Method> methods) {
    
    /**
     * Executes the command.
     *
     * @param sender    the sender of the command
     * @param arguments the arguments of the command
     * @param label     the name or alias of the command
     */
    // IntelliJ doesn't support Jspecify's generics.
    // So we suppress it, can be removed once IntelliJ supports Jspecify's generics
    @SuppressWarnings("DataFlowIssue")
    public void onCommand(CommandSender sender, String[] arguments, String label) {
        Deque<String> args = new ArrayDeque<>(Arrays.asList(arguments));
        List<Pair<Method,List<Object>>> methods = new ArrayList<>();
        
        for (Method method : this.methods) {
            Log.kapi("Processing method " + method.getName());
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
                ArgumentParser<?> parser = ArgumentRegistry.getInstance()
                    .get(parameter.getType())
                    .expect("Failed to get parser for parameter " + parameter.getType().getName());
                Option<?> option = parser.parse(parameter.getAnnotatedType(), sender, argsCopy);
                option.match(parsedArgs::add, () -> {
                    canParseMethod[0] = false;
                });
                if (!canParseMethod[0]) {
                    Log.kapi("Failed to parse method " + method.getName());
                    break;
                }
            }
            
            // Empty deque means all values were parsed (no leftovers)
            if (canParseMethod[0] && argsCopy.isEmpty()) {
                Log.kapi("Successfully parsed method " + method.getName());
                methods.add(Pair.of(method, parsedArgs));
            }
        }
        
        if (methods.isEmpty()) {
            instance.onNoMethodMatches(label, sender, arguments, this.methods);
            return;
        }
        
        methods.sort((a, b) -> CommandProcessor.compare(a.getFirst(), b.getFirst()));
        Method method = methods.get(0).getFirst();
        List<Object> parsedArgs = methods.get(0).getSecond();
        if (methods.size() > 1 && CommandProcessor.compare(method, methods.get(1).getFirst()) == 0) {
            throw new IllegalStateException(
                "Multiple methods with the same priority. input: " + String.join(" ", args));
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
    
    /**
     * Gets the completions of the command.
     *
     * @param sender    the sender of the command
     * @param arguments the arguments of the command
     * @return the completions of the command
     */
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
                ArgumentParser<?> parser = ArgumentRegistry.getInstance()
                    .get(parameter.getType())
                    .expect("Failed to get parser for parameter " + parameter.getType().getName());
                
                if (argsCopy.isEmpty() || argsCopy.peek().isEmpty()) {
                    completions.addAll(parser.getSuggestions(parameter.getAnnotatedType(), sender));
                    break;
                }
                Deque<String> argsCopyCopy = new ArrayDeque<>(argsCopy);
                Option<?> option = parser.parse(parameter.getAnnotatedType(), sender, argsCopyCopy);
                if (option.isNone()) {
                    argsCopyCopy.pollFirst();
                    boolean empty = argsCopyCopy.isEmpty() || argsCopyCopy.peek().isEmpty();
                    if (!empty) {
                        completions.addAll(parser.getSuggestions(parameter.getAnnotatedType(), sender));
                    }
                    break;
                }
                
                if (option.isSome() && parser.isParseableOnFailure()) {
                    completions.addAll(parser.getSuggestions(parameter.getAnnotatedType(), sender));
                    // Don't break here, we still want to continue and potentially suggest the next parameter
                }
            }
        }
        
        return completions;
    }
}
