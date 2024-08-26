/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package me.kyren223.kapi.utility;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.core.Kplugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jspecify.annotations.NullMarked;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * A utility class that manages configuration files.<br>
 * Allows CRUD operations on documents (.yml configs).
 */
@NullMarked
@Kapi
public class Config {
    
    private Config() {
        throw new AssertionError("Documents should not be instantiated");
    }
    
    /**
     * @return The data folder (usually "plugins/&lt;plugin&gt;")
     * @see Kplugin#getDataFolder()
     */
    @Kapi
    public static File getRoot() {
        return Kplugin.get().getDataFolder();
    }
    
    /**
     * @param path The path to the config file (relative to the data folder)
     * @return The config file
     * @see #getRoot()
     */
    @Kapi
    public static File getPath(String path) {
        return new File(getRoot(), path);
    }
    
    /**
     * @param path The path to the config file (relative to the data folder)
     * @return True if the config file exists, false otherwise
     */
    @Kapi
    public static boolean exists(String path) {
        return getPath(path).exists();
    }
    
    /**
     * Gets a config.<br>
     * If any errors occur, an empty config will be returned.
     *
     * @param path The path to the config file (relative to the data folder)
     * @return The config
     */
    @Kapi
    public static YamlConfiguration get(String path) {
        return YamlConfiguration.loadConfiguration(getPath(path));
    }
    
    /**
     * Writes to a config.<br>
     * If any errors occur, false will be returned.<br>
     * <br>
     * This method will overwrite the config if it already exists.<br>
     * For a method that will not overwrite the config,
     * see {@link #writeIfAbsent(String, YamlConfiguration)}
     *
     * @param path   The path to the config file (relative to the data folder)
     * @param config The config to write to
     * @return True if the config was written to, false otherwise
     * @see #writeIfAbsent(String, YamlConfiguration)
     */
    @Kapi
    public static boolean write(String path, YamlConfiguration config) {
        try {
            config.save(getPath(path));
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Writes to a config.<br>
     * If any errors occur, false will be returned.<br>
     * <br>
     * This method will not overwrite the config if it already exists.<br>
     * For a method that will overwrite the config,
     * see {@link #write(String, YamlConfiguration)}
     *
     * @param path   The path to the config file (relative to the data folder)
     * @param config The config to write to
     * @return True if the config was written to, false otherwise
     * @see #write(String, YamlConfiguration)
     */
    @Kapi
    public static boolean writeIfAbsent(String path, YamlConfiguration config) {
        if (exists(path)) {
            return false;
        }
        return write(path, config);
    }
    
    /**
     * @param path The path to the config file (relative to the data folder)
     * @return True if the config file was deleted, false otherwise
     */
    @Kapi
    public static boolean delete(String path) {
        return getPath(path).delete();
    }
    
    /**
     * Modifies a config.<br>
     * If any errors occur, false will be returned.<br>
     * <br>
     * Reads the config, passes it to the consumer, and then writes the config back to disk.<br>
     * For more information,
     * see {@link #get(String)}
     * and {@link #write(String, YamlConfiguration)}.
     *
     * @param path     The path to the config file (relative to the data folder)
     * @param consumer The consumer to apply to the config
     * @return True if the config was able to be written to disk, false otherwise
     * @see #get(String)
     * @see #write(String, YamlConfiguration)
     */
    @Kapi
    public static boolean getAndWrite(String path, Consumer<YamlConfiguration> consumer) {
        YamlConfiguration config = get(path);
        consumer.accept(config);
        return write(path, config);
    }
}
