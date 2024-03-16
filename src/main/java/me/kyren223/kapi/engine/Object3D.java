package me.kyren223.kapi.engine;

import me.kyren223.kapi.engine.ecs.EcsEntity;
import me.kyren223.kapi.engine.ecs.SystemTrigger;
import me.kyren223.kapi.utility.Pair;
import me.kyren223.kapi.utility.Task;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Object3D implements EcsEntity {
    private final World world;
    private final Matrix4f transform;
    private Matrix4fc cachedWorldTransform;
    private final List<Point> points;
    private final @Nullable Object3D parent;
    private final HashMap<String, Object3D> children;
    private final HashMap<String, Object> components;
    private final HashMap<String, List<Consumer<Object3D>>> events;
    private final List<Pair<SystemTrigger, Consumer<Object3D>>> tasks;
    private boolean cancel;
    private Visibility visibility;
    
    /**
     * Creates a new instance of this object
     * @param template The template to use for this object
     * @param world The world to spawn this object in
     * @param transform The transform of this object, cloned to prevent modification
     * @param parent The parent of this object, null if this object has no parent
     */
    public Object3D(Template3D template, World world, Matrix4f transform, @Nullable Object3D parent) {
        this.parent = parent;
        this.world = world;
        this.transform = new Matrix4f(transform);
        this.points = new ArrayList<>();
        template.getPoints().forEach(point ->
                this.points.add(new Point(point))
        );
        
        this.components = new HashMap<>();
        this.events = new HashMap<>(template.getEvents());
        this.tasks = new ArrayList<>(template.getTasks());
        
        this.children = new HashMap<>();
        this.visibility = parent == null ? Visibility.VISIBLE : Visibility.INHERIT;
        template.getChildren().forEach(entry -> {
            Pair<Matrix4f, Template3D> value = entry.getValue();
            Object3D child = value.second.newInstance(world, value.first, this);
            this.children.put(entry.getKey(), child);
        });
    }
    
    /**
     * Creates a new instance of this object's transform
     * <p>
     * If you wish to modify the transform, use the method
     * {@link #transform(Consumer)}
     *
     * @return A read-only interface of this object's transform
     */
    public Matrix4fc getTransform() {
        return transform;
    }
    
    /**
     * Get the transform of this object
     * <p></p>
     * Note: this method invalidates the cached world transform,
     * as it's not possible to know if the transform was modified or not,
     * for more info see {@link #getWorldTransform()}
     * <p></p>
     * If you don't need to modify the transform, use the method
     * {@link #getTransform()} instead
     * <p></p>
     * If you modify the scale, please use the method
     * {@link #transform(Consumer)} instead so the event
     * {@link SystemTrigger#SCALE_CHANGED_EVENT} is triggered<p>
     * This method won't trigger the event, and can be used if you don't
     * want the overhead of checking if the scale was modified
     *
     * @return The mutable transform of this object
     */
    public Matrix4f getMutableTransform() {
        invalidateCachedWorldTransform();
        return transform;
    }
    
    /**
     * Modifies this object's transform
     * <p></p>
     * Note: this method invalidates the cached world transform,
     * for more info see {@link #getWorldTransform()}
     * <p></p>
     * If the scale is modified, the event {@link SystemTrigger#SCALE_CHANGED_EVENT}
     * will be triggered<p>
     * If you don't modify the scale and don't want the overhead of
     * checking if the scale was modified, use the method
     * {@link #getMutableTransform()} instead
     *
     * @param consumer The consumer that modifies the transform
     */
    public void transform(Consumer<Matrix4f> consumer) {
        Vector3f scale = new Vector3f();
        transform.getScale(scale);
        
        consumer.accept(transform);
        invalidateCachedWorldTransform();
        
        Vector3f newScale = new Vector3f();
        transform.getScale(newScale);
        if (!newScale.equals(scale, 0)) {
            triggerEvent(SystemTrigger.SCALE_CHANGED_EVENT);
        }
    }
    
    /**
     * Get the world transform of this object
     * <p></p>
     * Calculates by recursively calling the parent's transform and
     * multiplying it by this object's transform.<p>
     * Until the parent is null where the transform is just a cloned
     * version of this object's transform
     * <p></p>
     * Note: this method is cached, so it's safe to call it multiple times
     *
     * @return A read-only interface of this object's world transform
     */
    public Matrix4fc getWorldTransform() {
        if (cachedWorldTransform != null) return cachedWorldTransform;
        
        if (parent != null) {
            cachedWorldTransform = new Matrix4f(parent.getWorldTransform()).mul(transform);
        } else {
            cachedWorldTransform = new Matrix4f(transform);
        }
        
        return cachedWorldTransform;
    }
    
    public Stream<Point> getPoints() {
        return points.stream();
    }
    
    public void addPoint(Point point) {
        points.add(point);
    }
    
    public void addPoints(List<Point> points) {
        this.points.addAll(points);
    }
    
    public void addPoints(Point... points) {
        this.points.addAll(Arrays.asList(points));
    }
    
    public void removePointIf(Predicate<Point> predicate) {
        points.removeIf(predicate);
    }
    
    public boolean hasParent() {
        return parent != null;
    }
    
    public @Nullable Object3D getParent() {
        return parent;
    }
    
    public void addChild(String name, Template3D child) {
        Object3D object = child.newInstance(world, new Matrix4f(), this);
        children.put(name, object);
    }
    
    public void addChild(String name, Template3D child, Matrix4f transform) {
        Object3D object = child.newInstance(world, transform, this);
        children.put(name, object);
    }
    
    public Object3D removeChild(String name) {
        return children.remove(name);
    }
    
    public void removeChildIf(Predicate<Map.Entry<String, Object3D>> predicate) {
        children.entrySet().removeIf(predicate);
    }
    
    public Stream<Map.Entry<String, Object3D>> getChildren() {
        return children.entrySet().stream();
    }
    
    public Object3D getChild(String name) {
        return children.get(name);
    }
    
    @Nullable
    public String getNameOfChild(Object3D child) {
        for (Map.Entry<String, Object3D> entry : children.entrySet()) {
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
     *                       Note: Display entities are not affected by this interval
     */
    public void spawn(int renderInterval) {
        this.cancel = false;
        
        children.values().forEach(child -> child.spawn(renderInterval));
        
        points.forEach(point -> point.getRenderable().spawn(
                world, Vector.fromJOML(getWorldTransform().transformPosition(point.getVector().toVector3f()))
        ));
        
        triggerEvent(SystemTrigger.SPAWN_EVENT);
        triggerEvent(SystemTrigger.SCALE_CHANGED_EVENT);
        for (Pair<SystemTrigger, Consumer<Object3D>> task : tasks) {
            SystemTrigger trigger = task.first;
            Consumer<Object3D> system = task.second;
            assert !trigger.isEvent();
            Task.runWhile(this::shouldContinue, t -> system.accept(this), trigger.getDelay(), trigger.getPeriod());
        }
        
        Task.runWhile(this::shouldContinue, task -> render(), 1, 1);
    }
    
    /**
     * Spawns this object and all of its children with a render interval of 1
     * <p>
     * See {@link #spawn(int)} for more info
     */
    public void spawn() {
        spawn(1);
    }
    
    /**
     * Despawns this object and all of its children
     */
    public void despawn() {
        this.cancel = true;
        points.forEach(point -> point.getRenderable().despawn(
                world, Vector.fromJOML(getWorldTransform().transformPosition(point.getVector().toVector3f()))
        ));
        children.values().forEach(Object3D::despawn);
        
        triggerEvent(SystemTrigger.DESPAWN_EVENT);
    }
    
    /**
     * Respawns this object and all of its children
     * <p></p>
     * Equivalent to calling {@link #despawn()} and then {@link #spawn(int)}
     * @param renderInterval The interval in ticks between each render call
     *                       Note: Display entities are not affected by this interval
     */
    public void respawn(int renderInterval) {
        despawn();
        spawn(renderInterval);
    }
    
    /**
     * Respawns this object and all of its children with a render interval of 1
     * <p>
     * See {@link #respawn(int)} for more info
     */
    public void respawn() {
        respawn(1);
    }
    
    public Visibility getVisibility() {
        return visibility;
    }
    
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
    
    /**
     * Returns whether this object is visible or not
     * <p></p>
     * How visibility is determined:
     * Visible: {@link Visibility#VISIBLE} - Always visible
     * Hidden: {@link Visibility#HIDDEN} - Always hidden
     * Inherit: {@link Visibility#INHERIT} - Inherits the visibility of the parent
     * <p></p>
     * If the visibility is set to inherit and the parent is null,
     * the visibility will default to visible
     * @return true if this object is visible, false otherwise
     */
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
    
    @Override
    public void set(String key, Object value) {
        components.put(key, value);
    }
    
    @Override
    public Object get(String key) {
        return components.get(key);
    }
    
    @Override
    public boolean has(String key) {
        return components.containsKey(key);
    }
    
    @Override
    public void remove(String key) {
        components.remove(key);
    }
    
    /**
     * Adds a system to this object
     * <p></p>
     * If the system is not an event and the object has already been spawned,
     * the system will be ignored until the object is respawned
     * @param trigger The trigger for the system
     * @param system The system to add
     * @return this object for chaining
     */
    @Override
    public Object3D addSystem(SystemTrigger trigger, Consumer<Object3D> system) {
        if (trigger.isEvent()) {
            events.computeIfAbsent(trigger.getEvent(), k -> new ArrayList<>()).add(system);
        } else {
            tasks.add(new Pair<>(trigger, system));
        }
        return this;
    }
    
    @Override
    public void triggerEvent(String event) {
        List<Consumer<Object3D>> listeners = events.getOrDefault(event, null);
        if (listeners == null) return;
        listeners.forEach(listener -> listener.accept(this));
    }
}
