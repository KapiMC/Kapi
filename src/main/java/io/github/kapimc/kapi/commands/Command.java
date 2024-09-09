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
import org.bukkit.command.CommandSender;

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
public interface Command {
    
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
    default Result<Void,String> checkRequirements(CommandSender sender) {
        return Result.ok(null);
    }
}
