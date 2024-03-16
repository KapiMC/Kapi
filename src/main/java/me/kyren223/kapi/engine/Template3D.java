package me.kyren223.kapi.engine;

import me.kyren223.kapi.engine.ecs.SystemTrigger;
import me.kyren223.kapi.utility.Pair;
import org.bukkit.Location;
import org.bukkit.World;
import org.joml.Matrix4f;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Template3D {
    private final List<Point> points;
    private final HashMap<String, Pair<Matrix4f, Template3D>> children;
    private final HashMap<String, Object> components;
    private final HashMap<String, List<Consumer<Object3D>>> events;
    private final List<Pair<SystemTrigger, Consumer<Object3D>>> tasks;
    
    
    public Template3D(List<Point> points) {
        this.points = points;
        this.children = new HashMap<>();
        this.components = new HashMap<>();
        this.events = new HashMap<>();
        this.tasks = new ArrayList<>();
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
    
    public void addChild(String name, Template3D child) {
        children.put(name, new Pair<>(new Matrix4f(), child));
    }
    
    public void addChild(String name, Template3D child, Matrix4f transform) {
        children.put(name, new Pair<>(transform, child));
    }
    
    public Template3D removeChild(String name) {
        return children.remove(name).second;
    }
    
    public void removeChildIf(Predicate<Map.Entry<String, Pair<Matrix4f, Template3D>>> predicate) {
        children.entrySet().removeIf(predicate);
    }
    
    public Stream<Map.Entry<String, Pair<Matrix4f, Template3D>>> getChildren() {
        return children.entrySet().stream();
    }
    
    public Pair<Matrix4f, Template3D> getChild(String name) {
        return children.get(name);
    }
    
    public Object3D newInstance(World world, Matrix4f transform, Object3D parent) {
        return new Object3D(this, world, transform, parent);
    }
    
    public void setDefault(String key, Object value) {
        components.put(key, value);
    }
    
    public Template3D addSystem(SystemTrigger trigger, Consumer<Object3D> system) {
        if (trigger.isEvent()) {
            events.computeIfAbsent(trigger.getEvent(), k -> new ArrayList<>()).add(system);
        } else {
            tasks.add(new Pair<>(trigger, system));
        }
        return this;
    }
    
    // Package-private
    HashMap<String, List<Consumer<Object3D>>> getEvents() {
        Consumer<Object3D> setDefaultsSystem = instance -> components.forEach(instance::set);
        events.computeIfAbsent(SystemTrigger.SPAWN_EVENT, k -> new ArrayList<>()).add(setDefaultsSystem);
        return events;
    }
    
    List<Pair<SystemTrigger, Consumer<Object3D>>> getTasks() {
        return tasks;
    }
    
    public Object3D newInstance(Location location, Object3D parent) {
        return new Object3D(
                this,
                location.getWorld(),
                new Matrix4f().translate(location.toVector().toVector3f()),
                parent
        );
    }
    
    public Object3D newInstance(Location location) {
        return new Object3D(
                this,
                location.getWorld(),
                new Matrix4f().translate(location.toVector().toVector3f()),
                null
        );
    }
}
