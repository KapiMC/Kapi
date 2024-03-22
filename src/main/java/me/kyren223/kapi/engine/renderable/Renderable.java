package me.kyren223.kapi.engine.renderable;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.engine.Object3D;
import org.bukkit.World;
import org.bukkit.util.Vector;

/**
 * Represents an object that can be rendered by a {@link Object3D}.
 */
@Kapi
public interface Renderable {
    @Kapi void spawn(World world, Vector point);
    @Kapi void render(World world, Vector point);
    @Kapi void despawn(World world, Vector point);
    @Kapi Renderable clone();
}
