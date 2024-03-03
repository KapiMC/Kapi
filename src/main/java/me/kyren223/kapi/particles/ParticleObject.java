package me.kyren223.kapi.particles;

import me.kyren223.kapi.utility.Pair;
import me.kyren223.kapi.utility.Task;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ParticleObject {
    private final Transform transform;
    private final List<Point> points;
    private @Nullable ParticleObject parent;
    private final HashMap<String, ParticleObject> children;
    private final List<Pair<Consumer<ParticleObject>, Integer>> behaviors;
    private int ticks;
    private boolean force;
    private boolean cancel;
    private int renderInterval;
    private Visibility visibility;
    
    public ParticleObject(ParticleTemplate template, Transform transform, @Nullable ParticleObject parent) {
        this.parent = parent;
        this.transform = transform;
        this.points = new ArrayList<>(template.getPoints().toList());
        this.behaviors = new ArrayList<>(template.getBehaviors());
        this.children = new HashMap<>();
        this.visibility = parent == null ? Visibility.VISIBLE : Visibility.INHERIT;
        template.getChildren().forEach(entry -> {
            Pair<Transform, ParticleTemplate> value = entry.getValue();
            ParticleObject child = value.second.newInstance(value.first, this);
            children.put(entry.getKey(), child);
        });
    }
    
    public Transform getTransform() {
        return transform;
    }
    
    public Transform getAbsoluteTransform() {
        Transform absolute = new Transform(transform);
        if (parent != null) {
            absolute = parent.getAbsoluteTransform().combine(absolute);
        }
        return absolute;
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
        ParticleObject object = new ParticleObject(child, transform, this);
        object.parent = this;
        children.put(name, object);
    }
    
    public void addChild(String name, ParticleTemplate child, Transform transform) {
        ParticleObject object = new ParticleObject(child, transform, this);
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
        Transform absoluteTransform = getAbsoluteTransform();
        getPoints().forEach(point -> {
            ParticleData particle = point.getParticle();
            double[] xyz = absoluteTransform.applyToPoint(point);
            double x = xyz[0];
            double y = xyz[1];
            double z = xyz[2];
            
            point.getWorld().spawnParticle(
                particle.getParticle(),
                x, y, z,
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
    
    private boolean shouldCancel() {
        return cancel;
    }
    
    public void spawn(boolean force, int renderInterval) {
        this.ticks = 0;
        this.force = false;
        this.renderInterval = renderInterval;
        this.cancel = false;
        Task.runWhile(this::shouldCancel, task -> {
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
}
