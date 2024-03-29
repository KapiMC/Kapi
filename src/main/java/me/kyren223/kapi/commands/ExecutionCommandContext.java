package me.kyren223.kapi.commands;

import me.kyren223.kapi.annotations.Kapi;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Represents the context of a command during execution (CommandExecutor).<br>
 * See {@link SuggestionCommandContext} for "TabCompleter" context.<br>
 * See {@link CommandContext} for a general command context.
 */
@Kapi
public class ExecutionCommandContext extends CommandContext {
    
    private boolean returnValue;
    private Consumer<ExecutionCommandContext> executor;
    
    public ExecutionCommandContext(
            ArgumentBuilder<CommandBuilder> argumentBuilder,
            BiConsumer<CommandContext, String> failureHandler,
            CommandSender sender, Command command, String label, String[] args
    ) {
        super(argumentBuilder, failureHandler, sender, command, label, args);
        returnValue = true;
        executor = argumentBuilder.getExecutor();
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
    
    public void process() {
        boolean shouldContinue = process(executor, this);
        if (shouldContinue) {
            this.executor = argumentBuilder.getExecutor();
        }
    }
}
