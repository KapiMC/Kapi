package me.kyren223.kapi.engine.ecs;

import me.kyren223.kapi.engine.Object3D;
import org.joml.Vector3fc;

import java.util.function.Consumer;

public class System {
    
    private System() {
        // Prevent instantiation
    }
    
    public Consumer<Object3D> velocity() {
        return instance -> {
            Object object = instance.get(Component.VELOCITY);
            if (!(object instanceof Vector3fc velocity)) return;
            instance.getMutableTransform().translate(velocity);
        };
    }
}
