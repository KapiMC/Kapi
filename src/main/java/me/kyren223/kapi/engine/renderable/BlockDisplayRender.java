package me.kyren223.kapi.engine.renderable;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.data.BlockDisplayData;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * Used to render block displays.
 */
@Kapi
public class BlockDisplayRender extends BlockDisplayData implements Renderable {
    
    private BlockDisplay entity;
    
    @Kapi
    public BlockDisplayRender(
            @NotNull Transformation transformation,
            int interpolationDuration,
            float viewRange,
            float shadowRadius,
            float shadowStrength,
            float displayWidth,
            float displayHeight,
            int interpolationDelay,
            @NotNull Display.Billboard billboard,
            @NotNull Color glowColorOverride,
            @NotNull Display.Brightness brightness,
            @NotNull BlockData block
    ) {
        super(transformation, interpolationDuration, viewRange, shadowRadius, shadowStrength, displayWidth, displayHeight, interpolationDelay, billboard, glowColorOverride, brightness, block);
    }
    
    /**
     * Copy constructor
     * @param data The data to copy
     */
    @Kapi
    public BlockDisplayRender(@NotNull BlockDisplayData data) {
        super(data);
    }
    
    @Override
    public void spawn(World world, Vector point) {
        entity = world.spawn(point.toLocation(world), BlockDisplay.class);
        entity.setTransformation(getTransformation());
        entity.setInterpolationDuration(getInterpolationDuration());
        entity.setViewRange(getViewRange());
        entity.setShadowRadius(getShadowRadius());
        entity.setShadowStrength(getShadowStrength());
        entity.setDisplayWidth(getDisplayWidth());
        entity.setDisplayHeight(getDisplayHeight());
        entity.setInterpolationDelay(getInterpolationDelay());
        entity.setBillboard(getBillboard());
        entity.setGlowColorOverride(getGlowColorOverride());
        entity.setBrightness(getBrightness());
        entity.setBlock(getBlock());
    }
    
    @Override
    public void render(World world, Vector point) {
        // No need to render, the entity will automatically render itself
    }
    
    @Override
    public void despawn(World world, Vector point) {
        entity.remove();
    }
    
    @Kapi
    @Override
    public void setBlock(@NotNull BlockData block) {
        super.setBlock(block);
        entity.setBlock(block);
    }
    
    @Kapi
    
    @Override
    public void setTransformation(@NotNull Transformation transformation) {
        super.setTransformation(transformation);
        entity.setTransformation(transformation);
    }
    
    @Kapi
    @Override
    public void setInterpolationDuration(int duration) {
        super.setInterpolationDuration(duration);
        entity.setInterpolationDuration(duration);
    }
    
    @Kapi
    @Override
    public void setViewRange(float range) {
        super.setViewRange(range);
        entity.setViewRange(range);
    }
    
    @Kapi
    @Override
    public void setShadowRadius(float radius) {
        super.setShadowRadius(radius);
        entity.setShadowRadius(radius);
    }
    
    @Kapi
    @Override
    public void setShadowStrength(float strength) {
        super.setShadowStrength(strength);
        entity.setShadowStrength(strength);
    }
    
    @Kapi
    @Override
    public void setDisplayWidth(float width) {
        super.setDisplayWidth(width);
        entity.setDisplayWidth(width);
    }
    
    @Kapi
    @Override
    public void setDisplayHeight(float height) {
        super.setDisplayHeight(height);
        entity.setDisplayHeight(height);
    }
    
    @Kapi
    @Override
    public void setInterpolationDelay(int ticks) {
        super.setInterpolationDelay(ticks);
        entity.setInterpolationDelay(ticks);
    }
    
    @Kapi
    @Override
    public void setBillboard(Display.@NotNull Billboard billboard) {
        super.setBillboard(billboard);
        entity.setBillboard(billboard);
    }
    
    @Kapi
    @Override
    public void setGlowColorOverride(@NotNull Color color) {
        super.setGlowColorOverride(color);
        entity.setGlowColorOverride(color);
    }
    
    @Kapi
    @Override
    public void setBrightness(Display.@NotNull Brightness brightness) {
        super.setBrightness(brightness);
        entity.setBrightness(brightness);
    }
    
    @Kapi
    @Override
    public Renderable clone() {
        return new BlockDisplayRender(this);
    }
}
