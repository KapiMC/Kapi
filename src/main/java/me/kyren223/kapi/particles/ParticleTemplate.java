package me.kyren223.kapi.particles;

import me.kyren223.kapi.math.Transform;
import me.kyren223.kapi.utility.Pair;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ParticleTemplate {
    private final List<Point> points;
    private final HashMap<String, Pair<Transform, ParticleTemplate>> children;
    private final List<Pair<Consumer<ParticleObject>, Integer>> behaviors;
    private Consumer<ParticleObject> onSpawn;
    private Consumer<ParticleObject> onDespawn;
    
    
    public ParticleTemplate(List<Point> points) {
        this.points = points;
        this.children = new HashMap<>();
        this.behaviors = new ArrayList<>();
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
    
    public void addChild(String name, ParticleTemplate child) {
        Transform transform = Transform.fromTranslation(0, 0, 0);
        children.put(name, new Pair<>(transform, child));
    }
    
    public void addChild(String name, ParticleTemplate child, Transform transform) {
        children.put(name, new Pair<>(transform, child));
    }
    
    public ParticleTemplate removeChild(String name) {
        return children.remove(name).second;
    }
    
    public void removeChildIf(Predicate<Map.Entry<String, Pair<Transform, ParticleTemplate>>> predicate) {
        children.entrySet().removeIf(predicate);
    }
    
    public Stream<Map.Entry<String, Pair<Transform, ParticleTemplate>>> getChildren() {
        return children.entrySet().stream();
    }
    
    public Pair<Transform, ParticleTemplate> getChild(String name) {
        return children.get(name);
    }
    
    public void addBehavior(Consumer<ParticleObject> behavior, int interval) {
        behaviors.add(new Pair<>(behavior, interval));
    }
    
    public void onSpawn(Consumer<ParticleObject> onSpawn) {
        this.onSpawn = onSpawn;
    }
    
    public void onDespawn(Consumer<ParticleObject> onDespawn) {
        this.onDespawn = onDespawn;
    }
    
    // Package-private getters for access in ParticleObject
    List<Pair<Consumer<ParticleObject>, Integer>> getBehaviors() {
        return behaviors;
    }
    
    Consumer< ParticleObject> getOnSpawn() {
        return onSpawn;
    }
    
    Consumer<ParticleObject> getOnDespawn() {
        return onDespawn;
    }
    
    
    
    public ParticleObject newInstance(World world, Transform transform, ParticleObject parent) {
        return new ParticleObject(this, world, transform, parent);
    }
    
    public ParticleObject newInstance(World world, Transform transform) {
        return newInstance(world, transform, null);
    }
    
    public ParticleObject newInstance(Location location) {
        return newInstance(location.getWorld(), Transform.fromTranslation(location.toVector()), null);
    }
}
