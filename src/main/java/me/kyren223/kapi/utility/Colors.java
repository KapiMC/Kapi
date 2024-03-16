package me.kyren223.kapi.utility;

import org.bukkit.Color;

public class Colors {
    
    private Colors() {
        // Prevent instantiation
    }
    
    public static Color bukkit(java.awt.Color color) {
        return Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
    }
    
    public static final Color TEST = bukkit(new java.awt.Color(255, 0, 0));
    public static final Color DARK_GREEN = bukkit(new java.awt.Color(16, 107, 16));
    public static final Color DARK_RED = bukkit(new java.awt.Color(120, 1, 1));
    public static final Color DARK_BLUE = bukkit(new java.awt.Color(1, 1, 120));
    public static final Color DARK_PURPLE = bukkit(new java.awt.Color(91, 1, 91));
    public static final Color DARK_AQUA = bukkit(new java.awt.Color(1, 120, 120));
    public static final Color KYRENS_COLOR = bukkit(new java.awt.Color(0, 241, 255));
    
}
