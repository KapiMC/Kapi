package me.kyren223.kapi.render.renderable;

import me.kyren223.kapi.render.ParticleData;
import me.kyren223.kapi.render.Renderable;
import org.bukkit.World;
import org.bukkit.util.Vector;

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
    
    public ParticleData getParticle() {
        return particle;
    }
    
    public void setParticle(ParticleData particle) {
        this.particle = particle;
    }
    
    public ParticleRender(ParticleData particle) {
        this.particle = particle;
    }
}
