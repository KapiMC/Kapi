package me.kyren223.kapi.particles;

import org.bukkit.Particle;
import org.bukkit.World;

public class Point {
    private World world;
    private double x, y, z;
    private Particle particle;
    private int count;
    private double spreadX, spreadY, spreadZ;
    private double extra;
    private Object data;
    
    public Point(World world, double x, double y, double z, Particle particle, int count, double spreadX, double spreadY, double spreadZ, double extra, Object data) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.particle = particle;
        this.count = count;
        this.spreadX = spreadX;
        this.spreadY = spreadY;
        this.spreadZ = spreadZ;
        this.extra = extra;
        this.data = data;
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
    
    public Particle getParticle() {
        return particle;
    }
    
    public void setParticle(Particle particle) {
        this.particle = particle;
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
    public double getSpreadX() {
        return spreadX;
    }
    
    public void setSpreadX(double spreadX) {
        this.spreadX = spreadX;
    }
    
    public double getSpreadY() {
        return spreadY;
    }
    
    public void setSpreadY(double spreadY) {
        this.spreadY = spreadY;
    }
    
    public double getSpreadZ() {
        return spreadZ;
    }
    
    public void setSpreadZ(double spreadZ) {
        this.spreadZ = spreadZ;
    }
    
    public double getExtra() {
        return extra;
    }
    
    public void setExtra(double extra) {
        this.extra = extra;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
    
    public void setXYZ(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void setSpreadXYZ(double spreadX, double spreadY, double spreadZ) {
        this.spreadX = spreadX;
        this.spreadY = spreadY;
        this.spreadZ = spreadZ;
    }
}




