package me.kyren223.kapi.particles;

import java.util.ArrayList;
import java.util.List;

public class ParticleFactory {
    
    public static ParticleTemplate createSphere(ParticleData particle, double radius, int pointsAmount) {
        List<Point> points = new ArrayList<>();
        return new ParticleTemplate(points);
    }
}
