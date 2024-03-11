package me.kyren223.kapi.utility;

import me.kyren223.kapi.KPlugin;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class CommandRegistry {
    
    public void register(String name, CommandExecutor executor) {
        PluginCommand command = KPlugin.get().getCommand(name);
        if (command == null) {
            throw new IllegalArgumentException("Command " + name + " does not exist!");
        }
        command.setExecutor(executor);
    }
    
    public void register(String name, CommandExecutor executor, TabCompleter completer) {
        PluginCommand command = KPlugin.get().getCommand(name);
        if (command == null) {
            throw new IllegalArgumentException("Command " + name + " does not exist!");
        }
        command.setExecutor(executor);
        command.setTabCompleter(completer);
    }
    
    public void register(String name, BiConsumer<CommandSender, String[]> executor) {
        register(name, (sender, command, label, args) -> {
            executor.accept(sender, args);
            return true;
        });
    }
    
    public void register(String name, BiConsumer<CommandSender, String[]> executor, BiFunction<CommandSender, String[], List<String>> completer) {
        register(name, (sender, command, label, args) -> {
            executor.accept(sender, args);
            return true;
        }, (sender, command, alias, args) -> completer.apply(sender, args));
    }
}
