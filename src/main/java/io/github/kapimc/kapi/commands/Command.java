/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.annotations.SubCommand;
import io.github.kapimc.kapi.data.Result;
import io.github.kapimc.kapi.utility.Log;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.function.Function;

/**
 * Represents a command, this class should be extended by all commands.
 * <p>
 * You can annotate methods within this class with {@link SubCommand} to define subcommands.
 * Subcommands are automatically executed when the command's input matches all the method's parameters.
 * If there are multiple subcommands that match, an "Alphabetical Order" is used to determine which one to execute.
 * See {@link SubCommand} for more information and requirements.
 * <p>
 * "Alphabetical Order" is determined by comparing the priority between the first parameter
 * of each method, if they are equal, the second parameter is compared, and so on.
 * If after fully comparing all parameters, there is still a tie, a runtime exception is thrown.
 * This should never happen unless your logic is flawed.
 */
@Kapi
public abstract class Command {
    
    /**
     * Checks for requirements before the command is executed.
     * If the requirements aren't met, the command will not be executed,
     * and the sender will receive the given error message.
     * <p>
     * This can be overridden to have a global requirement check for the entire command.
     * <p>
     * Tip: pass null to {@link Result#ok(Object)} to return an Ok void result.
     *
     * @param sender the sender of the command
     * @return a result containing nothing or an error message if the requirements aren't met
     */
    @Kapi
    public Result<Void,String> checkRequirements(CommandSender sender) {
        return Result.ok(null);
    }
    
    /**
     * Called when no {@link SubCommand} methods match the input.
     * <p>
     * This method is usually used to report the available subcommands to the sender.
     *
     * @param label   the label of the command
     * @param sender  the sender of the command
     * @param args    the arguments of the command
     * @param methods all the {@link SubCommand} methods of the command
     */
    @Kapi
    public void onNoMethodMatches(String label, CommandSender sender, String[] args, List<Method> methods) {
        reportAvailableMethods(label, sender, methods, Command::fromParserRepresentation);
    }
    
    /**
     * Reports the available subcommands to the sender.
     * <p>
     * The order of the methods is determined by the reverse
     * order of the order the methods were declared in.
     * So the method at the top of the class will be the last in this list,
     * and the method at the bottom of the class will be the first in this list.
     *
     * @param label   the label of the command
     * @param sender  the sender of the command
     * @param methods all the {@link SubCommand} methods of the command
     * @param mapper  a function that maps a {@link Parameter} to a string
     */
    @Kapi
    protected static void reportAvailableMethods(
        String label, CommandSender sender, List<Method> methods, Function<Parameter,String> mapper
    ) {
        StringBuilder builder = new StringBuilder();
        builder.append("No subcommands match! Available subcommands:");
        for (Method method : methods) {
            builder.append("&r\n/");
            builder.append(label);
            
            for (int i = 1; i < method.getParameterCount(); i++) {
                builder.append(" ");
                builder.append(mapper.apply(method.getParameters()[i]));
            }
        }
        
        Log.error(builder.toString(), sender);
    }
    
    /**
     * @param parameter the parameter to get the name of
     * @return the name of the parameter
     */
    @Kapi
    protected static String fromParameterName(Parameter parameter) {
        return parameter.getName();
    }
    
    /**
     * @param parameter the parameter to get the parser representation of
     * @return the representation of the parameter
     */
    @Kapi
    protected static String fromParserRepresentation(Parameter parameter) {
        ArgumentParser<?> parser = ArgumentRegistry.getInstance().get(parameter.getType())
            .expect("Failed to get parser for parameter " + parameter.getType().getSimpleName());
        return parser.getRepresentation(parameter.getAnnotatedType())
            .unwrapOr(ArgumentRepresentation.of("<", "?", ">"))
            .getRepresentation();
    }
}
