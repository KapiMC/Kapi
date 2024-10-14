/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.core;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.commands.Command;
import io.github.kapimc.kapi.commands.CommandRecord;
import io.github.kapimc.kapi.commands.CommandProcessor;
import io.github.kapimc.kapi.utility.Log;
import io.github.kapimc.kapi.utility.TaskBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.Nullable;

/**
 * This class should be extended by the main class of your plugin.
 * Extend this instead of {@link JavaPlugin}
 * <p>
 * Write your initialization code in {@link #onPluginLoad()}
 * and your unloading code in {@link #onPluginUnload()}.
 */
@Kapi
public abstract class KapiPlugin extends JavaPlugin {
    
    /**
     * The current version of Kapi.
     * Follows semantic versioning.
     */
    @Kapi
    public static final String VERSION = "0.2.1";
    
    private static @Nullable KapiPlugin plugin;
    
    @Override
    public final void onEnable() {
        plugin = this;
        Log.info("Enabling Kapi v" + VERSION);
        
        try {
            onPluginPreload();
        } catch (RuntimeException e) {
            Log.error("An error occurred while preloading the plugin!");
            throw e;
        }
        
        // Runs on the next server tick
        TaskBuilder.create(() -> {
            try {
                onPluginLoad();
            } catch (RuntimeException e) {
                Log.error("An error occurred while loading the plugin!");
                throw e;
            }
        }).schedule();
    }
    
    @Override
    public final void onDisable() {
        try {
            onPluginUnload();
        } catch (RuntimeException e) {
            Log.error("An error occurred while unloading the plugin!");
            e.printStackTrace();
            Log.warn("Attempting to unload Kapi anyway...");
        }
        Log.info("Disabling Kapi v" + VERSION);
        plugin = null;
    }
    
    /**
     * Called immediately after Kapi has been fully loaded,
     * in the same server tick as the onEnable method.
     * <p>
     * For initializations, it's recommended to use {@link #onPluginLoad()} instead of this method,
     * this is due to how Bukkit/Spigot works, some methods like {@link Bukkit#broadcastMessage(String)}
     * will not work if called in the onEnable method.
     */
    @Kapi
    public void onPluginPreload() {
        // Default implementation does nothing
        // If users need this, they can override this method
    }
    
    /**
     * Called one server tick after Kapi loads and {@link #onPluginPreload()} finishes.
     * This method should be used for initialization of the plugin.
     */
    @Kapi
    public abstract void onPluginLoad();
    
    /**
     * Called when the plugin is about to be disabled.
     * Kapi unloads only after this method finishes,
     * so you can still use Kapi methods here.
     */
    @Kapi
    public abstract void onPluginUnload();
    
    /**
     * @return the instance of the plugin
     * @throws IllegalStateException if the plugin has not been enabled yet
     */
    @Kapi
    public static KapiPlugin get() {
        if (plugin == null) {
            throw new IllegalStateException("Kapi has not been enabled yet!");
        }
        return plugin;
    }
    
    /**
     * Registers the given event listener.
     *
     * @param listener an instance of a class that implements {@link Listener}
     */
    @Kapi
    public void registerEvent(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }
    
    /**
     * Registers a Minecraft command.
     *
     * @param name     the name of the command
     * @param executor the executor for the command
     */
    @Kapi
    public void registerCommand(String name, CommandExecutor executor) {
        PluginCommand command = getCommand(name);
        if (command == null) {
            throw new IllegalArgumentException(
                "Command " + name + " does not exist! (did you add it to the plugin.yml file?)");
        }
        
        command.setExecutor(executor);
    }
    
    /**
     * Registers a Minecraft command.
     *
     * @param name      the name of the command
     * @param executor  the executor for the command
     * @param completer the tab completer for the command or null
     */
    @Kapi
    public void registerCommand(String name, CommandExecutor executor, TabCompleter completer) {
        PluginCommand command = getCommand(name);
        if (command == null) {
            throw new IllegalArgumentException(
                "Command " + name + " does not exist! (did you add it to the plugin.yml file?)");
        }
        
        command.setExecutor(executor);
        command.setTabCompleter(completer);
    }
    
    /**
     * Registers a command.
     *
     * @param command an instance of a command class, which must be annotated with {@link Command}
     */
    @Kapi
    public void registerCommand(String name, Command command) {
        CommandRecord record = CommandProcessor.process(command);
        registerCommand(
            name,
            (sender, _cmd, label, args) -> {
                record.onCommand(sender, args, label);
                return true;
            },
            (sender, _cmd, _label, args) -> record.onTabComplete(sender, args)
        );
    }
}
