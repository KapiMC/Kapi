/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package me.kyren223.kapi.utility;


import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.commands.CommandBuilder;
import me.kyren223.kapi.core.Kplugin;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A utility class for registering commands and event listeners.
 */
@Kapi
@NullMarked
public class KapiRegistry {
    
    private KapiRegistry() {
        throw new AssertionError("KapiRegistry should not be instantiated!");
    }
    
    /**
     * Registers a command.<br>
     * Using this method directly is not recommended,
     * use {@link CommandBuilder} instead.
     *
     * @param name      The name of the command
     * @param executor  The executor for the command
     * @param completer The tab completer for the command or null
     */
    @Kapi
    public static void register(
            String name, CommandExecutor executor, @Nullable TabCompleter completer
    ) {
        PluginCommand command = Kplugin.get().getCommand(name);
        if (command == null) {
            Log.error("Command " + name + " does not exist!");
            throw new IllegalArgumentException("Command " + name + " does not exist!");
        }
        
        command.setExecutor(executor);
        if (completer != null) command.setTabCompleter(completer);
    }
    
    /**
     * Registers a listener on the server.
     *
     * @param listener An instance of a class that implements Listener
     */
    @Kapi
    public static void register(Listener listener) {
        Kplugin.get().getServer().getPluginManager().registerEvents(listener, Kplugin.get());
    }
}
