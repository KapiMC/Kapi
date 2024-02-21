package me.kyren223.kapi.particles;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class KParticle implements Cloneable {
    
    private Particle particle;
    private int count;
    private double offsetX, offsetY, offsetZ;
    private double extra;
    private Object data;
    
    public KParticle(Particle particle) {
        this.particle = particle;
        this.count = 1;
        this.offsetX = 0;
        this.offsetY = 0;
        this.offsetZ = 0;
        this.extra = 0;
        this.data = null;
    }
    
    public KParticle(Particle particle, double offsetX, double offsetY, double offsetZ) {
        this.particle = particle;
        this.count = 1;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.extra = 0;
        this.data = null;
    }
    
    public KParticle(Particle particle, int count, double extra, Object data) {
        this.particle = particle;
        this.count = count;
        this.offsetX = 0;
        this.offsetY = 0;
        this.offsetZ = 0;
        this.extra = extra;
        this.data = data;
    }
    
    public KParticle(Particle particle, double offsetX, double offsetY, double offsetZ, double extra, Object data) {
        this.particle = particle;
        this.count = 1;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.extra = extra;
        this.data = data;
    }
    
    public KParticle(Particle particle, double extra, Object data) {
        this.particle = particle;
        this.count = 1;
        this.offsetX = 0;
        this.offsetY = 0;
        this.offsetZ = 0;
        this.extra = extra;
        this.data = data;
    }
    
    public KParticle(Particle particle, int count) {
        this.particle = particle;
        this.count = count;
        this.offsetX = 0;
        this.offsetY = 0;
        this.offsetZ = 0;
        this.extra = 0;
        this.data = null;
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
    
    public double getOffsetX() {
        return offsetX;
    }
    
    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }
    
    public double getOffsetY() {
        return offsetY;
    }
    
    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }
    
    public double getOffsetZ() {
        return offsetZ;
    }
    
    public void setOffsetZ(double offsetZ) {
        this.offsetZ = offsetZ;
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
    
    public KParticle(Particle particle, int count, double offsetX, double offsetY, double offsetZ, double extra, Object data) {
        this.particle = particle;
        this.count = count;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.extra = extra;
        this.data = data;
    }
    
    public void spawn(Location location) {
        if (location.getWorld() == null) throw new IllegalArgumentException("Location world is null!");
        spawn(location.getWorld(), location.getX(), location.getY(), location.getZ(), false);
    }
    
    public void spawn(Location location, boolean force) {
        if (location.getWorld() == null) throw new IllegalArgumentException("Location world is null!");
        spawn(location.getWorld(), location.getX(), location.getY(), location.getZ(), force);
    }
    
    public void spawn(World world, double x, double y, double z) {
        spawn(world, x, y, z, false);
    }
    
    public void spawn(World world, double x, double y, double z, boolean force) {
        world.spawnParticle(
                particle,
                x, y, z,
                count,
                offsetX, offsetY, offsetZ,
                extra,
                data,
                force
        );
    }
    
    @Override
    public KParticle clone() {
        try {
            return (KParticle) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}
