/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.engine.ecs;

import io.github.kapimc.kapi.engine.ecs.events.EntityCollisionEvent;
import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.engine.Object3D;
import io.github.kapimc.kapi.engine.Template3D;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * A collection of built-in systems that can be applied to an {@link Object3D}.<br>
 * Most systems require a component to be present on the object,
 * go to the component's documentation to see accepted types and systems that use it.<br>
 * See {@link Component} for a list of all the built-in components.<br>
 * <br>
 * To use a system on an {@link Object3D} or a {@link Template3D}, call:
 * {@link Template3D#addSystem(SystemTrigger, Consumer)}<br>
 * {@link Object3D#addSystem(SystemTrigger, Consumer)}<br>
 * Example:
 * <pre><code>
 *     Template3D template = ...
 *     template.addSystem(SystemTrigger.TICK, System::velocity);
 * </code></pre>
 * Most of the time it's recommended to use {@link SystemTrigger#TICK} as the trigger,
 * as it will be called every tick, but you can also use any other trigger
 * like an interval or event, see {@link SystemTrigger} for more information.<br>
 * <br>
 * Some systems require additional parameters like a callback function,
 * see {@link #entityCollision(Object3D, Consumer)} for an example.<br>
 */
@Kapi
public class System {
    
    private System() {
        throw new AssertionError("System should not be instantiated");
    }
    
    /**
     * Moves the object by its velocity.<br>
     * Components:
     * <ul>
     *     <li>{@value Component#VELOCITY}</li>
     * </ul>
     * If the component is not present or is not of the correct type,
     * this system will silently do nothing.
     *
     * @param instance The object to apply the system to.
     */
    @Kapi
    public static void velocity(Object3D instance) {
        if (!instance.has(Component.VELOCITY)) return;
        Object object = instance.get(Component.VELOCITY);
        if (object instanceof Vector3fc velocity) {
            instance.getMutableTransform().translate(velocity);
        } else if (object instanceof Vector velocity) {
            instance.getMutableTransform().translate(velocity.toVector3f());
        }
    }
    
    /**
     * Modifies the object's velocity by its acceleration.<br>
     * Components:
     * <ul>
     *     <li>{@value Component#VELOCITY}</li>
     *     <li>{@value Component#ACCELERATION}</li>
     * </ul>
     * If either the velocity or acceleration component is not present
     * or are not of the correct type, this system will silently do nothing.
     *
     * @param instance The object to apply the system to.
     */
    @Kapi
    public static void acceleration(Object3D instance) {
        if (!instance.has(Component.ACCELERATION)) return;
        if (!instance.has(Component.VELOCITY)) return;
        
        Object object = instance.get(Component.ACCELERATION);
        Vector3fc acceleration;
        Vector3f velocity;
        
        if (object instanceof Vector3fc acc) {
            acceleration = acc;
        } else if (object instanceof Vector acc) {
            acceleration = acc.toVector3f();
        } else return;
        
        object = instance.get(Component.VELOCITY);
        if (object instanceof Vector3fc vel) {
            velocity = new Vector3f(vel);
        } else if (object instanceof Vector vel) {
            velocity = vel.toVector3f();
        } else return;
        
        instance.set(Component.VELOCITY, velocity.add(acceleration));
    }
    
    /**
     * Checks if the object is within a certain radius of a living entity.<br>
     * Components:
     * <ul>
     *     <li>{@value Component#COLLISION_SHAPE}</li>
     * </ul>
     * If collision is detected, an EntityCollisionEvent will be triggered
     * with all the entities that were collided with.<br>
     * Note: when using a bounding box, it'll be shifted by the center + offset (if any)
     *
     * @param instance     The object to apply the system to.
     * @param callback     The callback function to call when a collision is detected.
     * @param centerOffset The offset to apply to the center of the object.
     * @see #entityCollision(Object3D, Consumer)
     */
    @Kapi
    public static void entityCollision(
        Object3D instance, Consumer<EntityCollisionEvent> callback, Vector centerOffset
    ) {
        if (!instance.has(Component.COLLISION_SHAPE)) return;
        Object collisionShape = instance.get(Component.COLLISION_SHAPE);
        
        World world = instance.getWorld();
        Location location = instance.getWorldLocation().add(centerOffset);
        Collection<Entity> entities;
        
        if (collisionShape instanceof BoundingBox boundingBox) {
            boundingBox.shift(location);
            entities = world.getNearbyEntities(boundingBox);
        } else if (collisionShape instanceof Double radius) {
            entities = world.getNearbyEntities(location, radius, radius, radius);
        } else if (collisionShape instanceof Vector3fc radius) {
            entities = world.getNearbyEntities(location, radius.x(), radius.y(), radius.z());
        } else if (collisionShape instanceof Vector radius) {
            entities =
                world.getNearbyEntities(location, radius.getX(), radius.getY(), radius.getZ());
        } else return;
        
        EntityCollisionEvent event = new EntityCollisionEvent(instance, entities);
        callback.accept(event);
    }
    
    /**
     * See {@link #entityCollision(Object3D, Consumer, Vector)} for more information.<br>
     * This system is the exact same, but with a default offset of (0, 0, 0),
     * which means no offset, the center of the object will be used.
     *
     * @param instance The object to apply the system to.
     * @param callback The callback function to call when a collision is detected.
     */
    @Kapi
    public static void entityCollision(Object3D instance, Consumer<EntityCollisionEvent> callback) {
        entityCollision(instance, callback, new Vector(0, 0, 0));
    }
    
}
