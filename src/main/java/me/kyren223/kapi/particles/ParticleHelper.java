package me.kyren223.kapi.particles;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ParticleHelper {
    
    public static KParticle createRedstone(@NotNull Color color, float size) {
        return new KParticle(org.bukkit.Particle.REDSTONE, 0, new Particle.DustOptions(
                color, size
        ));
    }
    
    public static List<Line> generateArrow(KParticle particle, Location start, double length, double width, int lengthPoints, int widthPoints, int angle, Vector direction, boolean hasBack) {
        double actualAngle = (angle + 180)  % 360;
        Location end = start.clone().add(direction.clone().normalize().multiply(length));
        Line line = new Line(particle, start, end, lengthPoints);
        
//        Vector left = direction.clone().normalize().rotateAroundY(direction, Math.toRadians(actualAngle));
//        Vector right = direction.clone().normalize().rotateAroundY(direction, Math.toRadians(-actualAngle));
        Vector axisOfRotation = direction.clone().crossProduct(new Vector(0, 1, 0)).normalize();
        Vector left = direction.clone().normalize().rotateAroundAxis(axisOfRotation, Math.toRadians(actualAngle));
        Vector right = direction.clone().normalize().rotateAroundAxis(axisOfRotation, Math.toRadians(-actualAngle));
        
        Line leftLine = new Line(particle, end, end.clone().add(left.clone().normalize().multiply(width)), widthPoints);
        Line rightLine = new Line(particle, end, end.clone().add(right.clone().normalize().multiply(width)), widthPoints);
        
        Line leftBack =  new Line(particle, start, start.clone().add(left.clone().normalize().multiply(width)), widthPoints);
        Line rightBack = new Line(particle, start, start.clone().add(right.clone().normalize().multiply(width)), widthPoints);
        List<Line> arrow = new ArrayList<>(List.of(line, leftLine, rightLine));
        if (hasBack) {
            arrow.addAll(List.of(leftBack, rightBack));
        }
        return arrow;
    }
}
