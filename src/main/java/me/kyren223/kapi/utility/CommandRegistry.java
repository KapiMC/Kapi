package me.kyren223.kapi.utility;

import me.kyren223.kapi.KPlugin;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

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
}
