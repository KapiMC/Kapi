/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands;

import io.github.kapimc.kapi.data.Option;
import io.github.kapimc.kapi.utility.Log;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public record CommandRecord(Command instance, List<Method> methods) {
    
    public boolean onCommand(CommandSender sender, String[] args) {
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
    
    public List<String> onTabComplete(CommandSender sender, String[] args) {
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
        // TODO: implement
        return null;
        /*
        Priority Table
        String = 0
        long = 7
        int = 9
        Enum = 10
        Location = 10
        
         1. String -> 0
         2. int -> 9
         2 wins
         
         1. int -> 9
         2. long -> 7
         1 wins
         
         1. String Location -> ??
         2. Enum int int int -> ??
         3. String int int int -> ??
         3 loses intuitively, but what about 1 and 2?
         Let's say location should win, but then, what about this:
         
         1. String String String String String Location
         2. Enum Enum Enum Enum Enum int int int
         Should now enum win?
         What if we add more Locations, at what point it would change?
         
         If each thing has a priority that determines how "specific" it is.
         What if there are two options, like:
         1. int int
         2. short long
         Is short vs int more specific than long vs short?
         
         */
    }
}
