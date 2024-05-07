/*
 * Copyright (c) 2024 Kapi Contributors. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted if the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions, the following disclaimer and the list of contributors.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation and/or
 *    other materials provided with the distribution.
 *
 * 3. The buyer of the "Kapi" API is granted the right to use this software
 *    as a dependency in their own software projects. However, the buyer
 *    may not resell or distribute the "Kapi" API, in whole or in part, to other parties.
 *
 * 4. The buyer may include the "Kapi" API in a "fat jar" along with their own code.
 *    The license for the "fat jar" is at the buyer's discretion and may allow
 *    redistribution of the "fat jar", but the "Kapi" API code inside the "fat jar"
 *    must not be modified.
 *
 * 5. Neither the name "Kapi" nor the names of its contributors may be used to endorse
 *    or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY "Kapi" API, AND ITS CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL "Kapi" API, AND CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Kapi Contributors:
 * - Kyren223
 */


package me.kyren223.kapi.commands;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.commands.builtin.LiteralArgumentType;
import me.kyren223.kapi.data.Result;
import me.kyren223.kapi.utility.KapiRegistry;
import me.kyren223.kapi.utility.Log;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * A utility class for building commands.
 */
@Kapi
@ApiStatus.Internal
// TODO Remove this annotation
@SuppressWarnings("all")
public class CommandBuilder {
    
    private final String name;
    private ArgumentBuilder<CommandBuilder> argumentBuilder;
    private BiConsumer<ExecutionCommandContext, String> failureHandler;
    
    private CommandBuilder(String name) {
        this.name = name;
    }
    
    /**
     * Creates a new command builder.
     *
     * @param name The name of the command
     * @return the command builder for further configuration
     */
    @Kapi
    public static ArgumentBuilder<CommandBuilder> create(String name) {
        CommandBuilder command = new CommandBuilder(name);
        command.argumentBuilder = ArgumentBuilder.construct(command);
        return command.argumentBuilder;
    }
    
    /**
     * Registers the command to the Kapi command registry.
     * Uses {@link KapiRegistry#register(String, CommandExecutor, TabCompleter)}.
     */
    @Kapi
    public void register() {
        KapiRegistry.register(name, this::onCommand, this::onTabComplete);
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
    public CommandBuilder onFail(BiConsumer<ExecutionCommandContext, String> failureHandler) {
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
            if (message == null) return;
            Log.error(message, context.getSender());
        });
    }
    
    private boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ExecutionCommandContext context = new ExecutionCommandContext(
                sender, command, label, args
        );
        
        ArgumentBuilder<?> builder = argumentBuilder;
        ArgumentType<?> type = LiteralArgumentType.literal(label).ignoreCase();
        String argumentName = null;
        List<String> arguments = new ArrayList<>(List.of(args));
        arguments.add(0, label);
        
        while (true) {
            // Check if all requirements are met
            for (var requirement : builder.getRequirements()) {
                if (requirement.getSecond().test(context)) continue;
                
                if (failureHandler != null) {
                    failureHandler.accept(context, requirement.getFirst());
                }
                
                return context.getReturnValue();
            }
            
            if (arguments.isEmpty()) {
                break;
            }
            
            // Parse current argument
            Result<?, String> result = type.parse(arguments);
            // TODO
            
            
        }
        
        builder.getExecutor().accept(context);
        
        return context.getReturnValue();
    }
    
    private List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        SuggestionCommandContext context = new SuggestionCommandContext(
                sender, command, label, args
        );
        
        return context.getReturnValue();
    }
}
