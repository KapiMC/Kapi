/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.engine.renderable;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.data.ItemDisplayData;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jspecify.annotations.Nullable;

/**
 * Used to render item displays.
 */
@Kapi
public final class ItemDisplayRender extends ItemDisplayData implements Renderable {
    
    private @Nullable ItemDisplay entity;
    
    /**
     * @param data the item display data to use for rendering
     */
    @Kapi
    public ItemDisplayRender(ItemDisplayData data) {
        super(
            data.getTransformation(), data.getInterpolationDuration(), data.getViewRange(),
            data.getShadowRadius(), data.getShadowStrength(), data.getDisplayWidth(),
            data.getDisplayHeight(), data.getInterpolationDelay(), data.getBillboard(),
            data.getGlowColorOverride(), data.getBrightness(), data.getItemStack(),
            data.getItemDisplayTransform()
        );
    }
    
    @Kapi
    @Override
    public void spawn(World world, Vector point) {
        if (entity != null) {
            throw new IllegalStateException("Cannot spawn an item display that has already been spawned");
        }
        entity = world.spawn(point.toLocation(world), ItemDisplay.class);
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
        entity.setItemStack(getItemStack());
        entity.setItemDisplayTransform(getItemDisplayTransform());
    }
    
    @Kapi
    @Override
    public void render(World world, Vector point) {
        if (entity == null) {
            throw new IllegalStateException("Cannot render an item display that has not been spawned");
        }
        entity.teleport(point.toLocation(world));
    }
    
    @Kapi
    @Override
    public void despawn(World world, Vector point) {
        if (entity == null) {
            throw new IllegalStateException("Cannot despawn an item display that has not been spawned");
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
    public void setItemStack(@Nullable ItemStack item) {
        super.setItemStack(item);
        if (entity != null) entity.setItemStack(item);
    }
    
    @Kapi
    @Override
    public void setItemDisplayTransform(ItemDisplay.ItemDisplayTransform display) {
        super.setItemDisplayTransform(display);
        if (entity != null) entity.setItemDisplayTransform(display);
    }
    
    @Kapi
    @Override
    public ItemDisplayRender clone() {
        return new ItemDisplayRender(super.clone());
    }
    
}
