/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package me.kyren223.kapi.core;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.utility.Log;
import me.kyren223.kapi.utility.Task;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * This class should be extended by the main class of your plugin.<br>
 * Extend this instead of {@link JavaPlugin}<br>
 */
@Kapi
public abstract class Kplugin extends JavaPlugin {
    
    private static @Nullable Kplugin plugin;
    
    @Override
    public void onEnable() {
        Log.setPrefix("&8[&9" + "TODO" + "I&8] &r");
        Log.info("KAPI has been enabled!");
        onPluginPreload();
        Task.run(this::onPluginLoad).delay(1).schedule();
    }
    
    @Override
    public void onDisable() {
        onPluginUnload();
        Log.info("KAPI has been disabled!");
        plugin = null;
    }
    
    /**
     * The developer's License Key for Kapi.<br>
     * This is the key you received when you purchased Kapi.<br>
     * If this is set to null, the plugin will not be loaded correctly.
     */
    @Kapi
    public abstract String getKapiDeveloperLicense();
    
    /**
     * The name of the plugin.<br>
     * Must adhere to the following regex:
     * <pre><code>[a-zA-Z_][a-zA-Z0-9_]*</code></pre>
     * <ul>
     *     <li>Starts with a letter or underscore</li>
     *     <li>Contains only letters, numbers, underscores</li>
     * </ul>
     *
     * @return The name of the plugin
     */
    @Kapi
    public abstract String getPluginName();
    
    /**
     * Called at the "preload" phase.<br>
     * Used as a replacement to the onEnable method in Bukkit plugins.<br>
     * For initializations, it's recommended to use {@link #onPluginLoad()} instead of this method.<br>
     * <br>
     * The preload phase comes after Kapi has been fully loaded,
     * it's called in the same game tick as the onEnable method.
     */
    @Kapi
    public abstract void onPluginPreload();
    
    /**
     * Called at the "load" phase.<br>
     * This method should be used for initialization of the plugin.<br>
     * {@link #onPluginPreload()} can be used only if absolutely needed, otherwise use this method.<br>
     * <br>
     * The load phase comes after the onEnable method has finished.<br>
     * It'll be called 1 tick after the onEnable method (by using a 1-tick delayed Bukkit Task).<br>
     * This means Kapi has been fully loaded and the {@link #onPluginPreload()} method has been called.
     */
    @Kapi
    public abstract void onPluginLoad();
    
    /**
     * Called at the "unload" phase.<br>
     * Used as a replacement to the onDisable method in Bukkit plugins.<br>
     * <br>
     * The unload phase comes before Kapi unloads itself, so you can still use Kapi methods here.<br>
     * It's called as the first thing in the onDisable method.<br>
     * Immediately after it, Kapi unloads and then the onDisable method finishes.
     */
    @Kapi
    public abstract void onPluginUnload();
    
    /**
     * Gets the instance of the plugin.
     *
     * @return The instance of the plugin
     * @throws IllegalStateException If the plugin has not been enabled yet
     */
    @Kapi
    public static Kplugin get() {
        if (plugin == null) {
            throw new IllegalStateException("KAPI has not been enabled yet!");
        }
        return plugin;
    }
}
