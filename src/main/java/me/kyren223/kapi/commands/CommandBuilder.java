package me.kyren223.kapi.commands;

import me.kyren223.kapi.utility.KapiRegistry;
import me.kyren223.kapi.utility.Log;

import java.util.function.BiConsumer;

public class CommandBuilder {
    
    private final String name;
    private ArgumentBuilder<CommandBuilder> argumentBuilder;
    private BiConsumer<CommandContext, String> failureHandler;
    
    private CommandBuilder(String name) {
        this.name = name;
    }
    
    public static ArgumentBuilder<CommandBuilder> create(String name) {
        CommandBuilder command = new CommandBuilder(name);
        command.argumentBuilder = ArgumentBuilder.construct(command);
        return command.argumentBuilder;
    }
    
    public void register() {
        KapiRegistry.register(name, ((sender, command, label, args) -> {
            ExecutionCommandContext context = new ExecutionCommandContext(
                    argumentBuilder, failureHandler,
                    sender, command, label, args
            );
            context.process();
            return context.getReturnValue();
        }), (sender, command, label, args) -> {
            SuggestionCommandContext context = new SuggestionCommandContext(
                    argumentBuilder, failureHandler,
                    sender, command, label, args
            );
            context.process();
            return context.getReturnValue();
        });
    }
    
    public CommandBuilder onFail(BiConsumer<CommandContext, String> failureHandler) {
        this.failureHandler = failureHandler;
        return this;
    }
    
    public CommandBuilder autoFailHandler() {
        return onFail((context, message) -> {
            if (message == null) return;
            Log.error(message, context.getSender());
        });
    }
}
