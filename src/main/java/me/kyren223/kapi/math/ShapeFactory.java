package me.kyren223.kapi.math;

import me.kyren223.kapi.annotations.Kapi;
import org.bukkit.util.Vector;

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
     * Creates a sphere with the given radius and amount of points.<br>
     * Uses the fibonacci lattice algorithm to ensure
     * an *almost* even distribution of points on the sphere.<br>
     * <br>
     * Assumes the sphere is centered at the origin (0, 0, 0).
     *
     * @param radius The radius of the sphere
     * @param pointsAmount The amount of points on the sphere
     * @return A list of Vector points on the sphere
     */
    @Kapi
    public static List<Vector> createHollowSphere(double radius, int pointsAmount) {
        List<Vector> points = new ArrayList<>();
        
        final double phi = Math.PI * (Math.sqrt(5) - 1); // golden angle in radians
        
        for (int i = 0; i < pointsAmount; i++) {
            double y = 1 - (i / (double) (pointsAmount - 1)) * 2; // y goes from 1 to -1
            double radiusAtY = Math.sqrt(1 - y * y); // radius at y
            double theta = phi * i; // golden angle increment
        
            double x = Math.cos(theta) * radiusAtY;
            double z = Math.sin(theta) * radiusAtY;
        
            points.add(new Vector(x, y, z).multiply(radius));
        }
        
        return points;
    }
    
    /**
     * Creates a circle with the given radius and amount of points.<br>
     * The circle is created on the xz-plane, with the y-coordinate set to 0.<br>
     * The points are evenly distributed on the circle.
     *
     * @param radius The radius of the circle
     * @param pointsAmount The amount of points on the circle
     * @return A list of Vector points on the circle
     */
    @Kapi
    public static List<Vector> createCircle(double radius, int pointsAmount) {
        List<Vector> points = new ArrayList<>();
        
        for (int i = 0; i < pointsAmount; i++) {
            double t = i / (double) (pointsAmount - 1);
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
     * @param radius  The radius of the circle
     * @param angleInDegrees The angle between each point in degrees,
     *                       can be considered as the "density" of the circle<br>
     *                       Calculates the amount of points by dividing 360 by the angle
     * @return A list of Vector points on the circle
     */
    @Kapi
    public static List<Vector> createCircle(double radius, double angleInDegrees) {
        return createCircle(radius, (int) (360 / angleInDegrees));
    }
    
    /**
     * Creates a line between two points with the given amount of points.<br>
     *
     * @param start The start point of the line
     * @param end The end point of the line
     * @param pointsAmount The amount of points on the line
     * @return A list of Vector points on the line
     */
    @Kapi
    public static List<Vector> createLine(Vector start, Vector end, int pointsAmount) {
        List<Vector> points = new ArrayList<>();
        
        for (int i = 0; i < pointsAmount; i++) {
            double t = i / (double) (pointsAmount - 1);
            points.add(Mathf.lerp(start, end, t));
        }
        
        return points;
    }
    
    /**
     * Creates a line between two points with the given distance between each point.<br>
     *
     * @param start The start point of the line
     * @param end The end point of the line
     * @param distanceBetweenPoints The distance between each point ("density" of the line)
     * @return A list of Vector points on the line
     */
    @Kapi
    public static List<Vector> createLine(Vector start, Vector end, double distanceBetweenPoints) {
        List<Vector> points = new ArrayList<>();
        
        Vector direction = end.clone().subtract(start).normalize();
        double distance = start.distance(end);
        
        for (double i = 0; i < distance; i += distanceBetweenPoints) {
            points.add(start.clone().add(direction.clone().multiply(i)));
        }
        
        return points;
    }
    
}
