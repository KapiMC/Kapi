package me.kyren223.kapi.engine.ecs;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.engine.Object3D;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.function.Consumer;

/**
 * A collection of built-in systems that can be applied to an {@link Object3D}.
 */
@Kapi
public class System {
    
    private System() {
        throw new AssertionError("System should not be instantiated");
    }
    
    /**
     * Moves the object by its velocity.<br>
     * <br>
     * The object must have a {@value Component#VELOCITY} component.
     * The type of the velocity can be
     * either {@link Vector3fc}, {@link Vector3f} or {@link Vector}.<br>
     * <br>
     * If the component is not present or is not of the correct type,
     * this system will silently do nothing.
     */
    @Kapi
    public static final Consumer<Object3D> VELOCITY = instance -> {
        if (!instance.has(Component.VELOCITY)) return;
        Object object = instance.get(Component.VELOCITY);
        if (object instanceof Vector3fc velocity) {
            instance.getMutableTransform().translate(velocity);
        } else if (object instanceof Vector velocity) {
            instance.getMutableTransform().translate(velocity.toVector3f());
        }
        // else do nothing
    };
    
    /**
     * Modifies the object's velocity by its acceleration.<br>
     * <br>
     * The object must have a {@value Component#VELOCITY} component
     * and an {@value Component#ACCELERATION} component.<br>
     *
     * The type of both the velocity and acceleration can be
     * either {@link Vector3fc}, {@link Vector3f} or {@link Vector}.<br>
     * <br>
     * If either the velocity or acceleration component is not present
     * or are not of the correct type, this system will silently do nothing.
     */
    @Kapi
    public static final Consumer<Object3D> ACCELERATION = instance -> {
        if (!instance.has(Component.ACCELERATION)) return;
        if (!instance.has(Component.VELOCITY)) return;
        
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
