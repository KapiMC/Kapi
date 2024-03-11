package me.kyren223.kapi.utility;


import me.kyren223.kapi.KPlugin;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class Log {
    
    private Log() {
        // Prevent instantiation
    }
    
    private static String prefix = "";
    
    private static String format(String message, String color) {
        return prefix + Utils.col(color + message);
    }
    
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
    
    public static void error(String message) {
        KPlugin.get().getLogger().severe(getError(message));
    }
    
    public static void error(String message, Player... players) {
        for (Player player : players) {
            player.sendMessage(getError(message));
        }
    }
    
    public static void broadcastError(String message) {
        KPlugin.get().getServer().broadcastMessage(getError(message));
    }
    
    public static void warn(String message) {
        KPlugin.get().getLogger().warning(getWarn(message));
    }
    
    public static void warn(String message, Player... players) {
        for (Player player : players) {
            player.sendMessage(getWarn(message));
        }
    }
    
    public static void broadcastWarn(String message) {
        KPlugin.get().getServer().broadcastMessage(getWarn(message));
    }
    
    public static void info(String message) {
        KPlugin.get().getLogger().info(getInfo(message));
    }
    
    public static void info(String message, Player... players) {
        for (Player player : players) {
            player.sendMessage(getInfo(message));
        }
    }
    
    public static void broadcastInfo(String message) {
        KPlugin.get().getServer().broadcastMessage(getInfo(message));
    }
    
    public static void success(String message) {
        KPlugin.get().getLogger().info(getSuccess(message));
    }
    
    public static void success(String message, Player... players) {
        for (Player player : players) {
            player.sendMessage(getSuccess(message));
        }
    }
    
    public static void broadcastSuccess(String message) {
        KPlugin.get().getServer().broadcastMessage(getSuccess(message));
    }
    
    public static void debug(String message) {
        if (!KPlugin.get().isDebug()) return;
        KPlugin.get().getLogger().log(Level.INFO, getDebug(message));
    }
    
    public static void debug(String message, Player... players) {
        if (!KPlugin.get().isDebug()) return;
        for (Player player : players) {
            player.sendMessage(getDebug(message));
        }
    }
    
    public static void broadcastDebug(String message) {
        if (!KPlugin.get().isDebug()) return;
        KPlugin.get().getServer().broadcastMessage(getDebug(message));
    }
    
    public static void log(String message) {
        KPlugin.get().getLogger().info(getLog(message));
    }
    
    public static void log(String message, Player... players) {
        for (Player player : players) {
            player.sendMessage(getLog(message));
        }
    }
    
    public static void broadcastLog(String message) {
        KPlugin.get().getServer().broadcastMessage(getLog(message));
    }
    
}
