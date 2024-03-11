package me.kyren223.kapi.render;

import me.kyren223.kapi.math.Transform;
import me.kyren223.kapi.utility.Pair;
import me.kyren223.kapi.utility.Task;
import org.bukkit.World;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Object3D {
    private final World world;
    private final Transform transform;
    private Transform cachedWorldTransform;
    private final List<Point> points;
    private final @Nullable Object3D parent;
    private final HashMap<String, Object3D> children;
    private final List<Pair<Consumer<Object3D>, Integer>> behaviors;
    private final @Nullable Consumer<Object3D> onSpawn;
    private final @Nullable Consumer<Object3D> onDespawn;
    private int ticks;
    private boolean cancel;
    private Visibility visibility;
    
    public Object3D(Template3D template, World world, Transform transform, @Nullable Object3D parent) {
        this.parent = parent;
        this.world = world;
        this.transform = transform;
        this.points = new ArrayList<>(template.getPoints().toList());
        this.behaviors = new ArrayList<>(template.getBehaviors());
        this.onSpawn = template.getOnSpawn();
        this.onDespawn = template.getOnDespawn();
        this.children = new HashMap<>();
        this.visibility = parent == null ? Visibility.VISIBLE : Visibility.INHERIT;
        template.getChildren().forEach(entry -> {
            Pair<Transform, Template3D> value = entry.getValue();
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
     * @return A cloned version of this object's transform
     */
    public Transform getTransform() {
        return transform.clone();
    }
    
    /**
     * Modifies this object's transform
     * <p>
     * Note: this method invalidates the cached world transform,
     * for more info see {@link #getWorldTransform()}
     *
     * @param consumer The consumer that modifies the transform
     */
    public void transform(Consumer<Transform> consumer) {
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
     * @return A cloned world transform
     */
    public Transform getWorldTransform() {
        if (cachedWorldTransform != null) return cachedWorldTransform.clone();
        
        Transform worldTransform;
        if (parent != null) {
            // No need to clone the parent's world transform, as it's already cloned
            // By the signature and Java docs of getWorldTransform
            worldTransform = parent.getWorldTransform().multiply(transform);
        } else {
            worldTransform = transform.clone();
        }
        
        cachedWorldTransform = worldTransform;
        return worldTransform;
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
        Transform childTransform = Transform.fromTranslation(0, 0, 0);
        Object3D object = child.newInstance(world, childTransform, this);
        children.put(name, object);
    }
    
    public void addChild(String name, Template3D child, Transform transform) {
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
        // Render this
        Transform absoluteTransform = getWorldTransform();
        getPoints().forEach(point -> {
            Vector position = absoluteTransform.transformPoint(point.getVector());
            point.getRenderable().render(world, position);
        });
        
        // Render children
        children.values().forEach(Object3D::render);
    }
    
    private boolean shouldContinue() {
        return !cancel;
    }
    
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
    
    public void spawn() {
        spawn(1);
    }
    
    public void despawn() {
        this.cancel = true;
        children.values().forEach(Object3D::despawn);
        if (onDespawn != null) this.onDespawn.accept(this);
    }
    
    public void reset(int renderInterval) {
        despawn();
        spawn(renderInterval);
    }
    
    public void reset() {
        reset(1);
    }
    
    public Visibility getVisibility() {
        return visibility;
    }
    
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
    
    public boolean isVisible() {
        if (visibility == Visibility.HIDDEN) return false;
        if (visibility == Visibility.VISIBLE) return true;
        if (parent != null) return parent.isVisible();
        return true;
    }
    
    private void invalidateCachedWorldTransform() {
        cachedWorldTransform = null;
    }
}
