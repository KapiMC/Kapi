package me.kyren223.kapi.engine.ecs;

import me.kyren223.kapi.engine.Object3D;

import java.util.function.Consumer;

public interface EcsEntity {
    
    // Component management
    void set(String key, Object value);
    Object get(String key);
    boolean has(String key);
    void remove(String key);
    
    // Utility
    default void setIfAbsent(String key, Object value) {
        if (!has(key)) {
            set(key, value);
        }
    }
    
    // System management
    Object3D addSystem(SystemTrigger trigger, Consumer<Object3D> system);
    void triggerEvent(String event);
}
