/*
 * Copyright (c) 2024 Kapi Contributors. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted if the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions, the following disclaimer and the list of contributors.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation and/or
 *    other materials provided with the distribution.
 *
 * 3. The buyer of the "Kapi" API is granted the right to use this software
 *    as a dependency in their own software projects. However, the buyer
 *    may not resell or distribute the "Kapi" API, in whole or in part, to other parties.
 *
 * 4. The buyer may include the "Kapi" API in a "fat jar" along with their own code.
 *    The license for the "fat jar" is at the buyer's discretion and may allow
 *    redistribution of the "fat jar", but the "Kapi" API code inside the "fat jar"
 *    must not be modified.
 *
 * 5. Neither the name "Kapi" nor the names of its contributors may be used to endorse
 *    or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY "Kapi" API, AND ITS CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL "Kapi" API, AND CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Kapi Contributors:
 * - Kyren223
 */

package me.kyren223.kapi.utility;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.data.ParticleData;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A builder for creating {@link ParticleData} objects.
 */
@Kapi
@NullMarked
public class ParticleBuilder {
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
     * Creates a new particle builder.<br>
     * For redstone particles, using {@link #createRedstone(Color, float)}
     * is recommended.<br>
     * <br>
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
     * @param particle The particle to create
     * @return A new particle builder
     * @see #createRedstone(Color, float)
     */
    @Kapi
    public static ParticleBuilder create(Particle particle) {
        return new ParticleBuilder(particle);
    }
    
    /**
     * Creates a new particle builder with a redstone particle.<br>
     * For more information see {@link #create(Particle)}.
     *
     * @param color The color of the redstone particle
     * @param size  The size of the redstone particle
     * @return A new particle builder
     * @see #create(Particle)
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
    public ParticleBuilder data(final @Nullable Object data) {
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
