package me.kyren223.kapi.utility;

import me.kyren223.kapi.annotations.Kapi;
import org.bukkit.ChatColor;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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
    
    /**
     * Checks if a regex is valid<br>
     * Note: may impact performance if used frequently on invalid regexes due to using exceptions for validation
     *
     * @param regex The regex to check
     * @return Whether the regex is valid
     */
    public static boolean isValidRegex(String regex) {
        try {
            Pattern.compile(regex);
            return true;
        } catch (PatternSyntaxException e) {
            return false;
        }
    }
}
