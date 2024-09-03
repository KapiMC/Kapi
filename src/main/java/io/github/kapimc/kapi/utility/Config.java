/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.utility;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.core.KapiPlugin;
import io.github.kapimc.kapi.data.Result;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * A utility class for working with config (YAML) files.
 */
@Kapi
public final class Config {
    
    private Config() {
        throw new AssertionError("Documents should not be instantiated");
    }
    
    /**
     * @param path the path to the config file (relative to the data folder)
     * @return the config file
     * @see KapiPlugin#getDataFolder()
     */
    @Kapi
    public static File getPath(String path) {
        return new File(KapiPlugin.get().getDataFolder(), path);
    }
    
    /**
     * @param path the path to the config file (relative to the data folder)
     * @return true if the config file exists on disk, false otherwise
     */
    @Kapi
    public static boolean exists(String path) {
        return getPath(path).exists();
    }
    
    /**
     * @param path the path to the config file (relative to the data folder)
     * @return the config, or an empty config if any errors occur
     */
    @Kapi
    public static YamlConfiguration get(String path) {
        return YamlConfiguration.loadConfiguration(getPath(path));
    }
    
    /**
     * Saves the config to the given path.
     * <p>
     * This method will overwrite the config if it already exists.
     * See {@link #writeIfAbsent(String, YamlConfiguration)}
     * for a method that will not overwrite the config.
     *
     * @param path   the path to the config file (relative to the data folder)
     * @param config the config to write to the file
     * @return an Err result in case of an IOException, otherwise Ok (Void)
     */
    @Kapi
    public static Result<Void,IOException> write(String path, YamlConfiguration config) {
        try {
            config.save(getPath(path));
            return Result.ok(null);
        } catch (IOException e) {
            return Result.err(e);
        }
    }
    
    /**
     * Saves the config to the given path if a file at the given path doesn't exist.
     *
     * @param path   the path to the config file (relative to the data folder)
     * @param config the config to write to the file
     * @return an Option containing the IOException on failure, otherwise None
     */
    @Kapi
    public static Result<Void,IOException> writeIfAbsent(String path, YamlConfiguration config) {
        if (exists(path)) {
            return Result.ok(null);
        }
        return write(path, config);
    }
    
    /**
     * @param path the path to the config file (relative to the data folder)
     * @return true if the config file was deleted, false otherwise
     */
    @Kapi
    public static boolean delete(String path) {
        return getPath(path).delete();
    }
    
    /**
     * Reads the config, passes it to the function for modification,
     * and then writes the config back to disk.
     *
     * @param path     the path to the config file (relative to the data folder)
     * @param consumer the consumer to apply to the config
     * @return an Err result in case of an IOException, otherwise Ok (Void)
     */
    @Kapi
    public static Result<Void,IOException> getAndWrite(String path, Consumer<YamlConfiguration> consumer) {
        YamlConfiguration config = get(path);
        consumer.accept(config);
        return write(path, config);
    }
}
