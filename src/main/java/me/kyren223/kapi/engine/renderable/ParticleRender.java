package me.kyren223.kapi.engine.renderable;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.engine.data.ParticleData;
import org.bukkit.World;
import org.bukkit.util.Vector;

/**
 * Used for rendering particles.
 */
@Kapi
public class ParticleRender implements Renderable {
    private ParticleData particle;
    
    @Override
    public void spawn(World world, Vector point) {
        // Do nothing, particles are rendered instantly
    }
    
    @Override
    public void render(World world, Vector point) {
        world.spawnParticle(
                particle.getParticle(),
                point.getX(), point.getY(), point.getZ(),
                particle.getCount(),
                particle.getSpreadX(), particle.getSpreadY(), particle.getSpreadZ(),
                particle.getExtra(),
                particle.getData(),
                particle.isForce()
        );
    }
    
    @Override
    public void despawn(World world, Vector point) {
        // Do nothing, particles are automatically despawned after some time
    }
    
    @Kapi
    public ParticleData getParticle() {
        return particle;
    }
    
    @Kapi
    public void setParticle(ParticleData particle) {
        this.particle = particle;
    }
    
    @Kapi
    public ParticleRender(ParticleData particle) {
        this.particle = particle;
    }
    
    @Override
    @Kapi
    public Renderable clone() {
        return new ParticleRender(new ParticleData(particle));
    }
}
