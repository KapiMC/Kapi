/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands;

import io.github.kapimc.kapi.utility.Log;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

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
}
