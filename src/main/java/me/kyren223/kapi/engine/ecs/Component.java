package me.kyren223.kapi.engine.ecs;

import me.kyren223.kapi.annotations.Kapi;

/**
 * A class that contains all the names of the built-in components.<br>
 * Makes it easier to reference components in the code,
 * and reduces the chance of typos from using strings directly.
 */
@Kapi
public class Component {
    
    private Component() {
        throw new AssertionError("Component should not be instantiated");
    }
    
    @Kapi public static final String VELOCITY = "velocity";
    @Kapi public static final String ACCELERATION = "acceleration";
}
