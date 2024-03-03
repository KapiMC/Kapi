package me.kyren223.kapi.particles;

import me.kyren223.kapi.utility.Pair;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ParticleObject {
    private final Transform transform;
    private final List<Point> points;
    private @Nullable ParticleObject parent;
    private final HashMap<String, ParticleObject> children;
    
    public ParticleObject(ParticleTemplate template, Transform transform) {
        this.transform = transform;
        this.points = new ArrayList<>(template.getPoints().toList());
        this.children = new HashMap<>();
        template.getChildren().forEach(entry -> {
            Pair<Transform, ParticleTemplate> value = entry.getValue();
            ParticleObject child = value.second.newInstance(value.first);
            child.parent = this;
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
        ParticleObject object = new ParticleObject(child, transform);
        object.parent = this;
        children.put(name, object);
    }
    
    public void addChild(String name, ParticleTemplate child, Transform transform) {
        ParticleObject object = new ParticleObject(child, transform);
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
    
}
