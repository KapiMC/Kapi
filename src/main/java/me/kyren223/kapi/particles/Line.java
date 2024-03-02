package me.kyren223.kapi.particles;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Line {
    
    private KParticle particle;
    private Location start, end;
    private int points;
    private final List<Location> cachedLocations;
    
    public Line(KParticle particle, Location start, Location end, int points) {
        this.particle = particle;
        this.start = start;
        this.end = end;
        this.points = points;
        this.cachedLocations = new ArrayList<>();
    }
    
    public KParticle getParticle() {
        return particle;
    }
    
    public void setParticle(KParticle particle) {
        this.particle = particle;
        this.cachedLocations.clear();
    }
    
    public Location getStart() {
        return start;
    }
    
    public void setStart(Location start) {
        this.start = start;
        this.cachedLocations.clear();
    }
    
    public Location getEnd() {
        return end;
    }
    
    public void setEnd(Location end) {
        this.end = end;
        this.cachedLocations.clear();
    }
    
    public int getPoints() {
        return points;
    }
    
    public void setPoints(int points) {
        this.points = points;
        this.cachedLocations.clear();
    }
    
    public Vector getDirection() {
        return end.toVector().subtract(start.toVector()).normalize();
    }
    
    public List<Location> getShapePoints() {
        if (cachedLocations.isEmpty()) {
            generateLocations();
        }
        return cachedLocations;
    }
    
    private void generateLocations() {
        cachedLocations.clear();
        
        double dx = (end.getX() - start.getX()) / points;
        double dy = (end.getY() - start.getY()) / points;
        double dz = (end.getZ() - start.getZ()) / points;
        
        for (int i = 0; i < points; i++) {
            double x = start.getX() + dx * i;
            double y = start.getY() + dy * i;
            double z = start.getZ() + dz * i;
            cachedLocations.add(new Location(start.getWorld(), x, y, z));
        }
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
