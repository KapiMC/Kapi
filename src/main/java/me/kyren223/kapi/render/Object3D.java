package me.kyren223.kapi.render;

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

public class Object3D {
    private final World world;
    private final Matrix4f transform;
    private Matrix4fc cachedWorldTransform;
    private final List<Point> points;
    private final @Nullable Object3D parent;
    private final HashMap<String, Object3D> children;
    private final List<Pair<Consumer<Object3D>, Integer>> behaviors;
    private final @Nullable Consumer<Object3D> onSpawn;
    private final @Nullable Consumer<Object3D> onDespawn;
    private int ticks;
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
        this.points = new ArrayList<>(template.getPoints().toList());
        this.behaviors = new ArrayList<>(template.getBehaviors());
        this.onSpawn = template.getOnSpawn();
        this.onDespawn = template.getOnDespawn();
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
     *
     * @return The mutable transform of this object
     */
    public Matrix4f getMutableTransform() {
        invalidateCachedWorldTransform();
        return transform;
    }
    
    /**
     * Modifies this object's transform
     * <p>
     * Note: this method invalidates the cached world transform,
     * for more info see {@link #getWorldTransform()}
     *
     * @param consumer The consumer that modifies the transform
     */
    public void transform(Consumer<Matrix4f> consumer) {
        consumer.accept(transform);
        invalidateCachedWorldTransform();
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
    
    public void addBehavior(Consumer<Object3D> behavior, int interval) {
        behaviors.add(new Pair<>(behavior, interval));
    }
    
    private void tick() {
        behaviors.forEach(pair -> {
            if (ticks % pair.second == 0) {
                pair.first.accept(this);
            }
        });
        children.values().forEach(Object3D::tick);
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
        this.ticks = 0;
        this.cancel = false;
        if (onSpawn != null) this.onSpawn.accept(this);
        Task.runWhile(this::shouldContinue, task -> {
            tick();
            if (isVisible()) render();
            ticks++;
        }, 1, 1);
        children.values().forEach(child -> child.spawn(renderInterval));
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
        children.values().forEach(Object3D::despawn);
        if (onDespawn != null) this.onDespawn.accept(this);
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
    }
}
