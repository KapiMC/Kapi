/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package me.kyren223.kapi.math;

import me.kyren223.kapi.annotations.Kapi;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * A factory for creating shapes.
 */
@Kapi
public class ShapeFactory {
    
    private ShapeFactory() {
        throw new AssertionError("ShapeFactory should not be instantiated");
    }
    
    /**
     * Creates a sphere with the given radius and number of points.<br>
     * Uses the fibonacci lattice algorithm to ensure
     * an *almost* even distribution of points on the sphere.<br>
     * <br>
     * Assumes the sphere is centered at the origin (0, 0, 0).
     *
     * @param radius         The radius of the sphere
     * @param numberOfPoints The number of points on the sphere
     * @return A list of Vector points on the sphere
     */
    @Kapi
    @Contract(pure = true)
    public static List<Vector> createHollowSphere(double radius, int numberOfPoints) {
        List<Vector> points = new ArrayList<>();
        
        for (int i = 0; i < numberOfPoints; i++) {
            // y goes from 1 to -1
            double y = 1 - (i / (double) (numberOfPoints - 1)) * 2;
            double radiusAtY = Math.sqrt(1 - y * y);
            // Golden angle increment
            double theta = Mathf.PHI_BIG * i;
            
            double x = Math.cos(theta) * radiusAtY;
            double z = Math.sin(theta) * radiusAtY;
            
            points.add(new Vector(x, y, z).multiply(radius));
        }
        
        return points;
    }
    
    /**
     * Creates a circle with the given radius and number of points.<br>
     * The circle is created on the xz-plane, with the y-coordinate set to 0.<br>
     * The points are evenly distributed on the circle.
     *
     * @param radius         The radius of the circle
     * @param numberOfPoints The number of points on the circle
     * @return A list of Vector points on the circle
     * @see #createCircle(double, double)
     * @see #createCircle(double, double, Vector)
     * @see #createCircle(double, int, Vector)
     */
    @Kapi
    @Contract(pure = true)
    public static List<Vector> createCircle(double radius, int numberOfPoints) {
        List<Vector> points = new ArrayList<>();
        
        for (int i = 0; i < numberOfPoints; i++) {
            double t = i / (double) (numberOfPoints - 1);
            double angleInRadians = Mathf.TAU * t;
            
            double x = Math.cos(angleInRadians) * radius;
            double z = Math.sin(angleInRadians) * radius;
            
            points.add(new Vector(x, 0, z));
        }
        
        return points;
    }
    
    /**
     * Creates a circle with the given radius and the angle between each point.<br>
     * The circle is created on the xz-plane, with the y-coordinate set to 0.<br>
     * The points are evenly distributed on the circle.
     *
     * @param radius         The radius of the circle
     * @param angleInDegrees The angle between each point in degrees
     * @return A list of Vector points on the circle
     * @see #createCircle(double, int)
     * @see #createCircle(double, double, Vector)
     * @see #createCircle(double, int, Vector)
     */
    @Kapi
    @Contract(pure = true)
    public static List<Vector> createCircle(double radius, double angleInDegrees) {
        return createCircle(radius, (int) (360 / angleInDegrees));
    }
    
    /**
     * Creates a circle with the given radius and the angle between each point.<br>
     * The circle is created on the 2D plane that is perpendicular to the given direction.<br>
     * The points are evenly distributed on the circle.
     *
     * @param radius         The radius of the circle
     * @param angleInDegrees The angle between each point in degrees
     * @param direction      The direction of the circle
     * @return A list of Vector points on the circle
     * @see #createCircle(double, int)
     * @see #createCircle(double, double)
     * @see #createCircle(double, int, Vector)
     */
    @Kapi
    @Contract(pure = true)
    public static List<Vector> createCircle(
            double radius, double angleInDegrees, Vector direction
    ) {
        List<Vector> circle = createCircle(radius, (int) (360 / angleInDegrees));
        circle.forEach(point -> {
            Matrix4f matrix = new Matrix4f();
            matrix.lookAlong(direction.toVector3f(), new Vector3f(0, 1, 0));
            Vector3f point3f = point.toVector3f();
            matrix.transformPosition(point3f);
            point.setX(point3f.x);
            point.setY(point3f.y);
            point.setZ(point3f.z);
        });
        return circle;
    }
    
    /**
     * Creates a circle with the given radius and number of points.<br>
     * The circle is created on the 2D plane that is perpendicular to the given direction.<br>
     * The points are evenly distributed on the circle.
     *
     * @param radius         The radius of the circle
     * @param numberOfPoints The number of points on the circle
     * @param direction      The direction of the circle
     * @return A list of Vector points on the circle
     * @see #createCircle(double, double)
     * @see #createCircle(double, int)
     * @see #createCircle(double, double, Vector)
     */
    @Kapi
    @Contract(pure = true)
    public static List<Vector> createCircle(
            double radius, int numberOfPoints, Vector direction
    ) {
        List<Vector> circle = createCircle(radius, numberOfPoints);
        circle.forEach(point -> {
            Matrix4f matrix = new Matrix4f();
            matrix.lookAlong(direction.toVector3f(), new Vector3f(0, 1, 0));
            Vector3f point3f = point.toVector3f();
            matrix.transformPosition(point3f);
            point.setX(point3f.x);
            point.setY(point3f.y);
            point.setZ(point3f.z);
        });
        return circle;
    }
    
    /**
     * Creates a line between two points with the given number of points.<br>
     *
     * @param start          The start point of the line
     * @param end            The end point of the line
     * @param numberOfPoints The number of points on the line
     * @return A list of Vector points on the line
     */
    @Kapi
    @Contract(pure = true)
    public static List<Vector> createLine(
            Vector start, Vector end, int numberOfPoints
    ) {
        List<Vector> points = new ArrayList<>();
        
        for (int i = 0; i < numberOfPoints; i++) {
            double t = i / (double) (numberOfPoints - 1);
            points.add(Mathf.lerp(start, end, t));
        }
        
        return points;
    }
    
    /**
     * Creates a line between two points with the given distance between each point.
     *
     * @param start                 The start point of the line
     * @param end                   The end point of the line
     * @param distanceBetweenPoints The distance between each point
     * @return A list of Vector points on the line
     */
    @Kapi
    @Contract(pure = true)
    public static List<Vector> createLine(
            Vector start, Vector end, double distanceBetweenPoints
    ) {
        List<Vector> points = new ArrayList<>();
        
        Vector direction = end.clone().subtract(start).normalize();
        double distance = start.distance(end);
        
        for (double i = 0; i < distance; i += distanceBetweenPoints) {
            points.add(start.clone().add(direction.clone().multiply(i)));
        }
        
        return points;
    }
    
    /**
     * Creates a line from a starting point in a direction
     * with the given length and distance between each point.
     *
     * @param start                 The start point of the line
     * @param direction             The direction of the line
     * @param length                The length of the line
     * @param distanceBetweenPoints The distance between each point on the line
     * @return A list of Vector points on the line
     */
    @Kapi
    @Contract(pure = true)
    public static List<Vector> createLineWithLength(
            Vector start, Vector direction, double length,
            double distanceBetweenPoints
    ) {
        return createLine(
                start, Mathf.pointFromDirection(start, direction, length), distanceBetweenPoints);
    }
    
    /**
     * Creates a line from a starting point in a direction
     * with the given length and number of points.
     *
     * @param start          The start point of the line
     * @param direction      The direction of the line
     * @param length         The length of the line
     * @param numberOfPoints The distance between each point on the line
     * @return A list of Vector points on the line
     */
    @Kapi
    @Contract(pure = true)
    public static List<Vector> createLineWithLength(
            Vector start, Vector direction, double length, int numberOfPoints
    ) {
        return createLine(
                start, Mathf.pointFromDirection(start, direction, length), numberOfPoints);
    }
}
