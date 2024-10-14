/*
 * Copyright (c) 2041-2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands;

import io.github.kapimc.kapi.annotations.Literal;
import io.github.kapimc.kapi.annotations.SubCommand;
import io.github.kapimc.kapi.utility.Log;
import org.bukkit.command.CommandSender;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Processes a command class and returns a {@link CommandRecord}.
 * <p>
 * Also contains some helper methods for the command system.
 */
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
        String commandName = commandClass.getSimpleName();
        
        List<Method> methods = new ArrayList<>(Arrays.asList(commandClass.getMethods()));
        methods.removeIf(method -> !method.isAnnotationPresent(SubCommand.class));
        
        for (Method method : methods) {
            if (!Modifier.isPublic(method.getModifiers())) {
                throw new IllegalArgumentException("Method " + method.getName() + " must be public");
            }
            if (method.getParameterCount() < 1) {
                throw new IllegalArgumentException(String.format(
                    "Method %s in %s class must have at least one parameter",
                    method.getName(), commandName
                ));
            }
            Parameter sender = method.getParameters()[0];
            if (!CommandSender.class.isAssignableFrom(sender.getType())) {
                throw new IllegalArgumentException(String.format(
                    "Method %s in %s class must have the first parameter be an instance of CommandSender",
                    method.getName(), commandName
                ));
            }
            
            for (int i = 1; i < method.getParameterCount(); i++) {
                Parameter parameter = method.getParameters()[i];
                if (!isRegisteredType(parameter.getAnnotatedType())) {
                    throw new IllegalArgumentException(String.format(
                        "Method %s in %s class has an unsupported parameter of type %s",
                        method.getName(), commandName, parameter.getType().getName()
                    ));
                }
            }
        }
        
        for (int i = 0; i < methods.size(); i++) {
            for (int j = i + 1; j < methods.size(); j++) {
                if (compare(methods.get(i), methods.get(j)) == 0) {
                    throw new IllegalArgumentException(String.format(
                        "Ambiguous methods %s and %s in %s class, methods with the same priority are not allowed!",
                        methods.get(i).getName(), methods.get(j).getName(), commandName
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
    
    /**
     * Compares two methods.
     * <p>
     * The priority of a method is calculated by going through each method's parameters
     * and comparing the priorities of those parameters.
     * For the first parameter which is always the sender,
     * the priority is calculated based on subclassing, which means
     * that if m1's sender is a subclass of m2's sender, m1 has a higher priority than m2
     * because it's "more specific", if m2's sender is a subclass of m1's sender, m2 has a higher priority
     * because it's "more specific", if they are the same, the next parameter is checked, and so on.
     * <p>
     * "Argument parameters" are checked by getting each parameter's {@link ArgumentParser} and comparing
     * the parser's priority with the other parser's priority.
     * If one of the priorities is higher, this method returns either -1 or 1, depending on which one is higher.
     * If they are equal, the next parameter is checked, and so on until all parameters are checked.
     * <p>
     * If both methods match in priority but one method has more parameters than the other,
     * the method with the extra parameters is considered less specific, so the other method
     * will be chosen.
     * For example, {@code foo(CommandSender)} and {@code bar(CommandSender, int[])},
     * given an empty input, both can match (bar will get an empty array as its second parameter),
     * but {@code foo} is more specific because it has no extra parameters, so it will be chosen.
     *
     * @param m1 the first method
     * @param m2 the second method
     * @return -1 if m1 has a higher priority, 0 if they are equal, 1 if m2 has a higher priority
     */
    public static int compare(Method m1, Method m2) {
        Log.kapi("Comparing methods " + m1.getName() + " and " + m2.getName());
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
            Log.kapi("Comparing parameters at index " + i);
            Parameter p1 = m1.getParameters()[i];
            if (m2.getParameterCount() == i) {
                return 1; // m1 has more parameters than m2
            }
            Parameter p2 = m2.getParameters()[i];
            ArgumentParser<?> parser1 = ArgumentRegistry.getInstance().get(p1.getType())
                .expect("Failed to get parser for parameter " + p1.getType().getName());
            ArgumentParser<?> parser2 = ArgumentRegistry.getInstance().get(p2.getType())
                .expect("Failed to get parser for parameter " + p2.getType().getName());
            // Flip so the highest priority is first
            int compare = -(parser1.getPriority(p1.getAnnotatedType()) - parser2.getPriority(p2.getAnnotatedType()));
            if (compare != 0) {
                Log.kapi("Returning Compare: " + compare);
                return compare;
            }
            Log.kapi("Parameters have equal priority");
            
            // NOTE: Kotlin seems to not include annotations on parameters
            // From my research, Kotlin parameter/type use annotations seem to be stored as Kotlin-specific metadata
            // Using @Literal annotations in Kotlin is currently not possible as far as I know
            // If you know a fix that doesn't involve using Kotlin's reflection
            // or a workaround a Kotlin developer can use to add this annotation
            // Please reach out with a PR/issue so it can be fixed/ or documented
            
            // Special case for literal annotation checking
            if (p1.getAnnotatedType().isAnnotationPresent(Literal.class)) {
                Log.kapi("Literal parameters");
                Literal literal1 = p1.getAnnotatedType().getAnnotation(Literal.class);
                Literal literal2 = p2.getAnnotatedType().getAnnotation(Literal.class);
                if (!literal1.caseSensitive() && literal2.caseSensitive()) {
                    if (!literal1.value().equals(literal2.value())) {
                        Log.kapi("Case Sensitive Name Mismatch");
                        return -1; // Arbitrary, can be either -1 or 1, but not 0
                    }
                    for (String alias : literal1.aliases()) {
                        if (!alias.equals(literal2.value())) {
                            Log.kapi("Case Sensitive Alias Mismatch");
                            return -1; // Arbitrary, can be either -1 or 1, but not 0
                        }
                    }
                } else {
                    if (!literal1.value().equalsIgnoreCase(literal2.value())) {
                        Log.kapi("Case Insensitive Name Mismatch");
                        return -1; // Arbitrary, can be either -1 or 1, but not 0
                    }
                    for (String alias : literal1.aliases()) {
                        if (!alias.equalsIgnoreCase(literal2.value())) {
                            Log.kapi("Case Insensitive Alias Mismatch");
                            return -1; // Arbitrary, can be either -1 or 1, but not 0
                        }
                    }
                }
                Log.kapi("Parameter literals conflict!");
            }
        }
        if (m1.getParameterCount() < m2.getParameterCount()) {
            return -1; // m2 has more parameters than m1
        }
        Log.kapi("Methods " + m1.getName() + " and " + m2.getName() + " have equal priority");
        return 0;
    }
}
