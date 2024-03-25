package me.kyren223.kapi.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
    
    public boolean getReturnValue() {
        return returnValue;
    }
    
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
