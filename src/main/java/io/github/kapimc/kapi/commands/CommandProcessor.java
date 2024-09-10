/*
 * Copyright (c) 2041-2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands;

import io.github.kapimc.kapi.annotations.SubCommand;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CommandProcessor {
    
    private CommandProcessor() {
        throw new AssertionError("CommandProcessor should not be instantiated");
    }
    
    /**
     * Processes a command class and returns a {@link CommandRecord}.
     *
     * @param instance an instance of a command class, which must be annotated with {@link Command}
     * @return a {@link CommandRecord} containing the command's name, instance, and methods
     * @throws IllegalArgumentException if the class is not annotated with {@link Command} or if
     *                                  a method that is annotated with {@link SubCommand} is either private, has less
     *                                  than one parameter,
     *                                  or the first parameter is not an instance of {@link CommandSender}
     */
    public static CommandRecord process(Command instance) {
        Class<?> commandClass = instance.getClass();
        
        List<Method> methods = new ArrayList<>(Arrays.asList(commandClass.getMethods()));
        methods.removeIf(method -> !method.isAnnotationPresent(SubCommand.class));
        
        for (Method method : methods) {
            if (!Modifier.isPublic(method.getModifiers())) {
                throw new IllegalArgumentException("Method " + method.getName() + " must be public");
            }
            if (method.getParameterCount() < 1) {
                throw new IllegalArgumentException(
                    "Method " + method.getName() + " must have at least one parameter");
            }
            Parameter parameter = method.getParameters()[0];
            if (!CommandSender.class.isAssignableFrom(parameter.getType())) {
                throw new IllegalArgumentException(
                    "Method " + method.getName() + " must have the first parameter be an instance of CommandSender");
            }
        }
        
        return new CommandRecord(instance, methods);
    }
}
