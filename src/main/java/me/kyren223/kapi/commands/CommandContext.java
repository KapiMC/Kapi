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
import me.kyren223.kapi.data.Option;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents the context of a command.<br>
 * See {@link ExecutionCommandContext} for "CommandExecutor" context.<br>
 * and {@link SuggestionCommandContext} for "TabCompleter" context.<br>
 * <br>
 * This class can be used when it doesn't matter if
 * the context is for execution or suggestion.
 */
@Kapi
@ApiStatus.Internal
@NullMarked
public class CommandContext {
    
    private final CommandSender sender;
    private final Command command;
    private final String label;
    private final String[] args;
    private final Map<String,Object> arguments;
    
    public CommandContext(CommandSender sender, Command command, String label, String[] args) {
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args;
        this.arguments = new LinkedHashMap<>();
    }
    
    void addArgument(String name, Object value) {
        arguments.put(name, value);
    }
    
    @Kapi
    public CommandSender getSender() {
        return sender;
    }
    
    @Kapi
    public Command getCommand() {
        return command;
    }
    
    @Kapi
    public String getLabel() {
        return label;
    }
    
    /**
     * This method returns the "original" arguments of the command.<br>
     * It's recommended to use {@link #getArg(String, Class)} instead.
     *
     * @return The arguments of the command.
     */
    @Kapi
    public String[] getArgs() {
        return args;
    }
    
    /**
     * This method returns the number of arguments of the command.<br>
     *
     * @return The number of arguments of the command.
     */
    @Kapi
    public int getArgsCount() {
        return args.length;
    }
    
    /**
     * Returns the "original" argument at the specified index.<br>
     * It's recommended to use {@link #getArg(String, Class)} instead.
     *
     * @param index The index of the argument.
     * @return The argument at the specified index.
     * @throws ArrayIndexOutOfBoundsException If the index is out of bounds.
     */
    @Kapi
    public String getArg(int index) {
        return args[index];
    }
    
    /**
     * @return True if the sender is an instance of a player, false otherwise.
     */
    @Kapi
    public boolean isSenderPlayer() {
        return sender instanceof Player;
    }
    
    /**
     * Gets the player instance of the sender.<br>
     *
     * @return A player or none if the sender is not a player.
     */
    @Kapi
    public Option<Player> getPlayer() {
        if (sender instanceof Player player) {
            return Option.some(player);
        } else return Option.none();
    }
    
    /**
     * Gets a parsed argument by name.<br>
     * <br>
     * Note, only previous and current arguments can be accessed.<br>
     * If you need to "look ahead" for logic, you should use
     * {@link #getArg(int)} or {@link #getArgs()} instead.<br>
     * These methods return the original string arguments which spigot provided.<br>
     * <br>
     *
     * @param name  The name of the argument.
     * @param clazz The class of the argument.
     * @param <T>   The type of the argument.
     * @return The parsed argument or none if the argument is not found.
     */
    @Kapi
    @SuppressWarnings("unchecked")
    public <T> Option<T> getArg(String name, Class<T> clazz) {
        Object value = arguments.get(name);
        if (value == null) return Option.none();
        if (!clazz.isInstance(value)) return Option.none();
        return Option.some((T) value);
    }
    
    /**
     * Gets a parsed argument by name.<br>
     * <br>
     * Note, only previous and current arguments can be accessed.<br>
     * If you need to "look ahead" for logic, you should use
     * {@link #getArg(int)} or {@link #getArgs()} instead.<br>
     * These methods return the original string arguments which spigot provided.<br>
     * <br>
     *
     * @param name The name of the argument.
     * @return The parsed argument or none if the argument is not found.
     */
    @Kapi
    public Option<Object> getArg(String name) {
        return Option.of(arguments.get(name));
    }
    
    
}
