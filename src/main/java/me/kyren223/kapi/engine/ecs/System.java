package me.kyren223.kapi.engine.ecs;

import me.kyren223.kapi.engine.Object3D;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.function.Consumer;

public class System {
    
    private System() {
        // Prevent instantiation
    }
    
    public static final Consumer<Object3D> VELOCITY = instance -> {
        Object object = instance.get(Component.VELOCITY);
        if (object instanceof Vector3fc velocity) {
            instance.getMutableTransform().translate(velocity);
        } else if (object instanceof Vector velocity) {
            instance.getMutableTransform().translate(velocity.toVector3f());
        }
    };
    
    public static final Consumer<Object3D> ACCELERATION = instance -> {
        Object object = instance.get(Component.ACCELERATION);
        Vector3fc acceleration;
        Vector3f velocity;
        if (object instanceof Vector3fc acc) {
            acceleration = acc;
        } else if (object instanceof Vector acc) {
            acceleration = acc.toVector3f();
        } else return;
        
        object = instance.get(Component.VELOCITY);
        if (object instanceof Vector3fc vel) {
            velocity = new Vector3f(vel);
        } else if (object instanceof Vector vel) {
            velocity = vel.toVector3f();
        } else return;
        
        instance.set(Component.VELOCITY, velocity.add(acceleration));
    };
}
