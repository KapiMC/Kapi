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
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

import java.util.logging.Level;

/**
 * A utility class that includes a bunch of useful methods for logging messages.<br>
 * <br>
 * Note: Console messages use the plugin's logger through Kplugin.get().getServer()'s API,
 * not System.out.println or System.err.println.
 */
@Kapi
@NullMarked
public class Log {
    
    private Log() {
        throw new AssertionError("Log should not be instantiated");
    }
    
    @SuppressWarnings("NotNullFieldNotInitialized")
    private static String prefix;
    
    private static String format(String message, String color) {
        if (prefix.isEmpty()) return "";
        return prefix + Utils.col(color + message);
    }
    
    /**
     * Sets the prefix for all log messages, supports color codes.<br>
     * <br>
     * Note: No whitespace is added after the prefix,
     * so make sure to add a space at the end of the prefix if needed.<br>
     * <br>
     * Example: Log.setPrefix("&amp;8[&amp;bKAPI&amp;8] &amp;r");
     *
     * @param prefix The prefix for all log messages
     */
    @Kapi
    public static void setPrefix(String prefix) {
        Log.prefix = Utils.col(prefix);
    }
    
    private static String getError(String message) {
        return format(message, "&c");
    }
    
    private static String getWarn(String message) {
        return format(message, "&e");
    }
    
    private static String getInfo(String message) {
        return format(message, "&9");
    }
    
    private static String getSuccess(String message) {
        return format(message, "&a");
    }
    
    private static String getDebug(String message) {
        return format(message, "&3");
    }
    
    private static String getLog(String message) {
        return format(message, "");
    }
    
    /**
     * Logs an error message to the console.<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;c` color code before the message.
     *
     * @param message The message to log
     */
    @Kapi
    public static void error(String message) {
        Kplugin.get().getLogger().severe(() -> getError(message));
    }
    
    /**
     * Logs an error message to a list of senders.<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;c` color code before the message.
     *
     * @param message The message to log
     */
    @Kapi
    public static void error(String message, CommandSender... senders) {
        for (CommandSender sender : senders) {
            sender.sendMessage(getError(message));
        }
    }
    
    /**
     * Logs an error message to all senders on the server (through Kplugin.get().getServer()'s
     * Broadcast).<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;c` color code before the message.
     *
     * @param message The message to log
     */
    @Kapi
    public static void broadcastError(String message) {
        Kplugin.get().getServer().broadcastMessage(getError(message));
    }
    
    /**
     * Logs a warning message to the console.<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;e` color code before the message.
     *
     * @param message The message to log
     */
    @Kapi
    public static void warn(String message) {
        Kplugin.get().getLogger().warning(() -> getWarn(message));
    }
    
    /**
     * Logs a warning message to a list of senders.<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;e` color code before the message.
     *
     * @param message The message to log
     */
    @Kapi
    public static void warn(String message, CommandSender... senders) {
        for (CommandSender sender : senders) {
            sender.sendMessage(getWarn(message));
        }
    }
    
    /**
     * Logs a warning message to all senders on the server (through Kplugin.get().getServer()'s
     * Broadcast).<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;e` color code before the message.
     *
     * @param message The message to log
     */
    @Kapi
    public static void broadcastWarn(String message) {
        Kplugin.get().getServer().broadcastMessage(getWarn(message));
    }
    
    /**
     * Logs an info message to the console.<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;9` color code before the message.
     *
     * @param message The message to log
     */
    @Kapi
    public static void info(String message) {
        Kplugin.get().getLogger().info(() -> getInfo(message));
    }
    
    /**
     * Logs an info message to a list of senders.<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;9` color code before the message.
     *
     * @param message The message to log
     */
    @Kapi
    public static void info(String message, CommandSender... senders) {
        for (CommandSender sender : senders) {
            sender.sendMessage(getInfo(message));
        }
    }
    
    /**
     * Logs an info message to all senders on the server (through Kplugin.get().getServer()'s
     * Broadcast).<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;9` color code before the message.
     *
     * @param message The message to log
     */
    @Kapi
    public static void broadcastInfo(String message) {
        Kplugin.get().getServer().broadcastMessage(getInfo(message));
    }
    
    /**
     * Logs a success message to the console.<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;a` color code before the message.
     *
     * @param message The message to log
     */
    @Kapi
    public static void success(String message) {
        Kplugin.get().getLogger().info(() -> getSuccess(message));
    }
    
    /**
     * Logs a success message to a list of senders.<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;a` color code before the message.
     *
     * @param message The message to log
     */
    @Kapi
    public static void success(String message, CommandSender... senders) {
        for (CommandSender sender : senders) {
            sender.sendMessage(getSuccess(message));
        }
    }
    
    /**
     * Logs a success message to all senders on the server (through Kplugin.get().getServer()'s
     * Broadcast).<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;a` color code before the message.
     *
     * @param message The message to log
     */
    @Kapi
    public static void broadcastSuccess(String message) {
        Kplugin.get().getServer().broadcastMessage(getSuccess(message));
    }
    
    /**
     * Logs a debug message to the console.<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;3` color code before the message.<br>
     * <br>
     * This method will only log if the plugin is in debug mode.
     *
     * @param message The message to log
     */
    @Kapi
    public static void debug(String message) {
        if (!Kplugin.get().isDebug()) return;
        Kplugin.get().getLogger().log(Level.INFO, () -> getDebug(message));
    }
    
    /**
     * Logs a debug message to a list of senders.<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;3` color code before the message.<br>
     * <br>
     * This method will only log if the plugin is in debug mode.
     *
     * @param message The message to log
     */
    @Kapi
    public static void debug(String message, CommandSender... senders) {
        if (!Kplugin.get().isDebug()) return;
        for (CommandSender sender : senders) {
            sender.sendMessage(getDebug(message));
        }
    }
    
    /**
     * Logs a debug message to all senders on the server (through Kplugin.get().getServer()'s
     * Broadcast).<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;3` color code before the message.<br>
     * <br>
     * This method will only log if the plugin is in debug mode.
     *
     * @param message The message to log
     */
    @Kapi
    public static void broadcastDebug(String message) {
        if (!Kplugin.get().isDebug()) return;
        Kplugin.get().getServer().broadcastMessage(getDebug(message));
    }
    
    /**
     * Logs a message to the console.<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It does not apply any color codes.
     *
     * @param message The message to log
     */
    @Kapi
    public static void log(String message) {
        Kplugin.get().getLogger().info(() -> getLog(message));
    }
    
    /**
     * Logs a message to a list of senders.<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It does not apply any color codes.
     *
     * @param message The message to log
     */
    @Kapi
    public static void log(String message, CommandSender... senders) {
        for (CommandSender sender : senders) {
            sender.sendMessage(getLog(message));
        }
    }
    
    /**
     * Logs a message to all senders on the server (through Kplugin.get().getServer()'s
     * Broadcast).<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It does not apply any color codes.
     *
     * @param message The message to log
     */
    @Kapi
    public static void broadcastLog(String message) {
        Kplugin.get().getServer().broadcastMessage(getLog(message));
    }
    
}
