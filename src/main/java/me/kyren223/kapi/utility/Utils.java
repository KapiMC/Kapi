package me.kyren223.kapi.utility;

import me.kyren223.kapi.annotations.Kapi;
import org.bukkit.ChatColor;

/**
 * A utility class that includes a bunch of useful methods.
 */
@Kapi
public class Utils {
    
    private Utils() {
        throw new AssertionError("Utils should not be instantiated");
    }
    
    /**
     * Replaces color codes starting with '&amp;' with their appropriate ChatColor
     * @param s The string to color
     * @return The colored string
     */
    @Kapi
    public static String col(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
