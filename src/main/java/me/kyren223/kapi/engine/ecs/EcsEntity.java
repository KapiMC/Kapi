package me.kyren223.kapi.engine.ecs;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.engine.Object3D;

import java.util.function.Consumer;

/**
 * This interface represents an entity in an ECS system.<br>
 * Implementing this interface yourself is not recommended nor supported.
 */
@Kapi
public interface EcsEntity {
    
    // Component management
    @Kapi void set(String key, Object value);
    @Kapi Object get(String key);
    @Kapi boolean has(String key);
    @Kapi  void remove(String key);
    
    // Utility
    @Kapi default void setIfAbsent(String key, Object value) {
        if (!has(key)) {
            set(key, value);
        }
    }
    
    // System management
    @Kapi Object3D addSystem(SystemTrigger trigger, Consumer<Object3D> system);
    @Kapi void triggerEvent(String event);
}
