/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.engine.ecs;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.engine.Object3D;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

/**
 * This interface represents an entity in an ECS system.<br>
 * Implementing this interface yourself is not recommended nor supported.
 */
@Kapi
public interface EcsEntity {
    
    @Kapi
    void set(String key, @Nullable Object value);
    
    @Kapi
    @Nullable
    Object get(String key);
    
    @Kapi
    boolean has(String key);
    
    @Kapi
    void remove(String key);
    
    @Kapi
    default void setIfAbsent(String key, Object value) {
        if (!has(key)) {
            set(key, value);
        }
    }
    
    @Kapi
    Object3D addSystem(SystemTrigger trigger, Consumer<Object3D> system);
    
    @Kapi
    void triggerEvent(String event);
}
