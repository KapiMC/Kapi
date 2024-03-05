package me.kyren223.kapi.particles;

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

public class ParticleObject {
    private final World world;
    private final Transform transform;
    private Transform cachedWorldTransform;
    private final List<Point> points;
    private @Nullable ParticleObject parent;
    private final HashMap<String, ParticleObject> children;
    private final List<Pair<Consumer<ParticleObject>, Integer>> behaviors;
    private final @Nullable Consumer<ParticleObject> onSpawn;
    private final @Nullable Consumer<ParticleObject> onDespawn;
    private int ticks;
    private boolean force;
    private boolean cancel;
    private int renderInterval;
    private Visibility visibility;
    
    public ParticleObject(ParticleTemplate template, World world, Transform transform, @Nullable ParticleObject parent) {this.parent = parent;
        this.world = world;
        this.transform = transform;
        this.points = new ArrayList<>(template.getPoints().toList());
        this.behaviors = new ArrayList<>(template.getBehaviors());
        this.onSpawn = template.getOnSpawn();
        this.onDespawn = template.getOnDespawn();
        this.children = new HashMap<>();
        this.visibility = parent == null ? Visibility.VISIBLE : Visibility.INHERIT;
        template.getChildren().forEach(entry -> {
            Pair<Transform, ParticleTemplate> value = entry.getValue();
            ParticleObject child = value.second.newInstance(world, value.first, this);
            child.parent = this;
            children.put(entry.getKey(), child);
        });
    }
    
    /**
     * Creates a new instance of this object's transform
     * <p>
     * If you wish to modify the transform, use the method
     * {@link #transform(Consumer)}
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
    
    public @Nullable ParticleObject getParent() {
        return parent;
    }
    
    public void addChild(String name, ParticleTemplate child) {
        Transform transform = Transform.fromTranslation(0, 0, 0);
        ParticleObject object = new ParticleObject(child, world, transform, this);
        object.parent = this;
        children.put(name, object);
    }
    
    public void addChild(String name, ParticleTemplate child, Transform transform) {
        ParticleObject object = new ParticleObject(child, world, transform, this);
        object.parent = this;
        children.put(name, object);
    }
    
    public ParticleObject removeChild(String name) {
        return children.remove(name);
    }
    
    public void removeChildIf(Predicate<Map.Entry<String, ParticleObject>> predicate) {
        children.entrySet().removeIf(predicate);
    }
    
    public Stream<Map.Entry<String, ParticleObject>> getChildren() {
        return children.entrySet().stream();
    }
    
    public ParticleObject getChild(String name) {
        return children.get(name);
    }
    
    public String getNameOfChild(ParticleObject child) {
        for (Map.Entry<String, ParticleObject> entry : children.entrySet()) {
            if (entry.getValue() == child) return entry.getKey();
        }
        return null;
    }
    
    public boolean isForce() {
        return force;
    }
    
    public void setForce(boolean force) {
        this.force = force;
    }
    
    public void addBehavior(Consumer<ParticleObject> behavior, int interval) {
        behaviors.add(new Pair<>(behavior, interval));
    }
    
    private void tick() {
        behaviors.forEach(pair -> {
            if (ticks % pair.second == 0) {
                pair.first.accept(this);
            }
        });
        children.values().forEach(ParticleObject::tick);
    }
    
    private void render() {
        // Render this
        Transform absoluteTransform = getWorldTransform();
        getPoints().forEach(point -> {
            ParticleData particle = point.getParticle();
            Vector absolutePosition = absoluteTransform.transformPoint(point.getVector());
            
            this.world.spawnParticle(
                particle.getParticle(),
                absolutePosition.getX(), absolutePosition.getY(), absolutePosition.getZ(),
                particle.getCount(),
                particle.getSpreadX(), particle.getSpreadY(), particle.getSpreadZ(),
                particle.getExtra(),
                particle.getData(),
                force
            );
        });
        
        // Render children
        children.values().forEach(ParticleObject::render);
    }
    
    private boolean shouldContinue() {
        return !cancel;
    }
    
    public void spawn(boolean force, int renderInterval) {
        this.ticks = 0;
        this.force = false;
        this.renderInterval = renderInterval;
        this.cancel = false;
        if (onSpawn != null) this.onSpawn.accept(this);
        Task.runWhile(this::shouldContinue, task -> {
            tick();
            if (isVisible()) render();
            ticks++;
        }, 1, 1);
        children.values().forEach(child -> child.spawn(force, renderInterval));
    }
    
    public void spawn(boolean force) {
        spawn(force, 1);
    }
    
    public void spawn(int renderInterval) {
        spawn(false, renderInterval);
    }
    
    public void spawn() {
        spawn(false);
    }
    
    public void despawn() {
        this.cancel = true;
        children.values().forEach(ParticleObject::despawn);
        if (onDespawn != null) this.onDespawn.accept(this);
    }
    
    public void reset(boolean force, int renderInterval) {
        despawn();
        spawn(force, renderInterval);
    }
    
    public void reset(boolean force) {
        reset(force, renderInterval);
    }
    
    public void reset(int renderInterval) {
        reset(false, renderInterval);
    }
    
    public void reset() {
        reset(false);
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
