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

package me.kyren223.kapi.engine.ecs.events;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.engine.Object3D;
import me.kyren223.kapi.engine.ecs.System;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Stores information about an entity collision event.<br>
 * See {@link System#entityCollision(Object3D, Consumer)} for more information.
 */
@Kapi
@NullMarked
public class EntityCollisionEvent {
    private final Object3D collider;
    private final List<Entity> collidedEntities;
    private final List<LivingEntity> collidedLivingEntities;
    private final List<Player> collidedPlayers;
    
    public EntityCollisionEvent(
            final Object3D collider, final Collection<Entity> collidedEntities
    ) {
        this.collider = collider;
        this.collidedEntities = new ArrayList<>();
        this.collidedLivingEntities = new ArrayList<>();
        this.collidedPlayers = new ArrayList<>();
        for (Entity entity : collidedEntities) {
            collidedEntities.add(entity);
            if (entity instanceof LivingEntity livingEntity) {
                collidedLivingEntities.add(livingEntity);
                if (livingEntity instanceof Player player) {
                    collidedPlayers.add(player);
                }
            }
        }
    }
    
    /**
     * @return The collider that was collided with.
     */
    @Kapi
    public Object3D getCollider() {
        return collider;
    }
    
    /**
     * @return The entities that were collided with the collider.
     */
    @Kapi
    public List<Entity> getEntities() {
        return collidedEntities;
    }
    
    /**
     * @return The living entities that were collided with the collider.
     */
    @Kapi
    public List<LivingEntity> getLivingEntities() {
        return collidedLivingEntities;
    }
    
    /**
     * @return The players that were collided with the collider.
     */
    @Kapi
    public List<Player> getPlayers() {
        return collidedPlayers;
    }
}
