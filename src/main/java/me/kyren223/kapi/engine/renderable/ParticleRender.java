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

package me.kyren223.kapi.engine.renderable;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.data.ParticleData;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;

/**
 * Used for rendering particles.
 */
@Kapi
@NullMarked
public class ParticleRender implements Renderable {
    
    private ParticleData particle;
    
    @Override
    public void spawn(World world, Vector point) {
        // Do nothing, particles are rendered instantly
    }
    
    @Override
    public void render(World world, Vector point) {
        world.spawnParticle(
                particle.getParticle(),
                point.getX(), point.getY(), point.getZ(),
                particle.getCount(),
                particle.getSpreadX(), particle.getSpreadY(), particle.getSpreadZ(),
                particle.getExtra(),
                particle.getData(),
                particle.isForce()
        );
    }
    
    @Override
    public void despawn(World world, Vector point) {
        // Do nothing, particles are automatically despawned after some time
    }
    
    @Override
    public boolean isSpawned() {
        return true;
    }
    
    @Kapi
    public ParticleData getParticle() {
        return particle;
    }
    
    @Kapi
    public void setParticle(ParticleData particle) {
        this.particle = particle;
    }
    
    @Kapi
    public ParticleRender(ParticleData particle) {
        this.particle = particle;
    }
    
    @Override
    @Kapi
    public Renderable clone() {
        return new ParticleRender(new ParticleData(particle));
    }
}
