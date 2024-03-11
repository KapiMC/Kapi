package me.kyren223.kapi.render;

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

public class Template3D {
    private final List<Point> points;
    private final HashMap<String, Pair<Transform, Template3D>> children;
    private final List<Pair<Consumer<Object3D>, Integer>> behaviors;
    private Consumer<Object3D> onSpawn;
    private Consumer<Object3D> onDespawn;
    
    
    public Template3D(List<Point> points) {
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
    
    public void addChild(String name, Template3D child) {
        Transform transform = Transform.fromTranslation(0, 0, 0);
        children.put(name, new Pair<>(transform, child));
    }
    
    public void addChild(String name, Template3D child, Transform transform) {
        children.put(name, new Pair<>(transform, child));
    }
    
    public Template3D removeChild(String name) {
        return children.remove(name).second;
    }
    
    public void removeChildIf(Predicate<Map.Entry<String, Pair<Transform, Template3D>>> predicate) {
        children.entrySet().removeIf(predicate);
    }
    
    public Stream<Map.Entry<String, Pair<Transform, Template3D>>> getChildren() {
        return children.entrySet().stream();
    }
    
    public Pair<Transform, Template3D> getChild(String name) {
        return children.get(name);
    }
    
    public void addBehavior(Consumer<Object3D> behavior, int interval) {
        behaviors.add(new Pair<>(behavior, interval));
    }
    
    public void onSpawn(Consumer<Object3D> onSpawn) {
        this.onSpawn = onSpawn;
    }
    
    public void onDespawn(Consumer<Object3D> onDespawn) {
        this.onDespawn = onDespawn;
    }
    
    // Package-private getters for access in ParticleObject
    List<Pair<Consumer<Object3D>, Integer>> getBehaviors() {
        return behaviors;
    }
    
    Consumer<Object3D> getOnSpawn() {
        return onSpawn;
    }
    
    Consumer<Object3D> getOnDespawn() {
        return onDespawn;
    }
    
    public Object3D newInstance(World world, Transform transform, Object3D parent) {
        return new Object3D(this, world, transform, parent);
    }
    
    public Object3D newInstance(Location location) {
        return new Object3D(
                this,
                location.getWorld(),
                Transform.fromTranslation(location.toVector()),
                null
        );
    }
}
