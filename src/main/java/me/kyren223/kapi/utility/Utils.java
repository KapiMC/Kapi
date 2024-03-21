package me.kyren223.kapi.utility;

import org.bukkit.ChatColor;

public class Utils {
    
    private Utils() {
        // Prevent instantiation
    }
    
    /**
     * Replaces color codes starting with '&amp;' with their appropriate ChatColor
     * @param s The string to color
     * @return The colored string
     */
    public static String col(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
