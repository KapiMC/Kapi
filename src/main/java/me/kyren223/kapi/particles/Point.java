package me.kyren223.kapi.particles;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class Point {
    private World world;
    private double x, y, z;
    private ParticleData particle;
    
    public Point(World world, double x, double y, double z, ParticleData particle) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.particle = particle;
    }
    
    public Point(Location location, ParticleData particle) {
        this(location.getWorld(), location.getX(), location.getY(), location.getZ(), particle);
    }
    
    public Point(World world, Vector vector, ParticleData particle) {
        this(world, vector.getX(), vector.getY(), vector.getZ(), particle);
    }
    
    public World getWorld() {
        return world;
    }
    
    public void setWorld(World world) {
        this.world = world;
    }
    
    public double getX() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public double getZ() {
        return z;
    }
    
    public void setZ(double z) {
        this.z = z;
    }
    
    public void setLocation(Location location) {
        this.world = location.getWorld();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }
    
    public Location getLocation() {
        return new Location(world, x, y, z);
    }
    
    public Vector getVector() {
        return new Vector(x, y, z);
    }
    
    public void setVector(Vector vector) {
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
    }
    
    public ParticleData getParticle() {
        return particle;
    }
    
    public void setParticle(ParticleData particle) {
        this.particle = particle;
    }
}




