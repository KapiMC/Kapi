/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package me.kyren223.kapi.core;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.utility.Log;
import me.kyren223.kapi.utility.Task;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.Nullable;

/**
 * This class should be extended by the main class of your plugin.<br>
 * Extend this instead of {@link JavaPlugin}<br>
 */
@Kapi
public abstract class Kplugin extends JavaPlugin {
    
    private static @Nullable Kplugin plugin;
    
    @Override
    public final void onEnable() {
        Log.info("Kapi has fully loaded!");
        try {
            onPluginPreload();
        } catch (RuntimeException e) {
            Log.error("An error occurred while preloading the plugin!");
            throw e;
        }
        Task.run(() -> {
            try {
                onPluginLoad();
            } catch (RuntimeException e) {
                Log.error("An error occurred while loading the plugin!");
                throw e;
            }
        }).delay(1).schedule();
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
        plugin = null;
        Log.info("Kapi has been unloaded!");
    }
    
    /**
     * Called immediately after Kapi has been fully loaded,
     * in the same game-tick as the onEnable method.
     * <p>
     * For initializations, it's recommended to use {@link #onPluginLoad()} instead of this method,
     * this is due to how Bukkit/Spigot works, some methods like {@link Bukkit#broadcastMessage(String)}
     * will not work if called in the onEnable method.
     *
     * @since 0.1.0
     */
    @Kapi
    public abstract void onPluginPreload();
    
    /**
     * Called 1 game-tick after Kapi loads and {@link #onPluginPreload()} finishes.
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
     * @return The instance of the plugin
     * @throws IllegalStateException If the plugin has not been enabled yet
     */
    @Kapi
    public static Kplugin get() {
        if (plugin == null) {
            throw new IllegalStateException("Kapi has not been enabled yet!");
        }
        return plugin;
    }
}
