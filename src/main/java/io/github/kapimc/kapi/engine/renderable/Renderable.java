/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.engine.renderable;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.engine.Object3D;
import org.bukkit.World;
import org.bukkit.util.Vector;

/**
 * Represents an object that can be rendered by a {@link Object3D}.
 */
@Kapi
public interface Renderable {
    
    /**
     * Spawns the object in the world at the given point.
     * This starts rendering the object.
     *
     * @param world the world to spawn the object in
     * @param point the point (location) to spawn the object at
     * @throws IllegalStateException if the object is already spawned
     */
    @Kapi
    void spawn(World world, Vector point);
    
    /**
     * This is called every tick to render the object.
     * Only called when {@link #isSpawned()} returns true.
     *
     * @param world the world to render the object in
     * @param point the point (location) to render the object at
     */
    @Kapi
    void render(World world, Vector point);
    
    /**
     * Despawns the object in the world at the given point.
     * This stops rendering the object.
     *
     * @param world the world to despawn the object in
     * @param point the point (location) to despawn the object at
     * @throws IllegalStateException if the object is not spawned
     */
    @Kapi
    void despawn(World world, Vector point);
    
    /**
     * @return true if the object is spawned, false otherwise
     */
    @Kapi
    boolean isSpawned();
    
    /**
     * @return a deep copy of this Renderable
     */
    @Kapi
    Renderable clone();
}
