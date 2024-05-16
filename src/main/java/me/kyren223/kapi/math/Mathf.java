/*
 * Copyright (c) 2024 Kapi Contributors. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted if the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions, the following disclaimer and the list of contributors.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation and/or
 *    other materials provided with the distribution.
 *
 * 3. The buyer of the "Kapi" API is granted the right to use this software
 *    as a dependency in their own software projects. However, the buyer
 *    may not resell or distribute the "Kapi" API, in whole or in part, to other parties.
 *
 * 4. The buyer may include the "Kapi" API in a "fat jar" along with their own code.
 *    The license for the "fat jar" is at the buyer's discretion and may allow
 *    redistribution of the "fat jar", but the "Kapi" API code inside the "fat jar"
 *    must not be modified.
 *
 * 5. Neither the name "Kapi" nor the names of its contributors may be used to endorse
 *    or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY "Kapi" API, AND ITS CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL "Kapi" API, AND CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Kapi Contributors:
 * - Kyren223
 */

package me.kyren223.kapi.math;

import me.kyren223.kapi.annotations.Kapi;
import org.bukkit.Color;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.joml.Math;
import org.jspecify.annotations.NullMarked;

/**
 * Math utility class for common math operations.<br>
 * <br>
 * If you can't find a method here,
 * check JOML's {@link Math} and Java's {@link java.lang.Math}.<br>
 * This class is mostly for plugin-specific math operations
 * that are not present in the standard libraries.
 */
@Kapi
@NullMarked
public class Mathf {
    
    private Mathf() {
        throw new AssertionError("Mathf should not be instantiated");
    }
    
    /**
     * The mathematical constant tau, equal to 2 * {@link Math#PI}
     */
    @Kapi
    public static final double TAU = Math.PI * 2;
    
    /**
     * The golden angle in radians.<br>
     * This is the smaller of the two golden angles,
     * usually considered the "golden angle".
     *
     * @see #PHI_BIG
     */
    @Kapi
    public static final double PHI_SMALL = Math.PI * Math.sqrt(5) - 1;
    
    /**
     * The golden angle in radians.<br>
     * This is the larger of the two golden angles.
     *
     * @see #PHI_SMALL
     */
    @Kapi
    public static final double PHI_BIG = Math.PI * 2 - PHI_SMALL;
    
    /**
     * Linearly interpolates between two vectors.
     *
     * @param a The first vector
     * @param b The second vector
     * @param t The interpolation factor, clamped to 0-1
     * @return The interpolated vector
     */
    @Kapi
    @Contract(pure = true)
    public static Vector lerp(final Vector a, final Vector b, double t) {
        t = Math.clamp(t, 0, 1);
        return new Vector(
                Math.lerp(a.getX(), b.getX(), t),
                Math.lerp(a.getY(), b.getY(), t),
                Math.lerp(a.getZ(), b.getZ(), t)
        );
    }
    
    /**
     * Spherical linear interpolation between two vectors.
     *
     * @param a The first vector
     * @param b The second vector
     * @param t The interpolation factor, clamped to 0-1
     * @return The interpolated vector
     */
    @Kapi
    @Contract(pure = true)
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
     * Linearly interpolates between two colors.<br>
     * Works by interpolating each RGB component separately.
     *
     * @param a The first color
     * @param b The second color
     * @param t The interpolation factor, clamped to 0-1
     * @return The interpolated color
     */
    @Kapi
    @Contract(pure = true)
    public static Color lerp(Color a, Color b, double t) {
        t = Math.clamp(t, 0, 1);
        return Color.fromRGB(
                (int) Math.lerp(a.getRed(), b.getRed(), t),
                (int) Math.lerp(a.getGreen(), b.getGreen(), t),
                (int) Math.lerp(a.getBlue(), b.getBlue(), t)
        );
    }
    
    /**
     * Inverse linear interpolation between two doubles.
     *
     * @param a The first double
     * @param b The second double
     * @param v The value to find the interpolation factor for
     * @return The interpolation factor between 0-1
     * @throws IllegalArgumentException if a and b are the same, as it would result in division by
     *                                  zero
     */
    @Kapi
    @Contract(pure = true)
    public static double iLerp(double a, double b, double v) {
        if (a == b) {
            throw new IllegalArgumentException(
                    "a is the same as b which is not allowed due to division by zero");
        }
        return (v - a) / (b - a);
    }
    
    /**
     * Remaps a value between a given range to another range.<br>
     * Keeps the value proportional to the input range.
     *
     * @param inputMin  The minimum value of the input range
     * @param inputMax  The maximum value of the input range
     * @param outputMin The minimum value of the output range
     * @param outputMax The maximum value of the output range
     * @param value     The value to remap
     * @return The remapped value
     */
    @Kapi
    @Contract(pure = true)
    public static double remap(
            double inputMin, double inputMax, double outputMin, double outputMax, double value
    ) {
        return Math.lerp(outputMin, outputMax, iLerp(inputMin, inputMax, value));
    }
    
    /**
     * Calculates the end point of a line segment starting at a given point
     * and going in a given direction for a given length.
     *
     * @param start     The starting point
     * @param direction The direction vector
     * @param length    The length of the line segment
     * @return The end point of the line segment
     */
    @Kapi
    @Contract(pure = true)
    public static Vector pointFromDirection(Vector start, Vector direction, double length) {
        return start.clone().add(direction.clone().normalize().multiply(length));
    }
}
