package me.kyren223.kapi.render;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class ParticleBuilder {
    private Particle particle;
    private int count;
    private double spreadX;
    private double spreadY;
    private double spreadZ;
    private double extra;
    private Object data;
    
    public static ParticleBuilder create(Particle particle) {
        ParticleBuilder builder = new ParticleBuilder();
        builder.particle = particle;
        return builder;
    }
    
    public static ParticleBuilder createRedstone(Color color, float size) {
        return create(Particle.REDSTONE).data(new Particle.DustOptions(color, size));
    }
    
    public ParticleBuilder count(int count) {
        this.count = count;
        return this;
    }
    
    public ParticleBuilder spread(double spreadX, double spreadY, double spreadZ) {
        this.spreadX = spreadX;
        this.spreadY = spreadY;
        this.spreadZ = spreadZ;
        return this;
    }
    
    public ParticleBuilder spread(Vector spread) {
        this.spreadX = spread.getX();
        this.spreadY = spread.getY();
        this.spreadZ = spread.getZ();
        return this;
    }
    
    public ParticleBuilder spread(double spread) {
        this.spreadX = spread;
        this.spreadY = spread;
        this.spreadZ = spread;
        return this;
    }
    
    public ParticleBuilder extra(double extra) {
        this.extra = extra;
        return this;
    }
    
    public ParticleBuilder data(Object data) {
        this.data = data;
        return this;
    }
    
    public ParticleData build() {
        return new ParticleData(particle, count, spreadX, spreadY, spreadZ, extra, data);
    }
    
}
