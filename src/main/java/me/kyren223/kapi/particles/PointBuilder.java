package me.kyren223.kapi.particles;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;


public class PointBuilder {
    private World world;
    private double x, y, z;
    private Particle particle;
    private int count;
    private double spreadX, spreadY, spreadZ;
    private double extra;
    private Object data;
    
    public static PointBuilder create(Particle particle) {
        PointBuilder builder = new PointBuilder();
        builder.particle = particle;
        return builder;
    }
    
    public static PointBuilder createRedstone(Color color, float size) {
        return create(Particle.REDSTONE).data(new Particle.DustOptions(color, size));
    }
    
    public PointBuilder world(World world) {
        this.world = world;
        return this;
    }
    
    public PointBuilder count(int count) {
        this.count = count;
        return this;
    }
    
    public PointBuilder position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    
    public PointBuilder position(Vector vector) {
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
        return this;
    }
    
    public PointBuilder position(Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        return this;
    }
    
    public PointBuilder location(World world, double x, double y, double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    
    public PointBuilder location(Location location) {
        this.world = location.getWorld();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        return this;
    }
    
    public PointBuilder location(World world, Vector vector) {
        this.world = world;
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
        return this;
    }
    
    public PointBuilder spread(double spreadX, double spreadY, double spreadZ) {
        this.spreadX = spreadX;
        this.spreadY = spreadY;
        this.spreadZ = spreadZ;
        return this;
    }
    
    public PointBuilder spread(Vector spread) {
        this.spreadX = spread.getX();
        this.spreadY = spread.getY();
        this.spreadZ = spread.getZ();
        return this;
    }
    
    public PointBuilder spread(double spread) {
        this.spreadX = spread;
        this.spreadY = spread;
        this.spreadZ = spread;
        return this;
    }
    
    public PointBuilder extra(double extra) {
        this.extra = extra;
        return this;
    }
    
    public PointBuilder data(Object data) {
        this.data = data;
        return this;
    }
    
    public Point build() {
        return new Point(world, x, y, z, particle, count, spreadX, spreadY, spreadZ, extra, data);
    }
    
}
