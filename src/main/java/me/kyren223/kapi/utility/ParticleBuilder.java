package me.kyren223.kapi.utility;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.data.ParticleData;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

/**
 * A builder for creating {@link ParticleData} objects.
 */
@Kapi
public class ParticleBuilder {
    private Particle particle;
    private int count;
    private double spreadX;
    private double spreadY;
    private double spreadZ;
    private double extra;
    private Object data;
    private boolean force;
    
    /**
     * Creates a new particle builder.<br>
     * For redstone particles, using {@link #createRedstone(Color, float)}
     * is recommended.
     *
     * @param particle The particle to create
     * @return A new particle builder
     */
    @Kapi
    public static ParticleBuilder create(Particle particle) {
        ParticleBuilder builder = new ParticleBuilder();
        builder.particle = particle;
        return builder;
    }
    
    /**
     * Creates a new particle builder with a redstone particle.
     *
     * @param color The color of the redstone particle
     * @param size The size of the redstone particle
     * @return A new particle builder
     */
    @Kapi
    public static ParticleBuilder createRedstone(Color color, float size) {
        return create(Particle.REDSTONE).data(new Particle.DustOptions(color, size));
    }
    
    /**
     * Sets the count of the particle.
     *
     * @param count The count
     * @return This builder for chaining
     */
    @Kapi
    public ParticleBuilder count(int count) {
        this.count = count;
        return this;
    }
    
    /**
     * Sets the spread of the particle.
     *
     * @param spreadX The spread on the x-axis
     * @param spreadY The spread on the y-axis
     * @param spreadZ The spread on the z-axis
     * @return This builder for chaining
     */
    @Kapi
    public ParticleBuilder spread(double spreadX, double spreadY, double spreadZ) {
        this.spreadX = spreadX;
        this.spreadY = spreadY;
        this.spreadZ = spreadZ;
        return this;
    }
    
    /**
     * Sets the spread of the particle.
     *
     * @param spread The spread on all axes
     * @return This builder for chaining
     */
    @Kapi
    public ParticleBuilder spread(Vector spread) {
        this.spreadX = spread.getX();
        this.spreadY = spread.getY();
        this.spreadZ = spread.getZ();
        return this;
    }
    
    /**
     * Sets the spread of the particle.
     *
     * @param spread The spread on all axes
     * @return This builder for chaining
     */
    @Kapi
    public ParticleBuilder spread(double spread) {
        this.spreadX = spread;
        this.spreadY = spread;
        this.spreadZ = spread;
        return this;
    }
    
    /**
     * Sets the extra data of the particle.
     *
     * @param extra The extra data
     * @return This builder for chaining
     */
    @Kapi
    public ParticleBuilder extra(double extra) {
        this.extra = extra;
        return this;
    }
    
    /**
     * Sets the data of the particle.
     *
     * @param data The data
     * @return This builder for chaining
     */
    @Kapi
    public ParticleBuilder data(Object data) {
        this.data = data;
        return this;
    }
    
    /**
     * Sets whether the particle should be forced to be displayed.
     *
     * @param force Whether to force the particle
     * @return This builder for chaining
     */
    @Kapi
    public ParticleBuilder force(boolean force) {
        this.force = force;
        return this;
    }
    
    /**
     * Builds the particle data.
     *
     * @return The particle data
     */
    @Kapi
    public ParticleData build() {
        return new ParticleData(particle, count, spreadX, spreadY, spreadZ, extra, data, force);
    }
    
}
