/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package me.kyren223.kapi.data;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.engine.renderable.ParticleRender;
import me.kyren223.kapi.utility.ParticleBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Holds the data for a particle.<br>
 * Use {@link ParticleBuilder} for easier particle creation.<br>
 * See {@link ParticleRender} for rendering particles.
 */
@Kapi
@NullMarked
public class ParticleData {
    private static final String PARTICLE_IS_NOT_REDSTONE = "Particle is not REDSTONE";
    private Particle particle;
    private int count;
    private double spreadX;
    private double spreadY;
    private double spreadZ;
    private double extra;
    private @Nullable Object data;
    private boolean force;
    
    /**
     * Creates a new ParticleData object with the given data.<br>
     * It's recommended to use {@link ParticleBuilder} for easier particle creation.
     *
     * @param particle The particle to display
     * @param count    The number of particles to display
     * @param spreadX  The spread of the particles on the X axis
     * @param spreadY  The spread of the particles on the Y axis
     * @param spreadZ  The spread of the particles on the Z axis
     * @param extra    The extra data of the particle
     * @param data     The data of the particle
     * @param force    Whether the particle should be forced to be displayed
     */
    @Kapi
    public ParticleData(
            Particle particle, int count,
            double spreadX, double spreadY, double spreadZ,
            double extra, @Nullable Object data, boolean force
    ) {
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
     * Note: modifying the given vector after setting it will not affect the spread.
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
    public @Nullable Object getData() {
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
    public @Nullable DustOptions getRedstoneData() {
        if (particle != Particle.REDSTONE) {
            throw new IllegalStateException(PARTICLE_IS_NOT_REDSTONE);
        }
        return (DustOptions) data;
    }
    
    /**
     * Sets the color of the redstone particle.
     *
     * @param color The color of the particle
     * @throws IllegalStateException If the particle is not REDSTONE
     * @throws NullPointerException  If data (dust options) is null
     */
    @Kapi
    public void setRedstoneColor(Color color) {
        if (particle != Particle.REDSTONE) {
            throw new IllegalStateException(PARTICLE_IS_NOT_REDSTONE);
        }
        DustOptions data = (DustOptions) this.data;
        if (data == null) {
            throw new NullPointerException("setRedstoneColor called on an object with null data");
        }
        this.data = new DustOptions(color, data.getSize());
    }
    
    /**
     * Sets the size of the redstone particle.
     *
     * @param size The size of the particle
     * @throws IllegalStateException If the particle is not REDSTONE
     */
    @Kapi
    public void setRedstoneSize(float size) {
        if (particle != Particle.REDSTONE) {
            throw new IllegalStateException(PARTICLE_IS_NOT_REDSTONE);
        }
        DustOptions data = (DustOptions) this.data;
        if (data == null) {
            throw new NullPointerException("setRedstoneSize called on an object with null data");
        }
        this.data = new DustOptions(data.getColor(), size);
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
    
    /**
     * Spawns the particle at the given location.
     *
     * @param location The location to spawn the particle at
     */
    @Kapi
    public void spawn(Location location) {
        World world = location.getWorld();
        if (world == null) {
            throw new IllegalStateException("The location's world cannot be null");
        }
        
        world.spawnParticle(
                particle,
                location,
                count,
                spreadX,
                spreadY,
                spreadZ,
                extra,
                data,
                force
        );
    }
    
    /**
     * Spawns the particle at the given location.
     *
     * @param world  The world to spawn the particle in
     * @param vector The X, Y, and Z coordinates to spawn the particle at
     */
    @Kapi
    public void spawn(World world, Vector vector) {
        world.spawnParticle(
                particle,
                vector.getX(),
                vector.getY(),
                vector.getZ(),
                count,
                spreadX,
                spreadY,
                spreadZ,
                extra,
                data,
                force
        );
    }
    
    /**
     * Spawns the particle at the given location.
     *
     * @param world The world to spawn the particle in
     * @param x     The X coordinate to spawn the particle at
     * @param y     The Y coordinate to spawn the particle at
     * @param z     The Z coordinate to spawn the particle at
     */
    @Kapi
    public void spawn(World world, double x, double y, double z) {
        world.spawnParticle(
                particle,
                x,
                y,
                z,
                count,
                spreadX,
                spreadY,
                spreadZ,
                extra,
                data,
                force
        );
    }
}
