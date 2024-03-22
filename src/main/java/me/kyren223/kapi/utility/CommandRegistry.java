package me.kyren223.kapi.utility;

import me.kyren223.kapi.KPlugin;
import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.annotations.ScheduledForRefactor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * A utility class for registering commands.
 */
@Kapi
@ScheduledForRefactor
public class CommandRegistry {
    
    /**
     * Registers a command
     * @param name The name of the command
     * @param executor The executor for the command
     */
    @Kapi
    @ScheduledForRefactor
    public void register(String name, CommandExecutor executor) {
        PluginCommand command = KPlugin.get().getCommand(name);
        if (command == null) {
            throw new IllegalArgumentException("Command " + name + " does not exist!");
        }
        command.setExecutor(executor);
    }
    
    /**
     * Registers a command
     * @param name The name of the command
     * @param executor The executor for the command
     * @param completer The tab completer for the command
     */
    @Kapi
    @ScheduledForRefactor
    public void register(String name, CommandExecutor executor, TabCompleter completer) {
        PluginCommand command = KPlugin.get().getCommand(name);
        if (command == null) {
            throw new IllegalArgumentException("Command " + name + " does not exist!");
        }
        command.setExecutor(executor);
        command.setTabCompleter(completer);
    }
    
    /**
     * Registers a command
     * <p></p>
     * The return value of the executor is always true,
     * use {@link #register(String, CommandExecutor)} if you need to return false
     * @param name The name of the command
     * @param executor A function that takes a sender and args for the command
     */
    @Kapi
    @ScheduledForRefactor
    public void register(String name, BiConsumer<CommandSender, String[]> executor) {
        register(name, (sender, command, label, args) -> {
            executor.accept(sender, args);
            return true;
        });
    }
    
    /**
     * Registers a command
     * <p></p>
     * The return value of the executor is always true,
     * use {@link #register(String, CommandExecutor, TabCompleter)} if you need to return false
     * @param name The name of the command
     * @param executor A function that takes a sender and args for the command
     * @param completer A function that takes a sender and args for the command
     *                  and returns a string list of possible completions
     */
    @Kapi
    @ScheduledForRefactor
    public void register(String name, BiConsumer<CommandSender, String[]> executor, BiFunction<CommandSender, String[], List<String>> completer) {
        register(name, (sender, command, label, args) -> {
            executor.accept(sender, args);
            return true;
        }, (sender, command, alias, args) -> completer.apply(sender, args));
    }
}
