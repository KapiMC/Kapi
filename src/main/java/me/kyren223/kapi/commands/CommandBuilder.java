/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */


package me.kyren223.kapi.commands;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.core.KapiPlugin;
import me.kyren223.kapi.data.Option;
import me.kyren223.kapi.data.Pair;
import me.kyren223.kapi.data.Result;
import me.kyren223.kapi.utility.Log;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static me.kyren223.kapi.commands.builtin.LiteralArgumentType.literal;

/**
 * A utility class for building commands.
 */
@Kapi
@ApiStatus.Internal
public class CommandBuilder {
    
    private final String name;
    private final ArgumentBuilder<CommandBuilder> argumentBuilder;
    private @Nullable BiConsumer<ExecutionCommandContext,Option<String>> failureHandler;
    
    private CommandBuilder(String name) {
        this.name = name;
        this.argumentBuilder = ArgumentBuilder.construct(this);
    }
    
    /**
     * Creates a new command builder.
     *
     * @param name The name of the command
     * @return the command builder for further configuration
     */
    @Kapi
    public static ArgumentBuilder<CommandBuilder> create(String name) {
        return new CommandBuilder(name).argumentBuilder;
    }
    
    /**
     * Registers the command.
     */
    @Kapi
    public void register() {
        KapiPlugin.get().registerCommand(name, this::onCommand, this::onTabComplete);
    }
    
    /**
     * Sets the failure handler for the command.<br>
     * <br>
     * This is called whenever an error occurs, this can be used
     * to provide feedback to the sender.<br>
     * <br>
     * It's recommended to check whether a
     * {@link ExecutionCommandContext} or a {@link SuggestionCommandContext}
     * is being passed to the handler.<br>
     * As you probably don't want to send a message to the sender
     * when a suggestion is being processed.
     *
     * @param failureHandler A consumer that handles command failures
     *                       The first parameter is the context of the command
     *                       The second parameter is the error message (can be null)
     * @return the command builder for chaining
     */
    @Kapi
    public CommandBuilder onFail(BiConsumer<ExecutionCommandContext,Option<String>> failureHandler) {
        this.failureHandler = failureHandler;
        return this;
    }
    
    /**
     * Sets the failure handler to automatically log the error message.<br>
     * <br>
     * This uses {@link Log#error(String, CommandSender...)} to log the error message.<br>
     * This does nothing if the error message is null or
     * if the context is a {@link SuggestionCommandContext}.
     *
     * @return the command builder for chaining
     */
    public CommandBuilder autoFailHandler() {
        return onFail((context, message) -> {
            message.inspect(msg -> Log.error(msg, context.getSender()));
        });
    }
    
    private boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ExecutionCommandContext context = new ExecutionCommandContext(sender, command, label, args);
        
        // Using `label` and not `name` to allow the executor
        // to know the specific alias that was used
        List<String> arguments = new ArrayList<>(List.of(args));
        arguments.add(0, label);
        
        // Using `name` so the executor can search for the name argument
        // which will store the actual label that was used (which many be an alias)
        String argumentName = name;
        ArgumentBuilder<?> builder = argumentBuilder;
        Object result = literal(label).ignoreCase().parse(arguments);
        
        while (true) {
            // Check if all requirements are met
            for (Pair<Predicate<CommandContext>,Option<String>> requirement : builder.getRequirements()) {
                if (requirement.getFirst().test(context)) continue;
                
                // Failed requirement
                if (failureHandler != null) {
                    failureHandler.accept(context, requirement.getSecond());
                }
                
                return context.getReturnValue();
            }
            
            // Add the argument
            context.addArgument(argumentName, result);
            
            // If no more arguments are left, break, no need to check next args
            if (arguments.isEmpty()) {
                break;
            }
            
            // Choose the next argument
            boolean hasNext = false;
            for (Pair<ArgumentType<?>,String> nextArgKey : builder.getArgs().keySet()) {
                ArgumentType<?> nextType = nextArgKey.getFirst();
                String nextArgumentName = nextArgKey.getSecond();
                ArgumentBuilder<?> nextBuilder = builder.getArgs().get(nextArgKey);
                
                // Try to parse the next argument
                Result<?,String> nextResult = nextType.parse(arguments);
                if (nextResult.isErr()) continue;
                
                // Argument succeeded
                hasNext = true;
                result = nextResult;
                argumentName = nextArgumentName;
                builder = nextBuilder;
                break;
            }
            
            if (!hasNext) break;
        }
        
        // TODO if executor doesn't exist that means the path is invalid
        // And must be handled by the failure handler
        // Otherwise it's valid, the next args r just optional and can be ignored
        Consumer<ExecutionCommandContext> executor = builder.getExecutor();
        if (executor != null) {
            executor.accept(context);
        } else if (failureHandler != null) {
            failureHandler.accept(context, Option.none());
        }
        
        return context.getReturnValue();
    }
    
    private List<String> onTabComplete(
        CommandSender sender, Command command, String label, String[] args
    ) {
        SuggestionCommandContext context =
            new SuggestionCommandContext(sender, command, label, args);
        
        return context.getReturnValue();
    }
}
