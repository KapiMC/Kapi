package me.kyren223.kapi.commands;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.data.Result;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
public class CommandContext {
    
    private final CommandSender sender;
    private final Command command;
    private final String label;
    private final String[] args;
    private final Map<String, Object> arguments;
    
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
     * @return A Result containing the player or an error message.
     */
    @Kapi
    public Result<Player, String> getPlayer() {
        if (sender instanceof Player player) {
            return Result.ok(player);
        } else {
            return Result.err("Sender is not a player!");
        }
    }
    
    /**
     * @return The player instance of the sender or null if the sender is not a player.
     */
    @Kapi
    public Player getPlayerOrNull() {
        if (sender instanceof Player player) {
            return player;
        } else {
            return null;
        }
    }
    
    /**
     * Gets the player instance of the sender.<br>
     *
     * @return The player instance of the sender.
     * @throws IllegalStateException If the sender is not a player.
     */
    public Player getPlayerOrThrow() {
        if (sender instanceof Player player) {
            return player;
        } else {
            throw new IllegalStateException("Sender is not a player!");
        }
    }
    
    /**
     * Gets a parsed argument by name.<br>
     * <br>
     * Note, only previous and current arguments can be accessed.<br>
     * If you need to "look ahead" for logic, you should use
     * {@link #getArg(int)} or {@link #getArgs()} instead.<br>
     * These methods return the original arguments which were provided by spigot.<br>
     * <br>
     * All possible error messages:
     * <ul>
     *     <li>Argument not found: {name}</li>
     *     <li>Invalid type for argument: {name}</li>
     * </ul>
     *
     * @param name The name of the argument.
     * @param clazz The class of the argument.
     * @param <T> The type of the argument.
     * @return A Result containing the parsed argument or an error message.
     */
    @Kapi
    public <T> Result<T, String> getArg(String name, Class<T> clazz) {
        if (!arguments.containsKey(name)) {
            return Result.err("Argument not found: " + name);
        }
        
        Object value = arguments.get(name);
        if (value != null && !clazz.isInstance(value)) {
            return Result.err("Invalid type for argument: " + name);
        }
        
        return Result.ok(clazz.cast(value));
    }
    
    
}
