package me.kyren223.kapi.math;

import org.bukkit.Color;
import org.bukkit.util.Vector;

public class MathUtils {
    
    public static final double TAU = Math.PI * 2;
    
    public static double lerp(double a, double b, double t) {
        return (1 - t) * a + b * t;
    }
    
    public static Vector lerp(Vector a, Vector b, double t) {
        return new Vector(
                lerp(a.getX(), b.getX(), t),
                lerp(a.getY(), b.getY(), t),
                lerp(a.getZ(), b.getZ(), t)
        );
    }
    
    public static Vector slerp(Vector a, Vector b, double t) {
        double dot = a.dot(b);
        dot = Math.max(-1, Math.min(1, dot));
        double theta = Math.acos(dot) * t;
        Vector relative = b.clone().subtract(a.clone().multiply(dot));
        relative.normalize();
        return a.clone().multiply(Math.cos(theta)).add(relative.multiply(Math.sin(theta)));
    }
    
    public static Color lerp(Color a, Color b, double t) {
        return Color.fromRGB((int) lerp(a.asRGB(), b.asRGB(), t));
    }
    
    public static double iLerp(double a, double b, double v) {
        if (a == b) throw new IllegalArgumentException("a and b cannot be the same, as it would result in division by zero");
        return (v - a) / (b - a);
    }
    
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
    
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
    
    public static double remap(double inputMin, double inputMax, double outputMin, double outputMax, double value) {
        return lerp(outputMin, outputMax, iLerp(inputMin, inputMax, value));
    }
    
}
