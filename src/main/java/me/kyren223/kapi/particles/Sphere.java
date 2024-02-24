package me.kyren223.kapi.particles;

import org.bukkit.Location;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Sphere {
    
    private KParticle particle;
    private Location center;
    private double radius;
    private int points;
    private boolean filled;
    private final List<Location> cachedLocations;
    
    public Sphere(KParticle particle, Location location, double radius, int points, boolean filled) {
        this.particle = particle.clone();
        this.center = location.clone();
        this.radius = radius;
        this.points = points;
        this.filled = filled;
        this.cachedLocations = new ArrayList<>();
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be greater than 0");
        }
        if (points <= 0) {
            throw new IllegalArgumentException("Points must be greater than 0");
        }
        if (location.getWorld() == null) {
            throw new IllegalArgumentException("Location must have a world");
        }
    }
    
    public KParticle getParticle() {
        return particle;
    }
    
    public void setParticle(KParticle particle) {
        this.particle = particle;
    }
    
    public Location getCenter() {
        return center;
    }
    
    public void setCenter(Location center) {
        this.center = center;
        cachedLocations.clear();
    }
    
    public double getRadius() {
        return radius;
    }
    
    public void setRadius(double radius) {
        this.radius = radius;
        cachedLocations.clear();
    }
    
    public int getPoints() {
        return points;
    }
    
    public void setPoints(int points) {
        this.points = points;
        cachedLocations.clear();
    }
    
    public boolean isFilled() {
        return filled;
    }
    
    public void setFilled(boolean filled) {
        this.filled = filled;
        cachedLocations.clear();
    }
    
    private void generateLocations() {
        if (filled) {
            generateFilledSphere();
        } else {
            generateHollowSphere();
        }
    }
    
    private void generateFilledSphere() {
        final double phi = Math.PI * (Math.sqrt(5) - 1); // golden angle in radians
        
        for (int i = 0; i < points; i++) {
            double y = 1 - (i / (double) (points - 1)) * 2; // y goes from 1 to -1
            double radiusAtY = Math.sqrt(1 - y * y);
            double theta = phi * i; // golden angle increment
            
            double x = Math.cos(theta) * radiusAtY;
            double z = Math.sin(theta) * radiusAtY;
            
            Location location = center.clone().add(x * radius, y * radius, z * radius);
            cachedLocations.add(location);
        }
    }
    
    private void generateHollowSphere() {
        // TODO
        throw new NotImplementedException();
    }
    
    public List<Location> getShapePoints() {
        if (cachedLocations.isEmpty()) {
            generateLocations();
        }
        return cachedLocations;
    }
    
    public void draw() {
        if (cachedLocations.isEmpty()) {
            generateLocations();
        }
        
        for (Location location : cachedLocations) {
            particle.spawn(location);
        }
    }
}
