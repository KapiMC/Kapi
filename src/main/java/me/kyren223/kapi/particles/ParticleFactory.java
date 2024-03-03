package me.kyren223.kapi.particles;

import java.util.ArrayList;
import java.util.List;

public class ParticleFactory {
    
    public static ParticleTemplate createHollowSphere(ParticleData particle, double radius, int pointsAmount) {
        List<Point> points = new ArrayList<>();
        
        final double phi = Math.PI * (Math.sqrt(5) - 1); // golden angle in radians
        
        int rows = (int) Math.sqrt(pointsAmount);
        double dPhi = Math.PI / rows;
        //noinspection UnnecessaryLocalVariable - for clarity
        double dTheta = phi;
        
        for (int i = 0; i < rows; i++) {
            double y = Math.cos((i * dPhi) - (Math.PI / 2)); // y goes from 1 to -1
            double radiusAtY = Math.sin((i * dPhi) - (Math.PI / 2));
            
            int pointsInRow = (int) (2 * Math.PI * radiusAtY * pointsAmount / 2);
            double dThetaForRow = (2 * Math.PI) / pointsInRow;
            
            for (int j = 0; j < pointsInRow; j++) {
                double theta = j * dThetaForRow; // golden angle increment
                
                double x = Math.cos(theta) * radiusAtY;
                double z = Math.sin(theta) * radiusAtY;
                
                // Add point
                points.add(new Point(x * radius, y * radius, z * radius, particle));
            }
        }
        
        return new ParticleTemplate(points);
    }
}
