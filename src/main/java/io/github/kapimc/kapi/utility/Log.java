/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.utility;


import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.core.KapiPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * A utility class that includes a bunch of useful methods for logging messages.
 */
@Kapi
public final class Log {
    
    /**
     * Represents the different log levels.
     * The order from lowest to highest is: DEBUG, INFO, WARN, ERROR.
     */
    @Kapi
    public enum Level {
        /**
         * Lowest log level, logs debug, info, warnings and errors.
         */
        @Kapi
        DEBUG,
        
        /**
         * Default log level, logs info, warnings and errors.
         */
        @Kapi
        INFO,
        
        /**
         * Medium log level, logs warnings and errors.
         */
        @Kapi
        WARN,
        
        /**
         * Highest log level, logs errors.
         */
        @Kapi
        ERROR,
        ;
        
        private ChatColor getColor() {
            return switch (this) {
                case DEBUG -> ChatColor.DARK_GREEN;
                case INFO -> ChatColor.WHITE;
                case WARN -> ChatColor.YELLOW;
                case ERROR -> ChatColor.RED;
            };
        }
    }
    
    private static Level currentLevel = Level.INFO;
    
    private Log() {
        throw new AssertionError("Log should not be instantiated");
    }
    
    /**
     * @return the current log level
     */
    @Kapi
    public static Level getLevel() {
        return currentLevel;
    }
    
    /**
     * The minimum log level to log messages.
     * Messages with a lower log level will not be logged.
     *
     * @param level the log level to set
     */
    @Kapi
    public static void setLevel(Level level) {
        Log.currentLevel = level;
    }
    
    /**
     * Logs the message to the console with the given level
     * if the level is higher or equal to the current log level.
     *
     * @param level   the log level to log the message with
     * @param message the message to log
     */
    @Kapi
    public static void log(Level level, String message) {
        if (level.ordinal() < currentLevel.ordinal()) return;
        String prefix = "[" + KapiPlugin.get().getDescription().getName() + "] ";
        KapiPlugin.get()
            .getServer()
            .getConsoleSender()
            .sendMessage(prefix + level.getColor() + Utils.col(message));
    }
    
    /**
     * Broadcasts the message to all players on the server with the given level
     * if the level is higher or equal to the current log level.
     * <p>
     * Supports color codes such as &amp;c for red.
     *
     * @param level   the log level to broadcast the message with
     * @param message the message to broadcast
     * @param senders the senders to broadcast the message to
     */
    @Kapi
    public static void log(Level level, String message, CommandSender... senders) {
        if (level.ordinal() < currentLevel.ordinal()) return;
        for (CommandSender sender : senders) {
            sender.sendMessage(level.getColor() + Utils.col(message));
        }
    }
    
    /**
     * Broadcasts the message to all players on the server with the given level
     * if the level is higher or equal to the current log level.
     * <p>
     * Supports color codes such as &amp;c for red.
     * <p>
     * This method uses the server's {@code broadcastMessage} API.
     *
     * @param level   the log level to broadcast the message with
     * @param message the message to broadcast
     */
    @Kapi
    public static void broadcast(Level level, String message) {
        if (level.ordinal() < currentLevel.ordinal()) return;
        KapiPlugin.get().getServer().broadcastMessage(level.getColor() + Utils.col(message));
    }
    
    /**
     * Logs the message to the console with {@link Level#DEBUG},
     * see {@link Log#log(Level, String)} for more information.
     *
     * @param message the message to log
     */
    @Kapi
    public static void debug(String message) {
        log(Level.DEBUG, message);
    }
    
    /**
     * Logs the message to the console with {@link Level#INFO},
     * see {@link Log#log(Level, String)} for more information.
     *
     * @param message the message to log
     */
    @Kapi
    public static void info(String message) {
        log(Level.INFO, message);
    }
    
    /**
     * Logs the message to the console with {@link Level#WARN},
     * see {@link Log#log(Level, String)} for more information.
     *
     * @param message the message to log
     */
    @Kapi
    public static void warn(String message) {
        log(Level.WARN, message);
    }
    
    /**
     * Logs the message to the console with {@link Level#ERROR},
     * see {@link Log#log(Level, String)} for more information.
     *
     * @param message the message to log
     */
    @Kapi
    public static void error(String message) {
        log(Level.ERROR, message);
    }
    
    /**
     * Broadcasts the message to all players on the server with {@link Level#DEBUG},
     * see {@link Log#broadcast(Level, String)} for more information.
     *
     * @param message the message to broadcast
     */
    @Kapi
    public static void broadcastDebug(String message) {
        broadcast(Level.DEBUG, message);
    }
    
    /**
     * Broadcasts the message to all players on the server with {@link Level#INFO},
     * see {@link Log#broadcast(Level, String)} for more information.
     *
     * @param message the message to broadcast
     */
    @Kapi
    public static void broadcastInfo(String message) {
        broadcast(Level.INFO, message);
    }
    
    /**
     * Broadcasts the message to all players on the server with {@link Level#WARN},
     * see {@link Log#broadcast(Level, String)} for more information.
     *
     * @param message the message to broadcast
     */
    @Kapi
    public static void broadcastWarn(String message) {
        broadcast(Level.WARN, message);
    }
    
    /**
     * Broadcasts the message to all players on the server with {@link Level#ERROR},
     * see {@link Log#broadcast(Level, String)} for more information.
     *
     * @param message the message to broadcast
     */
    @Kapi
    public static void broadcastError(String message) {
        broadcast(Level.ERROR, message);
    }
    
    /**
     * Logs the message to the console with {@link Level#DEBUG},
     * see {@link Log#log(Level, String, CommandSender...)} for more information.
     *
     * @param message the message to log
     * @param senders the senders to log the message to
     */
    @Kapi
    public static void debug(String message, CommandSender... senders) {
        log(Level.DEBUG, message, senders);
    }
    
    /**
     * Logs the message to the console with {@link Level#INFO},
     * see {@link Log#log(Level, String, CommandSender...)} for more information.
     *
     * @param message the message to log
     * @param senders the senders to log the message to
     */
    @Kapi
    public static void info(String message, CommandSender... senders) {
        log(Level.INFO, message, senders);
    }
    
    /**
     * Logs the message to the console with {@link Level#WARN},
     * see {@link Log#log(Level, String, CommandSender...)} for more information.
     *
     * @param message the message to log
     * @param senders the senders to log the message to
     */
    @Kapi
    public static void warn(String message, CommandSender... senders) {
        log(Level.WARN, message, senders);
    }
    
    /**
     * Logs the message to the console with {@link Level#ERROR},
     * see {@link Log#log(Level, String, CommandSender...)} for more information.
     *
     * @param message the message to log
     * @param senders the senders to log the message to
     */
    @Kapi
    public static void error(String message, CommandSender... senders) {
        log(Level.ERROR, message, senders);
    }
}
