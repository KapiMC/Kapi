/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.engine;

import io.github.kapimc.kapi.data.Pair;
import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.data.Option;
import io.github.kapimc.kapi.engine.ecs.SystemTrigger;
import org.bukkit.Location;
import org.bukkit.World;
import org.joml.Matrix4f;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Represents a 3D object template.
 */
@Kapi
public class Template3D {
    private final List<Point> points;
    private final HashMap<String,Pair<Matrix4f,Template3D>> children;
    private final HashMap<String,@Nullable Object> components;
    private final HashMap<String,List<Consumer<Object3D>>> events;
    private final List<Pair<SystemTrigger,Consumer<Object3D>>> tasks;
    
    
    @Kapi
    public Template3D(List<Point> points) {
        this.points = points;
        this.children = new HashMap<>();
        this.components = new HashMap<>();
        this.events = new HashMap<>();
        this.tasks = new ArrayList<>();
    }
    
    /**
     * Gets the points of this template.<br>
     * Modifying the returned list will affect the template's points.<br>
     * <br>
     * To add a point, use {@link #addPoint(Point)}<br>
     * To remove a point, use {@link #removePointIf(Predicate)}<br>
     *
     * @return A stream of points
     */
    @Kapi
    public Stream<Point> getPoints() {
        return points.stream();
    }
    
    /**
     * Adds a point to this template.<br>
     * <br>
     * If you want to add multiple points and know them in advance,
     * consider using {@link #addPoints(List)} or {@link #addPoints(Point...)}<br>
     * This can improve performance by reducing the amount of resizing the list has to do.<br>
     *
     * @param point The point to add
     */
    @Kapi
    public void addPoint(Point point) {
        points.add(point);
    }
    
    /**
     * Adds multiple points to this template.<br>
     * Also see {@link #addPoint(Point)} and {@link #addPoints(Point...)}<br>
     * <br>
     * This can improve performance by reducing the amount of resizing the list has to do.<br>
     *
     * @param points The points to add
     */
    @Kapi
    public void addPoints(List<Point> points) {
        this.points.addAll(points);
    }
    
    /**
     * Adds multiple points to this template.<br>
     * Also see {@link #addPoint(Point)} and {@link #addPoints(List)}<br>
     * <br>
     * This can improve performance by reducing the amount of resizing the list has to do.<br>
     *
     * @param points The points to add
     */
    @Kapi
    public void addPoints(Point... points) {
        this.points.addAll(Arrays.asList(points));
    }
    
    /**
     * Removes all points that the predicate returns true for.
     *
     * @param predicate The predicate
     */
    @Kapi
    public void removePointIf(Predicate<Point> predicate) {
        points.removeIf(predicate);
    }
    
    /**
     * Adds a child to this template.<br>
     * See {@link #addChild(String, Template3D, Matrix4f)} for adding a child with a transform.<br>
     *
     * @param name  The name of the child (used to retrieve it later)
     * @param child The child's template
     */
    @Kapi
    public void addChild(String name, Template3D child) {
        children.put(name, Pair.of(new Matrix4f(), child));
    }
    
    /**
     * Adds a child to this template with a transform.<br>
     * See {@link #addChild(String, Template3D)} for adding a child without a transform.<br>
     *
     * @param name      The name of the child (used to retrieve it later)
     * @param child     The child's template
     * @param transform The transform of the child relative to this template
     */
    @Kapi
    public void addChild(
        String name, Template3D child, Matrix4f transform
    ) {
        children.put(name, Pair.of(transform, child));
    }
    
    /**
     * Removes a child from this template.
     *
     * @param name The name of the child to remove
     * @return The removed child's template
     */
    @Kapi
    public Option<Template3D> removeChild(String name) {
        return Option.of(children.remove(name)).map(Pair::getSecond);
    }
    
    /**
     * Removes all children that the predicate returns true for.
     *
     * @param predicate The predicate
     */
    @Kapi
    public void removeChildIf(
        Predicate<Map.Entry<String,Pair<Matrix4f,Template3D>>> predicate
    ) {
        children.entrySet().removeIf(predicate);
    }
    
    /**
     * Gets the children of this template.
     *
     * @return A stream of the children names, transforms, and templates
     */
    @Kapi
    public Stream<Map.Entry<String,Pair<Matrix4f,Template3D>>> getChildren() {
        return children.entrySet().stream();
    }
    
    /**
     * Gets a child by name.
     *
     * @param name The name of the child
     * @return The child's template and it's relative transform or null if the child doesn't exist
     */
    @Kapi
    public Option<Pair<Matrix4f,Template3D>> getChild(String name) {
        return Option.of(children.get(name));
    }
    
    
    /**
     * Gets the default value of a component.<br>
     * This value will be set on all instances of this template when they are spawned.
     *
     * @param key   The name of the component
     * @param value The default value of the component
     */
    @Kapi
    public void setDefault(String key, @Nullable Object value) {
        components.put(key, value);
    }
    
    /**
     * Adds a system to this template.
     *
     * @param trigger The trigger to activate the system
     * @param system  The system
     * @return This template for chaining
     */
    @Kapi
    public Template3D addSystem(
        SystemTrigger trigger, Consumer<Object3D> system
    ) {
        if (trigger.isEvent()) {
            events.computeIfAbsent(trigger.getEvent(), k -> new ArrayList<>()).add(system);
        } else {
            tasks.add(Pair.of(trigger, system));
        }
        return this;
    }
    
    // Package-private
    HashMap<String,List<Consumer<Object3D>>> getEvents() {
        Consumer<Object3D> setDefaultsSystem = instance -> components.forEach(instance::set);
        events.computeIfAbsent(SystemTrigger.SPAWN_EVENT, k -> new ArrayList<>())
            .add(setDefaultsSystem);
        return events;
    }
    
    List<Pair<SystemTrigger,Consumer<Object3D>>> getTasks() {
        return tasks;
    }
    
    /**
     * Creates a new instance of this template at the specified world with the given transform.<br>
     * See {@link #newInstance(World, Matrix4f)} for spawning the object without a parent.<br>
     * See {@link #newInstance(Location, Object3D)} for spawning the object using a location.
     *
     * @param world     The world of the object
     * @param transform The transform of the object
     * @param parent    The parent object
     * @return A new Object3D instance
     */
    @Kapi
    public Object3D newInstance(
        World world, Matrix4f transform, Object3D parent
    ) {
        return new Object3D(this, world, transform, parent);
    }
    
    /**
     * Creates a new instance of this template at the specified world with the given transform.<br>
     * See {@link #newInstance(World, Matrix4f, Object3D)} for spawning the object with a
     * parent.<br>
     * See {@link #newInstance(Location)} for spawning the object using a location.
     *
     * @param world     The world of the object
     * @param transform The transform of the object
     * @return A new Object3D instance
     */
    @Kapi
    public Object3D newInstance(World world, Matrix4f transform) {
        return new Object3D(this, world, transform, null);
    }
    
    /**
     * Creates a new instance of this template at the specified location.<br>
     * See {@link #newInstance(Location)} for spawning the object without a parent.<br>
     * See {@link #newInstance(World, Matrix4f, Object3D)} for spawning the object using a
     * transform.
     *
     * @param location The location to spawn the object at
     * @param parent   The parent object
     * @return A new Object3D instance
     * @throws IllegalArgumentException If the world of the location is null
     */
    @Kapi
    public Object3D newInstance(Location location, Object3D parent) {
        World world = location.getWorld();
        if (world == null) {
            throw new IllegalArgumentException("Location world cannot be null");
        }
        return new Object3D(
            this,
            world,
            new Matrix4f().translate(location.toVector().toVector3f()),
            parent
        );
    }
    
    /**
     * Creates a new instance of this template at the specified location.<br>
     * See {@link #newInstance(Location, Object3D)} for spawning the object with a parent.<br>
     * See {@link #newInstance(World, Matrix4f)} for spawning the object using a transform.
     *
     * @param location The location to spawn the object at
     * @return A new Object3D instance
     * @throws IllegalArgumentException If the world of the location is null
     */
    @Kapi
    public Object3D newInstance(Location location) {
        World world = location.getWorld();
        if (world == null) {
            throw new IllegalArgumentException("Location world cannot be null");
        }
        return new Object3D(
            this,
            world,
            new Matrix4f().translate(location.toVector().toVector3f()),
            null
        );
    }
}
