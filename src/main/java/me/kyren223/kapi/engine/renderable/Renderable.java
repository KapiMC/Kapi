/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package me.kyren223.kapi.engine.renderable;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.engine.Object3D;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;

/**
 * Represents an object that can be rendered by a {@link Object3D}.
 */
@Kapi
@NullMarked
public interface Renderable {
    @Kapi
    void spawn(World world, Vector point);
    
    @Kapi
    void render(World world, Vector point);
    
    @Kapi
    void despawn(World world, Vector point);
    
    @Kapi
    boolean isSpawned();
    
    @Kapi
    Renderable clone();
}
