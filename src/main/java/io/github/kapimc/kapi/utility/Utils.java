/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.utility;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.data.Option;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class that includes a bunch of useful methods.
 */
@Kapi
public final class Utils {
    
    private static final Pattern HEX_COLOR_CODE_PATTERN = Pattern.compile("#[0-9A-Fa-f]{6}");
    
    private Utils() {
        throw new AssertionError("Utils should not be instantiated");
    }
    
    /**
     * Formats a string with support for Minecraft color codes.
     * Uses '&amp;' as the color code prefix.
     * <p>
     * For 24-bit color support, use the format
     * {@code &x&R&R&G&G&B&B}, where R, G, B are hex digits (0-9, a-f, A-F)
     * <p>
     * Tip: this <a href="https://www.birdflop.com/resources/rgb">website</a> can help you
     * write the color codes.
     * This website is not affiliated nor endorsed by Kapi, use it at your own risk.
     *
     * @param s the string to color
     * @return the colored string
     */
    @Kapi
    @Contract(pure = true)
    public static String col(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    
    /**
     * Converts a Color to a Minecraft-style color code string.
     * <p>
     * The format of the output string is {@code &x&R&R&G&G&B&B}, where R, G, B are hex digits (0-9, a-f, A-F).
     * Can be used in {@link #col(String)} to color a string.
     *
     * @param color the color to convert to a string
     * @return the color code string
     */
    @Kapi
    @Contract(pure = true)
    public static String colorToString(Color color) {
        int r1 = color.getRed() / 16;
        int r2 = color.getRed() % 16;
        int g1 = color.getGreen() / 16;
        int g2 = color.getGreen() % 16;
        int b1 = color.getBlue() / 16;
        int b2 = color.getBlue() % 16;
        return String.format("&x&%x%x%x%x%x%x", r1, r2, g1, g2, b1, b2);
    }
    
    /**
     * Formats a string with support for 24-bit color codes.
     * <p>
     * Replaces instances of '#DDDDDD' with the corresponding color code.
     * Where 'D' is a hex digit (0-9, a-f, A-F).
     *
     * @param s the string to color
     * @return the colored string
     * @deprecated use {@link #col(String)} instead, it supports 24-bit color codes
     */
    @Kapi
    @Contract(pure = true)
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "0.3.0")
    public static String col24(String s) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = HEX_COLOR_CODE_PATTERN.matcher(s);
        int last = 0;
        
        while (matcher.find()) {
            // Append the string before the color code
            sb.append(s, last, matcher.start());
            
            String hex = s.substring(matcher.start(), matcher.end());
            String color = String.valueOf(net.md_5.bungee.api.ChatColor.of(hex));
            sb.append(color);
            
            last = matcher.end();
        }
        
        // Append the remaining string
        sb.append(s, last, s.length());
        
        return sb.toString();
    }
    
    /**
     * Parses a string into an integer.
     *
     * @param s the string to parse
     * @return the parsed integer or None if the string is not a valid integer
     * @see Integer#parseInt(String)
     */
    @Kapi
    @Contract(pure = true)
    public static Option<Integer> parseInt(String s) {
        try {
            return Option.some(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return Option.none();
        }
    }
    
    /**
     * Parses a string into an integer.
     * <p>
     * The radix indicates the base of the number, for example,
     * 0-9 for base 10, 0/1 for base 2, 0-9/A-F for base 16, etc.
     *
     * @param s     the string to parse
     * @param radix the radix (base) to use while parsing
     * @return the parsed integer or None if the string is not a valid integer
     * @see Integer#parseInt(String, int)
     */
    @Kapi
    @Contract(pure = true)
    public static Option<Integer> parseInt(String s, int radix) {
        try {
            return Option.some(Integer.parseInt(s, radix));
        } catch (NumberFormatException e) {
            return Option.none();
        }
    }
    
    /**
     * Parses a string into a long.
     *
     * @param s the string to parse
     * @return the parsed long or None if the string is not a valid long
     */
    @Kapi
    public static Option<Long> parseLong(String s) {
        try {
            return Option.some(Long.parseLong(s));
        } catch (NumberFormatException e) {
            return Option.none();
        }
    }
    
    /**
     * Parses a string into a long.
     * <p>
     * The radix indicates the base of the number, for example,
     * 0-9 for base 10, 0/1 for base 2, 0-9/A-F for base 16, etc.
     *
     * @param s     the string to parse
     * @param radix the radix (base) to use while parsing
     * @return the parsed long or None if the string is not a valid long
     */
    @Kapi
    public static Option<Long> parseLong(String s, int radix) {
        try {
            return Option.some(Long.parseLong(s, radix));
        } catch (NumberFormatException e) {
            return Option.none();
        }
    }
    
    /**
     * Parses a string into a short.
     *
     * @param s the string to parse
     * @return the parsed short or None if the string is not a valid short
     */
    @Kapi
    public static Option<Short> parseShort(String s) {
        try {
            return Option.some(Short.parseShort(s));
        } catch (NumberFormatException e) {
            return Option.none();
        }
    }
    
    /**
     * Parses a string into a short.
     * <p>
     * The radix indicates the base of the number, for example,
     * 0-9 for base 10, 0/1 for base 2, 0-9/A-F for base 16, etc.
     *
     * @param s     the string to parse
     * @param radix the radix (base) to use while parsing
     * @return the parsed short or None if the string is not a valid short
     */
    @Kapi
    public static Option<Short> parseShort(String s, int radix) {
        try {
            return Option.some(Short.parseShort(s, radix));
        } catch (NumberFormatException e) {
            return Option.none();
        }
    }
    
    /**
     * Parses a string into a double.
     *
     * @param s the string to parse
     * @return the parsed double or None if the string is not a valid double
     */
    @Kapi
    public static Option<Double> parseDouble(String s) {
        try {
            return Option.some(Double.parseDouble(s));
        } catch (NumberFormatException e) {
            return Option.none();
        }
    }
    
    /**
     * Parses a string into a float.
     *
     * @param s the string to parse
     * @return the parsed float or None if the string is not a valid float
     */
    @Kapi
    public static Option<Float> parseFloat(String s) {
        try {
            return Option.some(Float.parseFloat(s));
        } catch (NumberFormatException e) {
            return Option.none();
        }
    }
    
}
