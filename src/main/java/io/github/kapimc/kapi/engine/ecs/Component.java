/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.engine.ecs;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.engine.Object3D;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.joml.Vector3fc;

import java.util.function.Consumer;

/**
 * A class that contains all the names of the built-in components.<br>
 * Makes it easier to reference components in the code,
 * and reduces the chance of typos from using strings directly.
 */
@Kapi
public class Component {
    
    private Component() {
        throw new AssertionError("Component should not be instantiated");
    }
    
    /**
     * Related Systems: {@link System#velocity(Object3D)}<br>
     * Supported types: {@link Vector3fc} and {@link Vector}
     */
    @Kapi
    public static final String VELOCITY = "velocity";
    
    /**
     * Related Systems: {@link System#acceleration(Object3D)}<br>
     * Supported types: {@link Vector3fc} and {@link Vector}
     */
    @Kapi
    public static final String ACCELERATION = "acceleration";
    
    /**
     * Related Systems: {@link System#entityCollision(Object3D, Consumer)}
     * Supported types: {@link Double}, {@link Vector},
     * {@link Vector3fc} and {@link BoundingBox}
     */
    @Kapi
    public static final String COLLISION_SHAPE = "collision_shape";
}
