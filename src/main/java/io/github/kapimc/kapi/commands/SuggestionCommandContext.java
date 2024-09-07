/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.commands;

import io.github.kapimc.kapi.annotations.Kapi;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the context of a command during suggestion (TabCompleter).<br>
 * See {@link ExecutionCommandContext} for "CommandExecutor" context.<br>
 * See {@link CommandContext} for a general command context.
 */
@Kapi
@ApiStatus.Internal
public class SuggestionCommandContext extends CommandContext {
    
    private final List<String> returnValue;
    
    public SuggestionCommandContext(CommandSender sender, Command command, String label, String[] args) {
        super(sender, command, label, args);
        returnValue = new ArrayList<>();
    }
    
    public List<String> getReturnValue() {
        return returnValue;
    }
    
    /**
     * Adds a suggestion to the list of suggestions.
     *
     * @param suggestion the suggestion
     */
    @Kapi
    public void addSuggestion(String suggestion) {
        returnValue.add(suggestion);
    }
    
}
