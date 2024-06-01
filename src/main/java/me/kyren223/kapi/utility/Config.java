/*
 * Copyright (c) 2024 Kapi Contributors. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted if the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions, the following disclaimer and the list of contributors.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation and/or
 *    other materials provided with the distribution.
 *
 * 3. The buyer of the "Kapi" API is granted the right to use this software
 *    as a dependency in their own software projects. However, the buyer
 *    may not resell or distribute the "Kapi" API, in whole or in part, to other parties.
 *
 * 4. The buyer may include the "Kapi" API in a "fat jar" along with their own code.
 *    The license for the "fat jar" is at the buyer's discretion and may allow
 *    redistribution of the "fat jar", but the "Kapi" API code inside the "fat jar"
 *    must not be modified.
 *
 * 5. Neither the name "Kapi" nor the names of its contributors may be used to endorse
 *    or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY "Kapi" API, AND ITS CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL "Kapi" API, AND CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Kapi Contributors:
 * - Kyren223
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
