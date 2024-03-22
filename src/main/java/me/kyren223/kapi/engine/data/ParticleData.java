package me.kyren223.kapi.engine.data;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.engine.renderable.ParticleRender;
import me.kyren223.kapi.engine.utility.ParticleBuilder;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

/**
 * Holds the data for a particle.<br>
 * Use {@link ParticleBuilder} for easier particle creation.
 * See {@link ParticleRender} for rendering particles.
 */
@Kapi
public class ParticleData {
    private static final String PARTICLE_IS_NOT_REDSTONE = "Particle is not REDSTONE";
    private Particle particle;
    private int count;
    private double spreadX;
    private double spreadY;
    private double spreadZ;
    private double extra;
    private Object data;
    private boolean force;
    
    @Kapi
    public ParticleData(Particle particle, int count, double spreadX, double spreadY, double spreadZ, double extra, Object data, boolean force) {
        this.particle = particle;
        this.count = count;
        this.spreadX = spreadX;
        this.spreadY = spreadY;
        this.spreadZ = spreadZ;
        this.extra = extra;
        this.data = data;
        this.force = force;
    }
    
    /**
     * Copy constructor<br>
     * <br>
     * Note: This does not make a deep copy of the data object
     *
     * @param data The data to copy
     */
    @Kapi
    public ParticleData(ParticleData data) {
        this.particle = data.getParticle();
        this.count = data.getCount();
        this.spreadX = data.getSpreadX();
        this.spreadY = data.getSpreadY();
        this.spreadZ = data.getSpreadZ();
        this.extra = data.getExtra();
        this.data = data.getData();
        this.force = data.isForce();
    }
    
    @Kapi
    public Particle getParticle() {
        return particle;
    }
    
    @Kapi
    public void setParticle(Particle particle) {
        this.particle = particle;
    }
    
    @Kapi
    public int getCount() {
        return count;
    }
    
    @Kapi
    public void setCount(int count) {
        this.count = count;
    }
    
    @Kapi
    public double getSpreadX() {
        return spreadX;
    }
    
    @Kapi
    public void setSpreadX(double spreadX) {
        this.spreadX = spreadX;
    }
    
    @Kapi
    public double getSpreadY() {
        return spreadY;
    }
    
    @Kapi
    public void setSpreadY(double spreadY) {
        this.spreadY = spreadY;
    }
    
    @Kapi
    public double getSpreadZ() {
        return spreadZ;
    }
    
    @Kapi
    public void setSpreadZ(double spreadZ) {
        this.spreadZ = spreadZ;
    }
    
    /**
     * Gets the spread of the particle.
     *
     * @return A new vector with the spread
     */
    @Kapi
    public Vector getSpread() {
        return new Vector(spreadX, spreadY, spreadZ);
    }
    
    /**
     * Sets the spread of the particle.<br>
     * Note: modifying the given vector after setting it will not affect the spread
     *
     * @param vector The new spread
     */
    @Kapi
    public void setSpread(Vector vector) {
        this.spreadX = vector.getX();
        this.spreadY = vector.getY();
        this.spreadZ = vector.getZ();
    }
    
    /**
     * Sets the X, Y, and Z spread of the particle to the same value.
     *
     * @param spread The new spread
     */
    @Kapi
    public void setSpread(double spread) {
        this.spreadX = spread;
        this.spreadY = spread;
        this.spreadZ = spread;
    }
    
    @Kapi
    public double getExtra() {
        return extra;
    }
    
    @Kapi
    public void setExtra(double extra) {
        this.extra = extra;
    }
    
    @Kapi
    public Object getData() {
        return data;
    }
    
    @Kapi
    public void setData(Object data) {
        this.data = data;
    }
    
    /**
     * Sets the data of the particle to a dust color and size.
     *
     * @param color The color of the particle
     * @param size  The size of the particle
     * @throws IllegalStateException If the particle is not REDSTONE
     */
    @Kapi
    public void setRedstoneData(Color color, float size) {
        if (particle != Particle.REDSTONE) {
            throw new IllegalStateException(PARTICLE_IS_NOT_REDSTONE);
        }
        this.data = new Particle.DustOptions(color, size);
    }
    
    /**
     * Gets the data of the particle as a DustOptions object.
     *
     * @return The DustOptions object of this particle
     * @throws IllegalStateException If the particle is not REDSTONE
     */
    @Kapi
    public Particle.DustOptions getRedstoneData() {
        if (particle != Particle.REDSTONE) {
            throw new IllegalStateException(PARTICLE_IS_NOT_REDSTONE);
        }
        return (Particle.DustOptions) data;
    }
    
    /**
     * Sets the color of the redstone particle.
     *
     * @param color The color of the particle
     * @throws IllegalStateException If the particle is not REDSTONE
     */
    @Kapi
    public void setRedstoneColor(Color color) {
        if (particle != Particle.REDSTONE) {
            throw new IllegalStateException(PARTICLE_IS_NOT_REDSTONE);
        }
        this.data = new Particle.DustOptions(color, getRedstoneData().getSize());
    }
    
    /**
     * Sets the size of the redstone particle.
     *
     * @param size The size of the particle
     * @throws IllegalStateException If the particle is not REDSTONE=
     */
    @Kapi
    public void setRedstoneSize(float size) {
        if (particle != Particle.REDSTONE) {
            throw new IllegalStateException(PARTICLE_IS_NOT_REDSTONE);
        }
        this.data = new Particle.DustOptions(getRedstoneData().getColor(), size);
    }
    
    /**
     * @return Whether the particle should be forced to be displayed
     */
    @Kapi
    public boolean isForce() {
        return force;
    }
    
    /**
     * Sets whether the particle should be forced to be displayed<br>
     * When false, the particle(s) will be sent to players within 32.0 blocks (exclusive).
     *
     * @param force Whether the particle should be forced to be displayed
     */
    @Kapi
    public void setForce(boolean force) {
        this.force = force;
    }
}
