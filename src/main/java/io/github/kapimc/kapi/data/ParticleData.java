/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.data;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.utility.ParticleBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jspecify.annotations.Nullable;

/**
 * Holds the data for a particle.
 * Use {@link ParticleBuilder} to easily create this class.
 */
@Kapi
public final class ParticleData {
    
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
     * Creates a new ParticleData object with the given data.
     * It's recommended to use {@link ParticleBuilder} to easily create this class.
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
     * Copy constructor.
     * <p>
     * This is a shallow copy due to the data object not being deep copied.
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
    
    /**
     * @return the particle type
     */
    @Kapi
    public Particle getParticle() {
        return particle;
    }
    
    /**
     * @param particle the particle type
     */
    @Kapi
    public void setParticle(Particle particle) {
        this.particle = particle;
    }
    
    /**
     * @return the number of particles
     */
    @Kapi
    public int getCount() {
        return count;
    }
    
    /**
     * @param count the number of particles
     */
    @Kapi
    public void setCount(int count) {
        this.count = count;
    }
    
    /**
     * The spread is the maximum distance from the origin
     * where particles are able to spawn.
     *
     * @return the spread on the X axis
     */
    @Kapi
    public double getSpreadX() {
        return spreadX;
    }
    
    /**
     * The spread is the maximum distance from the origin
     * where particles are able to spawn.
     *
     * @param spreadX the spread on the X axis
     */
    @Kapi
    public void setSpreadX(double spreadX) {
        this.spreadX = spreadX;
    }
    
    /**
     * The spread is the maximum distance from the origin
     * where particles are able to spawn.
     *
     * @return the spread on the Y axis
     */
    @Kapi
    public double getSpreadY() {
        return spreadY;
    }
    
    /**
     * The spread is the maximum distance from the origin
     * where particles are able to spawn.
     *
     * @param spreadY the spread on the Y axis
     */
    @Kapi
    public void setSpreadY(double spreadY) {
        this.spreadY = spreadY;
    }
    
    /**
     * The spread is the maximum distance from the origin
     * where particles are able to spawn.
     *
     * @return the spread on the Z axis
     */
    @Kapi
    public double getSpreadZ() {
        return spreadZ;
    }
    
    /**
     * The spread is the maximum distance from the origin
     * where particles are able to spawn.
     *
     * @param spreadZ the spread on the Z axis
     */
    @Kapi
    public void setSpreadZ(double spreadZ) {
        this.spreadZ = spreadZ;
    }
    
    /**
     * The spread is the maximum distance from the origin
     * where particles are able to spawn.
     *
     * @return a new vector with the spread of all axes
     */
    @Kapi
    public Vector getSpread() {
        return new Vector(spreadX, spreadY, spreadZ);
    }
    
    /**
     * The spread is the maximum distance from the origin
     * where particles are able to spawn.
     *
     * @param vector the new spread
     */
    @Kapi
    public void setSpread(Vector vector) {
        this.spreadX = vector.getX();
        this.spreadY = vector.getY();
        this.spreadZ = vector.getZ();
    }
    
    /**
     * The spread is the maximum distance from the origin
     * where particles are able to spawn.
     *
     * @param spread the new spread on all axes
     */
    @Kapi
    public void setSpread(double spread) {
        this.spreadX = spread;
        this.spreadY = spread;
        this.spreadZ = spread;
    }
    
    /**
     * The extra data is a value that can be used to modify the particle.
     * Usually it indicates the speed of the particle, but it may differ
     * depending on the particle.
     *
     * @return the extra data of the particle
     */
    @Kapi
    public double getExtra() {
        return extra;
    }
    
    /**
     * The extra data is a value that can be used to modify the particle.
     * Usually it indicates the speed of the particle, but it may differ
     * depending on the particle.
     *
     * @param extra the extra data of the particle
     */
    @Kapi
    public void setExtra(double extra) {
        this.extra = extra;
    }
    
    /**
     * The data is extra attributes of the particle.
     * For example, the dust options of a redstone particle.
     *
     * @return the data of the particle
     */
    @Kapi
    public @Nullable Object getData() {
        return data;
    }
    
    /**
     * The data is extra attributes of the particle.
     * For example, the dust options of a redstone particle.
     *
     * @param data the data of the particle
     */
    @Kapi
    public void setData(Object data) {
        this.data = data;
    }
    
    /**
     * @param dustOptions the dust options of the redstone particle
     * @throws IllegalStateException if the particle is not REDSTONE
     */
    @Kapi
    public void setRedstoneData(DustOptions dustOptions) {
        if (particle != Particle.REDSTONE) {
            throw new IllegalStateException(PARTICLE_IS_NOT_REDSTONE);
        }
        this.data = dustOptions;
    }
    
    /**
     * Does not clone the color.
     * Any changes to the given color will also affect this particle.
     *
     * @param color the color of the redstone particle
     * @param size  the size of the redstone particle
     * @throws IllegalStateException if the particle is not REDSTONE
     */
    @Kapi
    public void setRedstoneData(Color color, float size) {
        if (particle != Particle.REDSTONE) {
            throw new IllegalStateException(PARTICLE_IS_NOT_REDSTONE);
        }
        this.data = new Particle.DustOptions(color, size);
    }
    
    /**
     * @return the dust options of the redstone particle
     * @throws IllegalStateException if the particle is not REDSTONE
     */
    @Kapi
    public @Nullable DustOptions getRedstoneData() {
        if (particle == Particle.REDSTONE) return (DustOptions) data;
        throw new IllegalStateException(PARTICLE_IS_NOT_REDSTONE);
    }
    
    /**
     * If data is null, a new dust options will be created with size 1.
     * <p>
     * Does not clone the color.
     * Any changes to the given color will also affect this particle.
     *
     * @param color the color of the redstone particle
     * @throws IllegalStateException if the particle is not REDSTONE
     */
    @Kapi
    public void setRedstoneColor(Color color) {
        if (particle != Particle.REDSTONE) {
            throw new IllegalStateException(PARTICLE_IS_NOT_REDSTONE);
        }
        float size = data == null ? 1 : ((DustOptions) data).getSize();
        this.data = new DustOptions(color, size);
    }
    
    /**
     * If data is null, a new dust options will be created with color {@link Color#RED}.
     *
     * @param size The size of the particle
     * @throws IllegalStateException if the particle is not REDSTONE
     */
    @Kapi
    public void setRedstoneSize(float size) {
        if (particle != Particle.REDSTONE) {
            throw new IllegalStateException(PARTICLE_IS_NOT_REDSTONE);
        }
        Color color = data == null ? Color.RED : ((DustOptions) data).getColor();
        this.data = new DustOptions(color, size);
    }
    
    /**
     * When force is true, the particle(s) will be sent to all players.
     * When force is false, the particle(s) will be sent to players within 32.0 blocks (exclusive).
     *
     * @return the force of the particle
     */
    @Kapi
    public boolean isForce() {
        return force;
    }
    
    /**
     * When force is true, the particle(s) will be sent to all players.
     * When force is false, the particle(s) will be sent to players within 32.0 blocks (exclusive).
     *
     * @param force the particle force
     */
    @Kapi
    public void setForce(boolean force) {
        this.force = force;
    }
    
    /**
     * @param location the location to spawn the particle at
     * @throws IllegalStateException if the location's world is null
     */
    @Kapi
    public void spawn(Location location) {
        if (location.getWorld() == null) {
            throw new IllegalStateException("The location's world cannot be null");
        }
        location.getWorld().spawnParticle(
            particle, location, count,
            spreadX, spreadY, spreadZ,
            extra, data, force
        );
    }
    
    /**
     * @param world  the world to spawn the particle in
     * @param vector the X, Y, and Z coordinates to spawn the particle at
     */
    @Kapi
    public void spawn(World world, Vector vector) {
        world.spawnParticle(
            particle, vector.getX(), vector.getY(), vector.getZ(), count,
            spreadX, spreadY, spreadZ,
            extra, data, force
        );
    }
    
    /**
     * @param world the world to spawn the particle in
     * @param x     the X coordinate to spawn the particle at
     * @param y     the Y coordinate to spawn the particle at
     * @param z     the Z coordinate to spawn the particle at
     */
    @Kapi
    public void spawn(World world, double x, double y, double z) {
        world.spawnParticle(particle, x, y, z, count, spreadX, spreadY, spreadZ, extra, data, force);
    }
}
