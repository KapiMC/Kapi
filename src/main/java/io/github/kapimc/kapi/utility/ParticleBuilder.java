/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.utility;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.data.ParticleData;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.jspecify.annotations.Nullable;

/**
 * A builder for creating {@link ParticleData}.
 */
@Kapi
public final class ParticleBuilder {
    
    private final Particle particle;
    private int count;
    private double spreadX;
    private double spreadY;
    private double spreadZ;
    private double extra;
    private @Nullable Object data;
    private boolean force;
    
    private ParticleBuilder(Particle particle) {
        this.particle = particle;
        count = 1;
        spreadX = 0;
        spreadY = 0;
        spreadZ = 0;
        extra = 0;
        data = null;
        force = false;
    }
    
    /**
     * Creates a new particle builder.
     * <p>
     * All values will be set to their default values, which are:
     * <ul>
     *     <li>count = 1;
     *     <li>spreadX = 0;
     *     <li>spreadY = 0;
     *     <li>spreadZ = 0;
     *     <li>extra = 0;
     *     <li>data = null;
     *     <li>force = false;
     * </ul>
     *
     * @param particle the particle to create
     * @return a new particle builder
     * @see #createRedstone(Color, float)
     */
    @Kapi
    public static ParticleBuilder create(Particle particle) {
        return new ParticleBuilder(particle);
    }
    
    /**
     * Creates a new particle builder with a redstone particle.
     * For more information see {@link #create(Particle)}.
     *
     * @param color the color of the redstone particle
     * @param size  the size of the redstone particle
     * @return a new particle builder
     */
    @Kapi
    public static ParticleBuilder createRedstone(Color color, float size) {
        return create(Particle.REDSTONE).data(new Particle.DustOptions(color, size));
    }
    
    /**
     * @param count the number of particles
     * @return this, for chaining
     */
    @Kapi
    public ParticleBuilder count(int count) {
        this.count = count;
        return this;
    }
    
    /**
     * Spread indicates the maximum distance from the origin
     * where particles are able to spawn.
     *
     * @param spreadX the maximum spread on the x-axis
     * @param spreadY the maximum spread on the y-axis
     * @param spreadZ the maximum spread on the z-axis
     * @return this, for chaining
     */
    @Kapi
    public ParticleBuilder spread(double spreadX, double spreadY, double spreadZ) {
        this.spreadX = spreadX;
        this.spreadY = spreadY;
        this.spreadZ = spreadZ;
        return this;
    }
    
    /**
     * Spread indicates the maximum distance from the origin
     * where particles are able to spawn.
     *
     * @param spread the maximum spread on each axis
     * @return this, for chaining
     */
    @Kapi
    public ParticleBuilder spread(Vector spread) {
        this.spreadX = spread.getX();
        this.spreadY = spread.getY();
        this.spreadZ = spread.getZ();
        return this;
    }
    
    /**
     * Spread indicates the maximum distance from the origin
     * where particles are able to spawn.
     *
     * @param spread the maximum spread on all axes
     * @return this, for chaining
     */
    @Kapi
    public ParticleBuilder spread(double spread) {
        this.spreadX = spread;
        this.spreadY = spread;
        this.spreadZ = spread;
        return this;
    }
    
    /**
     * The extra data is a value that can be used to modify the particle.
     * Usually it indicates the speed of the particle, but it may differ
     * depending on the particle.
     *
     * @param extra the extra data
     * @return this, for chaining
     */
    @Kapi
    public ParticleBuilder extra(double extra) {
        this.extra = extra;
        return this;
    }
    
    /**
     * The data is extra attributes of the particle.
     * For example, the dust options of a redstone particle.
     *
     * @param data the data of the particle
     * @return this, for chaining
     */
    @Kapi
    public ParticleBuilder data(final @Nullable Object data) {
        this.data = data;
        return this;
    }
    
    /**
     * When force is true, the particle(s) will be sent to all players.
     * When force is false, the particle(s) will be sent to players within 32.0 blocks (exclusive).
     *
     * @param force whether to forcibly send the particle to all players or not
     * @return this, for chaining
     */
    @Kapi
    public ParticleBuilder force(boolean force) {
        this.force = force;
        return this;
    }
    
    /**
     * @return the built particle data
     */
    @Kapi
    public ParticleData build() {
        return new ParticleData(particle, count, spreadX, spreadY, spreadZ, extra, data, force);
    }
    
}
