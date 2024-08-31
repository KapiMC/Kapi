/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.utility;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.data.Option;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Contract;

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
     * Formats the string to have colors.<br>
     * Supports the following color codes:
     * <ul>
     *     <li>&amp; - converts to {@link ChatColor#COLOR_CHAR} which is '§' </li>
     *     <li>#[a-fA-F0-9]{6} - converts to hex color codes</li>
     *     <li>&amp;# - Converts to '#', used to escape hex</li>
     * </ul>
     *
     * @param s The string to color
     * @return The colored string
     */
    @Kapi
    @Contract(pure = true)
    public static String col(String s) {
        int skip = 0;
        StringBuilder sb = new StringBuilder();
        outer:
        for (int i = 0; i < s.length(); i++) {
            if (skip > 0) {
                skip--;
                continue;
            }
            
            char c = s.charAt(i);
            Option<Character> nextC = i + 1 < s.length() ? Option.some(s.charAt(i + 1)) : Option.none();
            
            if (nextC.isSomeAnd(next -> next == '#' && c == '&')) {
                // Escape # after & into just #
                sb.append('#');
                skip = 1; // Skip the next character which is the #
                continue;
            }
            
            if (c == '&') {
                sb.append(ChatColor.COLOR_CHAR);
                continue;
            }
            
            if (c == '#') {
                // Try parsing the hex color
                char[] hex = new char[6];
                for (int j = 0; j < 6; j++) {
                    if (i + j + 1 >= s.length()) {
                        sb.append(c);
                        continue outer;
                    }
                    char next = s.charAt(i + j + 1);
                    boolean isDigit = Character.isDigit(next);
                    boolean isHex = 'a' <= next && next <= 'f' || 'A' <= next && next <= 'F';
                    if (!isDigit && !isHex) {
                        sb.append(c);
                        continue outer;
                    }
                    hex[j] = s.charAt(i + j + 1);
                }
                String color = net.md_5.bungee.api.ChatColor.of("#" + new String(hex)) + "";
                sb.append(color);
            }
            
            sb.append(c);
        }
        return sb.toString();
    }
    
    /**
     * Checks if a regex is valid.<br>
     * Note: may impact performance if used frequently on invalid regexes
     * due to using exceptions for validation.
     *
     * @param regex The regex to check
     * @return True if the regex is valid, false otherwise
     */
    @Kapi
    @Contract(pure = true)
    public static boolean isValidRegex(String regex) {
        try {
            Pattern.compile(regex);
            return true;
        } catch (PatternSyntaxException e) {
            return false;
        }
    }
}
