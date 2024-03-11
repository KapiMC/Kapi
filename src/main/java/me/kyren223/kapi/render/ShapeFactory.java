package me.kyren223.kapi.render;

import me.kyren223.kapi.math.Mathf;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ShapeFactory {
    
    private ShapeFactory() {
        // Prevent instantiation
    }
    
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
    
    public static List<Vector> createCircle(double radius, double angleInDegrees) {
        return createCircle(radius, (int) (360 / angleInDegrees));
    }
    
    public static List<Vector> createLine(Vector start, Vector end, int pointsAmount) {
        List<Vector> points = new ArrayList<>();
        
        for (int i = 0; i < pointsAmount; i++) {
            double t = i / (double) (pointsAmount - 1);
            points.add(Mathf.lerp(start, end, t));
        }
        
        return points;
    }
    
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
