/*
 * Copyright (c) 2041-2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands;

import io.github.kapimc.kapi.annotations.SubCommand;
import io.github.kapimc.kapi.utility.Log;
import org.bukkit.command.CommandSender;

import java.lang.reflect.*;
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
                throw new IllegalArgumentException(String.format(
                    "Method %s must have at least one parameter",
                    method.getName()
                ));
            }
            Parameter sender = method.getParameters()[0];
            if (!CommandSender.class.isAssignableFrom(sender.getType())) {
                throw new IllegalArgumentException(String.format(
                    "Method %s must have the first parameter be an instance of CommandSender",
                    method.getName()
                ));
            }
            
            for (int i = 1; i < method.getParameterCount(); i++) {
                Parameter parameter = method.getParameters()[i];
                if (!isRegisteredType(parameter.getAnnotatedType())) {
                    throw new IllegalArgumentException(String.format(
                        "Method %s has an unsupported parameter type: %s",
                        method.getName(), parameter.getType().getName()
                    ));
                }
            }
        }
        
        return new CommandRecord(instance, methods);
    }
    
    private static boolean isRegisteredType(AnnotatedType type) {
        if (type instanceof AnnotatedParameterizedType parameterizedType) {
            for (AnnotatedType argument : parameterizedType.getAnnotatedActualTypeArguments()) {
                if (!isRegisteredType(argument)) {
                    return false;
                }
            }
            if (parameterizedType.getType() instanceof ParameterizedType pType &&
                pType.getRawType() instanceof Class<?> clazz) {
                return isRegisteredClass(clazz);
            }
            return false;
        } else if (type instanceof AnnotatedArrayType arrayType) {
            // Arrays are always supported
            return isRegisteredType(arrayType.getAnnotatedGenericComponentType());//
        } else if (type instanceof AnnotatedTypeVariable) {
            return false;
        } else if (type instanceof AnnotatedWildcardType) {
            return false;
        } else if (type.getType() instanceof Class<?> clazz) {
            return isRegisteredClass(clazz);
        } else {
            return false;
        }
    }
    
    private static boolean isRegisteredClass(Class<?> clazz) {
        return ArgumentRegistry.getInstance().get(clazz).isSome();
    }
}
