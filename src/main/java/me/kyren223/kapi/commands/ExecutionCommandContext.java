package me.kyren223.kapi.commands;

import me.kyren223.kapi.annotations.Kapi;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Represents the context of a command during execution (CommandExecutor).<br>
 * See {@link SuggestionCommandContext} for "TabCompleter" context.<br>
 * See {@link CommandContext} for a general command context.
 */
@Kapi
public class ExecutionCommandContext extends CommandContext {
    
    private boolean returnValue;
    
    public ExecutionCommandContext(CommandSender sender, Command command, String label, String[] args) {
        super(sender, command, label, args);
        returnValue = true;
    }
    
    /**
     * Gets the return value of the command execution.<br>
     * This return value is what the CommandExecutor will return.
     *
     * @return the return value
     */
    @Kapi
    public boolean getReturnValue() {
        return returnValue;
    }
    
    /**
     * Sets the return value of the command execution.<br>
     * This return value is what the CommandExecutor will return.
     *
     * @param returnValue the return value
     */
    @Kapi
    public void setReturnValue(boolean returnValue) {
        this.returnValue = returnValue;
    }
    
}
