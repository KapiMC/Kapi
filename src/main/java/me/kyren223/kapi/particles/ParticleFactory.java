package me.kyren223.kapi.particles;

import me.kyren223.kapi.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class ParticleFactory {
    
    public static ParticleTemplate createHollowSphere(ParticleData particle, double radius, int pointsAmount) {
        List<Point> points = new ArrayList<>();
        
        final double phi = Math.PI * (Math.sqrt(5) - 1); // golden angle in radians
        
        for (int i = 0; i < pointsAmount; i++) {
            double y = 1 - (i / (double) (pointsAmount - 1)) * 2; // y goes from 1 to -1
            double radiusAtY = Math.sqrt(1 - y * y); // radius at y
            double theta = phi * i; // golden angle increment
        
            double x = Math.cos(theta) * radiusAtY;
            double z = Math.sin(theta) * radiusAtY;
        
            points.add(new Point(x * radius, y * radius, z * radius, particle));
        }
        
        return new ParticleTemplate(points);
    }
    
    public static ParticleTemplate createCircle(ParticleData particle, double radius, int pointsAmount) {
        List<Point> points = new ArrayList<>();
        
        for (int i = 0; i < pointsAmount; i++) {
            double t = i / (double) (pointsAmount - 1);
            double angleInRadians = MathUtils.TAU * t;
            
            double x = Math.cos(angleInRadians) * radius;
            double z = Math.sin(angleInRadians) * radius;
            
            points.add(new Point(x, 0, z, particle));
        }
        
        return new ParticleTemplate(points);
    }
    
}
