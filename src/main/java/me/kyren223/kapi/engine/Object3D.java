/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package me.kyren223.kapi.engine;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.data.Option;
import me.kyren223.kapi.data.Pair;
import me.kyren223.kapi.engine.ecs.EcsEntity;
import me.kyren223.kapi.engine.ecs.SystemTrigger;
import me.kyren223.kapi.utility.Task;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Represents a 3D object.
 */
@Kapi
@NullMarked
public class Object3D implements EcsEntity {
    private final World world;
    private final Matrix4f transform;
    private @Nullable Matrix4fc cachedWorldTransform;
    private final List<Point> points;
    private final @Nullable Object3D parent;
    private final HashMap<String,Object3D> children;
    private final HashMap<String,@Nullable Object> components;
    private final HashMap<String,@Nullable List<Consumer<Object3D>>> events;
    private final List<Pair<SystemTrigger,Consumer<Object3D>>> tasks;
    private boolean cancel;
    private Visibility visibility;
    
    /**
     * Creates a new instance of this object
     *
     * @param template  The template to use for this object
     * @param world     The world to spawn this object in
     * @param transform The transform of this object, cloned to prevent modification
     * @param parent    The parent of this object, null if this object has no parent
     */
    public Object3D(
            Template3D template, World world, Matrix4f transform,
            @Nullable Object3D parent
    ) {
        this.parent = parent;
        this.world = world;
        this.transform = new Matrix4f(transform);
        this.points = new ArrayList<>();
        template.getPoints().forEach(point -> this.points.add(new Point(point)));
        
        this.components = new HashMap<>();
        this.events = new HashMap<>(template.getEvents());
        this.tasks = new ArrayList<>(template.getTasks());
        
        this.children = new HashMap<>();
        this.visibility = parent == null ? Visibility.VISIBLE : Visibility.INHERIT;
        template.getChildren().forEach(entry -> {
            Pair<Matrix4f,Template3D> value = entry.getValue();
            Object3D child = value.getSecond().newInstance(world, value.getFirst(), this);
            this.children.put(entry.getKey(), child);
        });
    }
    
    /**
     * Creates a new instance of this object's transform<br>
     * If you wish to modify the transform, use the method
     * {@link #transform(Consumer)}
     *
     * @return A read-only interface of this object's transform
     */
    @Kapi
    public Matrix4fc getTransform() {
        return transform;
    }
    
    /**
     * Get the transform of this object<br>
     * <br>
     * Note: this method invalidates the cached world transform,
     * as it's not possible to know if the transform was modified or not,
     * for more info see {@link #getWorldTransform()}<br>
     * <br>
     * If you don't need to modify the transform, use the method
     * {@link #getTransform()} instead<br>
     * <br>
     * If you modify the scale, please use the method
     * {@link #transform(Consumer)} instead so the event
     * {@link SystemTrigger#SCALE_CHANGED_EVENT} is triggered<br>
     * This method won't trigger the event, and can be used if you don't
     * want the overhead of checking if the scale was modified
     *
     * @return The mutable transform of this object
     */
    @Kapi
    public Matrix4f getMutableTransform() {
        invalidateCachedWorldTransform();
        return transform;
    }
    
    /**
     * Modifies this object's transform<br>
     * <br>
     * Note: this method invalidates the cached world transform,
     * for more info see {@link #getWorldTransform()}<br>
     * <br>
     * If the scale is modified, the event {@link SystemTrigger#SCALE_CHANGED_EVENT}
     * will be triggered<br>
     * If you don't modify the scale and don't want the overhead of
     * checking if the scale was modified, use the method
     * {@link #getMutableTransform()} instead
     *
     * @param transformer The consumer that modifies the transform
     */
    @Kapi
    public void transform(Consumer<Matrix4f> transformer) {
        Vector3f scale = new Vector3f();
        transform.getScale(scale);
        
        transformer.accept(transform);
        invalidateCachedWorldTransform();
        
        Vector3f newScale = new Vector3f();
        transform.getScale(newScale);
        if (!newScale.equals(scale, 0)) {
            triggerEvent(SystemTrigger.SCALE_CHANGED_EVENT);
        }
    }
    
    /**
     * Get the world transform of this object<br>
     * <br>
     * Calculates by recursively calling the parent's transform and
     * multiplying it by this object's transform.<br>
     * Until the parent is null where the transform is just a cloned
     * version of this object's transform<br>
     * <br>
     * Note: this method is cached, so it's safe to call it multiple times,
     * although the initial call (or calls after moving the object) will be slower
     *
     * @return A read-only interface of this object's world transform
     */
    @Kapi
    public Matrix4fc getWorldTransform() {
        if (cachedWorldTransform != null) {
            return cachedWorldTransform;
        }
        
        if (parent != null) {
            cachedWorldTransform = new Matrix4f(parent.getWorldTransform()).mul(transform);
        } else {
            cachedWorldTransform = new Matrix4f(transform);
        }
        
        return cachedWorldTransform;
    }
    
    /**
     * Gets the location of this object in world space.<br>
     * Note: this method MAY be expensive, for more info see {@link #getWorldTransform()}
     *
     * @return A new location with the object's position and world
     * @see #getWorldTransform()
     */
    @Kapi
    public Location getWorldLocation() {
        Vector3f position = new Vector3f();
        getWorldTransform().getTranslation(position);
        return new Location(world, position.x, position.y, position.z);
    }
    
    /**
     * Gets the points of this object.<br>
     * Modifying the returned list will affect the object's points.<br>
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
     * Adds a point to this object.<br>
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
     * Adds multiple points to this object.<br>
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
     * Adds multiple points to this object.<br>
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
    
    @Kapi
    public boolean hasParent() {
        return parent != null;
    }
    
    @Kapi
    public @Nullable Object3D getParent() {
        return parent;
    }
    
    /**
     * Adds a child to this object.<br>
     * See {@link #addChild(String, Template3D, Matrix4f)} for adding a child with a transform.<br>
     *
     * @param name  The name of the child (used to retrieve it later)
     * @param child The child's template
     */
    @Kapi
    public void addChild(String name, Template3D child) {
        Object3D object = child.newInstance(world, new Matrix4f(), this);
        children.put(name, object);
    }
    
    /**
     * Adds a child to this object with a transform.<br>
     * See {@link #addChild(String, Template3D)} for adding a child without a transform.<br>
     *
     * @param name      The name of the child (used to retrieve it later)
     * @param child     The child's template
     * @param transform The transform of the child relative to this template
     */
    public void addChild(
            String name, Template3D child, Matrix4f transform
    ) {
        Object3D object = child.newInstance(world, transform, this);
        children.put(name, object);
    }
    
    /**
     * Removes a child from this object.
     *
     * @param name The name of the child to remove
     * @return The removed child's object
     */
    @Kapi
    public Object3D removeChild(String name) {
        return children.remove(name);
    }
    
    /**
     * Removes all children that the predicate returns true for.
     *
     * @param predicate The predicate
     */
    @Kapi
    public void removeChildIf(Predicate<Map.Entry<String,Object3D>> predicate) {
        children.entrySet().removeIf(predicate);
    }
    
    /**
     * Gets the children of this object.
     *
     * @return A stream of the children names, transforms, and objects
     */
    @Kapi
    public Stream<Map.Entry<String,Object3D>> getChildren() {
        return children.entrySet().stream();
    }
    
    /**
     * Gets a child by name.
     *
     * @param name The name of the child
     * @return The child's object or null if the child doesn't exist
     */
    @Kapi
    public Option<Object3D> getChild(String name) {
        return Option.of(children.get(name));
    }
    
    /**
     * Gets the name of a child by the object.
     *
     * @param child The child object
     * @return The name of the child or null if the child doesn't exist
     */
    @Kapi
    public @Nullable String getNameOfChild(Object3D child) {
        for (Map.Entry<String,Object3D> entry : children.entrySet()) {
            if (entry.getValue() == child) return entry.getKey();
        }
        return null;
    }
    
    private void render() {
        // Render children
        children.values().forEach(Object3D::render);
        
        // Children have render priority
        if (!isVisible()) return;
        Matrix4fc worldTransform = getWorldTransform();
        getPoints().forEach(point -> {
            Vector3f position = worldTransform.transformPosition(point.getVector().toVector3f());
            point.getRenderable().render(world, Vector.fromJOML(position));
        });
        
    }
    
    private boolean shouldContinue() {
        return !cancel;
    }
    
    /**
     * Spawns this object and all of its children
     *
     * @param renderInterval The interval in ticks between each render call
     *                       Note: This interval does not affect display entities
     */
    @Kapi
    public void spawn(int renderInterval) {
        this.cancel = false;
        
        children.values().forEach(child -> child.spawn(renderInterval));
        
        points.forEach(point -> point.getRenderable().spawn(
                world, Vector.fromJOML(
                        getWorldTransform().transformPosition(point.getVector().toVector3f()))
        ));
        
        triggerEvent(SystemTrigger.SPAWN_EVENT);
        triggerEvent(SystemTrigger.SCALE_CHANGED_EVENT);
        for (Pair<SystemTrigger,Consumer<Object3D>> task : tasks) {
            SystemTrigger trigger = task.getFirst();
            Consumer<Object3D> system = task.getSecond();
            assert !trigger.isEvent();
            Task.run(() -> system.accept(this)).timer(trigger.getDelay(), trigger.getPeriod())
                .whileCondition(this::shouldContinue).schedule();
        }
        Task.run(this::render).timer(1, 1).whileCondition(this::shouldContinue).schedule();
    }
    
    /**
     * Spawns this object and all of its children with a render interval of 1<br>
     *
     * See {@link #spawn(int)} for more info
     */
    @Kapi
    public void spawn() {
        spawn(1);
    }
    
    /**
     * Despawns this object and all of its children
     */
    @Kapi
    public void despawn() {
        this.cancel = true;
        points.forEach(point -> point.getRenderable().despawn(
                world, Vector.fromJOML(
                        getWorldTransform().transformPosition(point.getVector().toVector3f()))
        ));
        children.values().forEach(Object3D::despawn);
        
        triggerEvent(SystemTrigger.DESPAWN_EVENT);
    }
    
    /**
     * Respawns this object and all of its children<br>
     * <br>
     * Equivalent to calling {@link #despawn()} and then {@link #spawn(int)}
     *
     * @param renderInterval The interval in ticks between each render call
     *                       Note: This interval does not affect display entities
     */
    @Kapi
    public void respawn(int renderInterval) {
        despawn();
        spawn(renderInterval);
    }
    
    /**
     * Respawns this object and all of its children with a render interval of 1<br>
     * See {@link #respawn(int)} for more info
     */
    @Kapi
    public void respawn() {
        respawn(1);
    }
    
    /**
     * Gets the visibility of this object<br>
     * Note: this doesn't check if the object is actually visible,
     * for that, use {@link #isVisible()} instead
     *
     * @return The visibility of this object
     */
    @Kapi
    public Visibility getVisibility() {
        return visibility;
    }
    
    @Kapi
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
    
    /**
     * Returns whether this object is visible or not<br>
     * To get the visibility of this object, use {@link #getVisibility()} instead<br>
     * <br>
     * How visibility is determined:
     * Visible: {@link Visibility#VISIBLE} - Always visible
     * Hidden: {@link Visibility#HIDDEN} - Always hidden
     * Inherit: {@link Visibility#INHERIT} - Inherits the visibility of the parent<br>
     * <br>
     * If the visibility is set to inherit and the parent is null,
     * the visibility will default to visible
     *
     * @return true if this object is visible, false otherwise
     */
    @Kapi
    public boolean isVisible() {
        if (visibility == Visibility.INHERIT) {
            if (parent == null) return true;
            return parent.isVisible();
        }
        return visibility == Visibility.VISIBLE;
    }
    
    private void invalidateCachedWorldTransform() {
        cachedWorldTransform = null;
        // Invalidate children cached world transform
        getChildren().forEach(entry -> entry.getValue().invalidateCachedWorldTransform());
    }
    
    /**
     * Sets a component on this object
     *
     * @param key   The name of the component
     * @param value The value of the component
     */
    @Kapi
    @Override
    public void set(String key, @Nullable Object value) {
        components.put(key, value);
    }
    
    /**
     * Gets a component from this object<br>
     * <br>
     * Note: to determine if a returned null value is because the component
     * doesn't exist or because the component is null, use {@link #has(String)}
     *
     * @param key The name of the component
     * @return The value of the component or null if the component doesn't exist
     */
    @Kapi
    @Override
    public @Nullable Object get(String key) {
        return components.getOrDefault(key, null);
    }
    
    /**
     * Checks if this object has a component with the given name
     *
     * @param key The name of the component
     * @return true if this object has the component, false otherwise
     */
    @Kapi
    @Override
    public boolean has(String key) {
        return components.containsKey(key);
    }
    
    /**
     * Removes a component from this object
     *
     * @param key The name of the component
     */
    @Kapi
    @Override
    public void remove(String key) {
        components.remove(key);
    }
    
    /**
     * Adds a system to this object<br>
     * <br>
     * If the system is not an event and the object has already been spawned,
     * the system will be ignored until the object is respawned
     *
     * @param trigger The trigger for the system
     * @param system  The system to add
     * @return this object for chaining
     */
    @Kapi
    @Override
    public Object3D addSystem(
            SystemTrigger trigger, Consumer<Object3D> system
    ) {
        if (trigger.isEvent()) {
            events.computeIfAbsent(trigger.getEvent(), k -> new ArrayList<>()).add(system);
        } else {
            tasks.add(Pair.of(trigger, system));
        }
        return this;
    }
    
    /**
     * Triggers an event on this object<br>
     * This will call all systems that are listening for this event
     *
     * @param event The event to trigger
     */
    @Kapi
    @Override
    public void triggerEvent(String event) {
        List<Consumer<Object3D>> listeners = events.getOrDefault(event, null);
        if (listeners == null) return;
        listeners.forEach(listener -> listener.accept(this));
    }
    
    /**
     * Gets the world of this object
     *
     * @return The world of this object
     */
    @Kapi
    public World getWorld() {
        return world;
    }
}
