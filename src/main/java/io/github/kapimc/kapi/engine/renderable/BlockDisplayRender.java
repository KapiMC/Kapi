/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.engine.renderable;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.data.BlockDisplayData;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jspecify.annotations.Nullable;

/**
 * Used to render block displays.
 */
@Kapi
public class BlockDisplayRender extends BlockDisplayData implements Renderable {
    
    private @Nullable BlockDisplay entity;
    
    /**
     * Creates a new block display render.
     *
     * @param data the block display data to use for rendering
     */
    @Kapi
    public BlockDisplayRender(BlockDisplayData data) {
        super(
            data.getTransformation(), data.getInterpolationDuration(), data.getViewRange(),
            data.getShadowRadius(), data.getShadowStrength(), data.getDisplayWidth(),
            data.getDisplayHeight(), data.getInterpolationDelay(), data.getBillboard(),
            data.getGlowColorOverride(), data.getBrightness(), data.getBlock()
        );
    }
    
    @Kapi
    @Override
    public void spawn(World world, Vector point) {
        if (entity != null) {
            throw new IllegalStateException("Cannot spawn a block display that has already been spawned");
        }
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
    
    @Kapi
    @Override
    public void render(World world, Vector point) {
        // No need to render, the entity will automatically render itself
    }
    
    @Kapi
    @Override
    public void despawn(World world, Vector point) {
        if (entity == null) {
            throw new IllegalStateException("Cannot despawn a block display that has not been spawned");
        }
        entity.remove();
    }
    
    @Kapi
    @Override
    public boolean isSpawned() {
        return entity != null;
    }
    
    @Kapi
    @Override
    public void setBlock(BlockData block) {
        super.setBlock(block);
        if (entity != null) entity.setBlock(block);
    }
    
    @Kapi
    @Override
    public void setTransformation(Transformation transformation) {
        super.setTransformation(transformation);
        if (entity != null) entity.setTransformation(transformation);
    }
    
    @Kapi
    @Override
    public void setInterpolationDuration(int duration) {
        super.setInterpolationDuration(duration);
        if (entity != null) entity.setInterpolationDuration(duration);
    }
    
    @Kapi
    @Override
    public void setViewRange(float range) {
        super.setViewRange(range);
        if (entity != null) entity.setViewRange(range);
    }
    
    @Kapi
    @Override
    public void setShadowRadius(float radius) {
        super.setShadowRadius(radius);
        if (entity != null) entity.setShadowRadius(radius);
    }
    
    @Kapi
    @Override
    public void setShadowStrength(float strength) {
        super.setShadowStrength(strength);
        if (entity != null) entity.setShadowStrength(strength);
    }
    
    @Kapi
    @Override
    public void setDisplayWidth(float width) {
        super.setDisplayWidth(width);
        if (entity != null) entity.setDisplayWidth(width);
    }
    
    @Kapi
    @Override
    public void setDisplayHeight(float height) {
        super.setDisplayHeight(height);
        if (entity != null) entity.setDisplayHeight(height);
    }
    
    @Kapi
    @Override
    public void setInterpolationDelay(int ticks) {
        super.setInterpolationDelay(ticks);
        if (entity != null) entity.setInterpolationDelay(ticks);
    }
    
    @Kapi
    @Override
    public void setBillboard(Display.Billboard billboard) {
        super.setBillboard(billboard);
        if (entity != null) entity.setBillboard(billboard);
    }
    
    @Kapi
    @Override
    public void setGlowColorOverride(Color color) {
        super.setGlowColorOverride(color);
        if (entity != null) entity.setGlowColorOverride(color);
    }
    
    @Kapi
    @Override
    public void setBrightness(Display.Brightness brightness) {
        super.setBrightness(brightness);
        if (entity != null) entity.setBrightness(brightness);
    }
    
    @Kapi
    @Override
    public BlockDisplayRender clone() {
        return new BlockDisplayRender(super.clone());
    }
}
