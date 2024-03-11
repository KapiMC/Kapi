package me.kyren223.kapi.utility;

import org.bukkit.ChatColor;

public class Utils {
    
    private Utils() {
        // Prevent instantiation
    }
    
    /**
     * Replaces all color codes starting with '&' with the corresponding color
     *
     * @param s A string to be colored
     * @return The colored string
     */
    public static String col(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
