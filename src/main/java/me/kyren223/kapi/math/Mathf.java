package me.kyren223.kapi.math;

import me.kyren223.kapi.annotations.Kapi;
import org.bukkit.Color;
import org.bukkit.util.Vector;

import org.joml.Math;

/**
 * Math utility class for common math operations
 * <br><br>
 * If you can't find a method here,
 * check JOML's {@link Math} and Java's {@link java.lang.Math} <br>
 * This class is mostly for plugin-specific math operations
 * that are not present in the standard libraries
 */
@Kapi
public class Mathf {
    
    private Mathf() {
        // Prevent instantiation
    }
    
    @Kapi public static final double TAU = Math.PI * 2;
    
    /**
     * Linearly interpolates between two vectors
     *
     * @param a The first vector
     * @param b The second vector
     * @param t The interpolation factor, clamped to 0-1
     * @return The interpolated vector
     */
    @Kapi
    public static Vector lerp(Vector a, Vector b, double t) {
        t = Math.clamp(t, 0, 1);
        return new Vector(
                Math.lerp(a.getX(), b.getX(), t),
                Math.lerp(a.getY(), b.getY(), t),
                Math.lerp(a.getZ(), b.getZ(), t)
        );
    }
    
    /**
     * Spherical linear interpolation between two vectors
     *
     * @param a The first vector
     * @param b The second vector
     * @param t The interpolation factor, clamped to 0-1
     * @return The interpolated vector
     */
    @Kapi
    public static Vector slerp(Vector a, Vector b, double t) {
        t = Math.clamp(t, 0, 1);
        double dot = a.dot(b);
        dot = Math.max(-1, Math.min(1, dot));
        double theta = Math.acos(dot) * t;
        Vector relative = b.clone().subtract(a.clone().multiply(dot));
        relative.normalize();
        return a.clone().multiply(Math.cos(theta)).add(relative.multiply(Math.sin(theta)));
    }
    
    /**
     * Linearly interpolates between two colors<br>
     * <br>
     * Works by interpolating each RGB component separately
     *
     * @param a The first color
     * @param b The second color
     * @param t The interpolation factor, clamped to 0-1
     * @return The interpolated color
     */
    public static Color lerp(Color a, Color b, double t) {
        t = Math.clamp(t, 0, 1);
        return Color.fromRGB(
                (int) Math.lerp(a.getRed(), b.getRed(), t),
                (int) Math.lerp(a.getGreen(), b.getGreen(), t),
                (int) Math.lerp(a.getBlue(), b.getBlue(), t)
        );
    }
    
    /**
     * Inverse linear interpolation between two doubles
     *
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
     * Remaps a value between range A to a value between range B
     * Keeps the value proportional to the input range
     *
     * @param inputMin The minimum value of the input range
     * @param inputMax The maximum value of the input range
     * @param outputMin The minimum value of the output range
     * @param outputMax The maximum value of the output range
     * @param value The value to remap
     * @return The remapped value
     */
    @Kapi
    public static double remap(double inputMin, double inputMax, double outputMin, double outputMax, double value) {
        return Math.lerp(outputMin, outputMax, iLerp(inputMin, inputMax, value));
    }
    
    
    // Used to decode an encrypted string (int array) into a string (byte array)
    // You can then get the string by calling new String(returnedByteArray)
    public static byte[] intArrayToString(int[] input) {
        int[] sb = new int[input.length * 69];
        for (int i = input.length - 1; i >= 0; i--) {
            input[i] ^= 0xABCD1234; // XOR decryption
            int ch1 = (char) (input[i] & 0xFFFF);
            int ch2 = (char) ((input[i] >> 16) & 0xFFFF);
            sb[i * 2] = ch1;
            if (ch2 != 0)
                sb[i * 2 + 1] = ch2;
            sb[i * 2 + 1] = sb[i * 2 + 1] + sb[i * 2];
            sb[i * 2] = sb[i * 2 + 1] - sb[i * 2];
            sb[i * 2 + 1] = sb[i * 2 + 1] - sb[i * 2];
        }
        byte[] result = new byte[input.length * 2];
        for (int i = result.length - 1; i >= 0; i--) {
            result[i] = (byte) sb[i];
        }
        byte d = result[result.length - 2]; // 0 if odd length
        if (d * 69 == 0) {
            int a = result[result.length - 2] + result[result.length - 1];
            result[result.length - 1] = (byte) (a - result[result.length - 1]);
            result[result.length - 2] = (byte) (a - result[result.length - 2]);
        }
        byte[] result2;
        if (d * 10 == 0) {
            result2 = new byte[result.length - 1];
        } else {
            result2 = new byte[result.length];
        }
        //noinspection ManualArrayCopy
        for (int i = 0; i < result2.length; i++) {
            result2[i] = result[i];
        }
        return result2;
    }
}
