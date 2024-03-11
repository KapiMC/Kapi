package me.kyren223.kapi.math;

import org.bukkit.Color;
import org.bukkit.util.Vector;

import org.joml.Math;

public class Mathf {
    
    private Mathf() {
        // Prevent instantiation
    }
    
    public static final double TAU = Math.PI * 2;
    
    /**
     * Linearly interpolates between two doubles
     * @param a The first double
     * @param b The second double
     * @param t The interpolation factor, clamped to 0-1
     * @deprecated Use {@link org.joml.Math#lerp(double, double, double)} instead
     * @return The interpolated double
     */
    @Deprecated
    public static double lerp(double a, double b, double t) {
        return (1 - t) * a + b * t;
    }
    
    /**
     * Linearly interpolates between two vectors
     * @param a The first vector
     * @param b The second vector
     * @param t The interpolation factor, clamped to 0-1
     * @return The interpolated vector
     */
    public static Vector lerp(Vector a, Vector b, double t) {
        t = clamp(t, 0, 1);
        return new Vector(
                Math.lerp(a.getX(), b.getX(), t),
                Math.lerp(a.getY(), b.getY(), t),
                Math.lerp(a.getZ(), b.getZ(), t)
        );
    }
    
    /**
     * Spherical linear interpolation between two vectors
     * @param a The first vector
     * @param b The second vector
     * @param t The interpolation factor, clamped to 0-1
     * @return The interpolated vector
     */
    public static Vector slerp(Vector a, Vector b, double t) {
        t = clamp(t, 0, 1);
        double dot = a.dot(b);
        dot = Math.max(-1, Math.min(1, dot));
        double theta = Math.acos(dot) * t;
        Vector relative = b.clone().subtract(a.clone().multiply(dot));
        relative.normalize();
        return a.clone().multiply(Math.cos(theta)).add(relative.multiply(Math.sin(theta)));
    }
    
    /**
     * Linearly interpolates between two colors
     * <p></p>
     * Works by interpolating each RGB component separately
     * @param a The first color
     * @param b The second color
     * @param t The interpolation factor, clamped to 0-1
     * @return The interpolated color
     */
    public static Color lerp(Color a, Color b, double t) {
        t = clamp(t, 0, 1);
        return Color.fromRGB(
                (int) Math.lerp(a.getRed(), b.getRed(), t),
                (int) Math.lerp(a.getGreen(), b.getGreen(), t),
                (int) Math.lerp(a.getBlue(), b.getBlue(), t)
        );
    }
    
    /**
     * Inverse linear interpolation between two doubles
     * <p></p>
     * @param a The first double
     * @param b The second double
     * @param v The value to find the interpolation factor for
     * @return The interpolation factor between 0-1
     * @throws IllegalArgumentException if a and b are the same, as it would result in division by zero
     */
    public static double iLerp(double a, double b, double v) {
        if (a == b) throw new IllegalArgumentException("a and b cannot be the same, as it would result in division by zero");
        return (v - a) / (b - a);
    }
    
    /**
     * Clamps a double between a minimum and maximum
     * <p></p>
     * Equivalent to Math.max(min, Math.min(max, value))
     * @param value The value to clamp
     * @param min The minimum value
     * @param max The maximum value
     * @deprecated Use {@link org.joml.Math#clamp(double, double, double)} instead
     * @return The clamped value, between min and max
     */
    @Deprecated
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Clamps an integer between a minimum and maximum
     * <p></p>
     * Equivalent to Math.max(min, Math.min(max, value))
     * @param value The value to clamp
     * @param min The minimum value
     * @param max The maximum value
     * @return The clamped value, between min and max
     * @deprecated Use {@link org.joml.Math#clamp(int, int, int)} instead
     */
    @Deprecated
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Remaps a value between range A to a value between range B
     * Keeps the value proportional to the input range
     * @param inputMin The minimum value of the input range
     * @param inputMax The maximum value of the input range
     * @param outputMin The minimum value of the output range
     * @param outputMax The maximum value of the output range
     * @param value The value to remap
     * @return The remapped value
     */
    public static double remap(double inputMin, double inputMax, double outputMin, double outputMax, double value) {
        return lerp(outputMin, outputMax, iLerp(inputMin, inputMax, value));
    }
    
}
