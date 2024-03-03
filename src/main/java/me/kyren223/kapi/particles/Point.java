package me.kyren223.kapi.particles;

import org.bukkit.util.Vector;

public class Point {
    private double x, y, z;
    private ParticleData particle;
    
    public Point(double x, double y, double z, ParticleData particle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.particle = particle;
    }
    
    public Point(Vector vector, ParticleData particle) {
        this(vector.getX(), vector.getY(), vector.getZ(), particle);
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




