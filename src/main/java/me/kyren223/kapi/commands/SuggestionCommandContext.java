package me.kyren223.kapi.commands;

import me.kyren223.kapi.annotations.Kapi;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the context of a command during suggestion (TabCompleter).<br>
 * See {@link ExecutionCommandContext} for "CommandExecutor" context.<br>
 * See {@link CommandContext} for a general command context.
 */
@Kapi
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
    public void addSuggestion(String suggestion) {
        returnValue.add(suggestion);
    }
    
}
