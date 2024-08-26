/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
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
