/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands;

import io.github.kapimc.kapi.data.Option;
import io.github.kapimc.kapi.utility.Log;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public record CommandRecord(String name, Object instance, List<Method> methods) {
    
    public boolean execute(CommandSender sender, Command ignoredCommand, String ignoredLabel, String[] args) {
        methods.forEach(method -> {
            Log.debug("Invoking method " + method.getName());
            try {
                method.invoke(instance, sender);
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                Log.error("An error occurred while executing the command!", sender);
            } catch (IllegalArgumentException e) {
                Log.error("There must only be 1 argument, which is a CommandSender");
            }
        });
        return true;
    }
    
    public List<String> suggest(CommandSender sender, Command ignoredCommand, String ignoredLabel, String[] args) {
        return List.of();
    }
    
    private Option<Method> getMethod(String name, CommandSender sender, Deque<String> args) {
        ArgumentRegistry registry = ArgumentRegistry.getInstance();
        List<Method> methods = new ArrayList<>(this.methods);
        
        // Skipping the first parameter, which is the CommandSender
        for (int i = 1; i < args.size(); i++) {
            List<Method> methodsForRemoval = new ArrayList<>();
            for (Method method : methods) {
                Parameter parameter = method.getParameters()[i];
                final boolean[] canParse = {false};
                
                registry.get(parameter.getType()).inspect(parser -> {
                    canParse[0] = parser.canParse(args);
                });
                
                if (!canParse[0]) {
                    methodsForRemoval.add(method);
                }
            }
            methods.removeAll(methodsForRemoval);
        }
        
    }
}
