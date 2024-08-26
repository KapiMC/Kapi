/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package me.kyren223.kapi.utility;

import me.kyren223.kapi.annotations.Kapi;
import org.bukkit.Color;
import org.jspecify.annotations.NullMarked;

/**
 * A utility class that includes a bunch of predefined colors.<br>
 * For the built-in bukkit colors, see {@link org.bukkit.Color}.<br>
 */
@Kapi
@NullMarked
public class Colors {
    
    private Colors() {
        // Prevent instantiation
    }
    
    private static Color bukkit(java.awt.Color color) {
        return Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
    }
    
    @Kapi
    public static final Color DARK_GREEN = bukkit(new java.awt.Color(16, 107, 16));
    @Kapi
    public static final Color DARK_RED = bukkit(new java.awt.Color(120, 1, 1));
    @Kapi
    public static final Color DARK_BLUE = bukkit(new java.awt.Color(1, 1, 120));
    @Kapi
    public static final Color DARK_PURPLE = bukkit(new java.awt.Color(91, 1, 91));
    @Kapi
    public static final Color DARK_AQUA = bukkit(new java.awt.Color(1, 120, 120));
    @Kapi
    public static final Color KYRENS_COLOR = bukkit(new java.awt.Color(0, 241, 255));
    
}
