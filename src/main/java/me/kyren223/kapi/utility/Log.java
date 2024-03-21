package me.kyren223.kapi.utility;


import me.kyren223.kapi.KPlugin;
import me.kyren223.kapi.annotations.Kapi;
import org.bukkit.entity.Player;

import java.util.logging.Level;

/**
 * A utility class that includes a bunch of useful methods for logging messages.<br>
 * <br>
 * Note: Console messages use the plugin's logger through Bukkit's API,
 * not System.out.println or System.err.println.<br>
 */
@Kapi
public class Log {
    
    private Log() {
        // Prevent instantiation
    }
    
    private static String prefix = "";
    
    private static String format(String message, String color) {
        return prefix + Utils.col(color + message);
    }
    
    /**
     * Sets the prefix for all log messages, supports color codes.<br>
     * <br>
     * Note: No whitespace is added after the prefix,
     * so make sure to add a space at the end of the prefix if needed.<br>
     * <br>
     * Example: Log.setPrefix("&8[&bKAPI&8] &r");
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
        KPlugin.get().getLogger().severe(() -> getError(message));
    }
    
    /**
     * Logs an error message to a list of players.<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;c` color code before the message.
     *
     * @param message The message to log
     */
    @Kapi
    public static void error(String message, Player... players) {
        for (Player player : players) {
            player.sendMessage(getError(message));
        }
    }
    
    /**
     * Logs an error message to all players on the server (through Bukkit's Broadcast).<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;c` color code before the message.
     *
     * @param message The message to log
     */
    @Kapi
    public static void broadcastError(String message) {
        KPlugin.get().getServer().broadcastMessage(getError(message));
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
        KPlugin.get().getLogger().warning(() -> getWarn(message));
    }
    
    /**
     * Logs a warning message to a list of players.<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;e` color code before the message.
     *
     * @param message The message to log
     */
    @Kapi
    public static void warn(String message, Player... players) {
        for (Player player : players) {
            player.sendMessage(getWarn(message));
        }
    }
    
    /**
     * Logs a warning message to all players on the server (through Bukkit's Broadcast).<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;e` color code before the message.
     *
     * @param message The message to log
     */
    @Kapi
    public static void broadcastWarn(String message) {
        KPlugin.get().getServer().broadcastMessage(getWarn(message));
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
        KPlugin.get().getLogger().info(() -> getInfo(message));
    }
    
    /**
     * Logs an info message to a list of players.<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;9` color code before the message.
     *
     * @param message The message to log
     */
    @Kapi
    public static void info(String message, Player... players) {
        for (Player player : players) {
            player.sendMessage(getInfo(message));
        }
    }
    
    /**
     * Logs an info message to all players on the server (through Bukkit's Broadcast).<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;9` color code before the message.
     *
     * @param message The message to log
     */
    @Kapi
    public static void broadcastInfo(String message) {
        KPlugin.get().getServer().broadcastMessage(getInfo(message));
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
        KPlugin.get().getLogger().info(() -> getSuccess(message));
    }
    
    /**
     * Logs a success message to a list of players.<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;a` color code before the message.
     *
     * @param message The message to log
     */
    @Kapi
    public static void success(String message, Player... players) {
        for (Player player : players) {
            player.sendMessage(getSuccess(message));
        }
    }
    
    /**
     * Logs a success message to all players on the server (through Bukkit's Broadcast).<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;a` color code before the message.
     *
     * @param message The message to log
     */
    @Kapi
    public static void broadcastSuccess(String message) {
        KPlugin.get().getServer().broadcastMessage(getSuccess(message));
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
        if (!KPlugin.get().isDebug()) return;
        KPlugin.get().getLogger().log(Level.INFO, () -> getDebug(message));
    }
    
    /**
     * Logs a debug message to a list of players.<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It applies `&amp;3` color code before the message.<br>
     * <br>
     * This method will only log if the plugin is in debug mode.
     *
     * @param message The message to log
     */
    @Kapi
    public static void debug(String message, Player... players) {
        if (!KPlugin.get().isDebug()) return;
        for (Player player : players) {
            player.sendMessage(getDebug(message));
        }
    }
    
    /**
     * Logs a debug message to all players on the server (through Bukkit's Broadcast).<br>
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
        if (!KPlugin.get().isDebug()) return;
        KPlugin.get().getServer().broadcastMessage(getDebug(message));
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
        KPlugin.get().getLogger().info(getLog(message));
    }
    
    /**
     * Logs a message to a list of players.<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It does not apply any color codes.
     *
     * @param message The message to log
     */
    @Kapi
    public static void log(String message, Player... players) {
        for (Player player : players) {
            player.sendMessage(getLog(message));
        }
    }
    
    /**
     * Logs a message to all players on the server (through Bukkit's Broadcast).<br>
     * This method is prefixed by the plugin's prefix,
     * see {@link Log#setPrefix(String)}<br>
     * It does not apply any color codes.
     *
     * @param message The message to log
     */
    @Kapi
    public static void broadcastLog(String message) {
        KPlugin.get().getServer().broadcastMessage(getLog(message));
    }
    
}
