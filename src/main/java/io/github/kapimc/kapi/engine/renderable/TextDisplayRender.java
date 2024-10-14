/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.engine.renderable;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.data.TextDisplayData;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jspecify.annotations.Nullable;

/**
 * Used to render text displays.
 */
@Kapi
public final class TextDisplayRender extends TextDisplayData implements Renderable {
    
    private @Nullable TextDisplay entity;
    
    /**
     * Creates a new text display render.
     *
     * @param data the text display data to use for rendering
     */
    @Kapi
    public TextDisplayRender(TextDisplayData data) {
        super(
            data.getTransformation(), data.getInterpolationDuration(), data.getViewRange(),
            data.getShadowRadius(), data.getShadowStrength(), data.getDisplayWidth(),
            data.getDisplayHeight(), data.getInterpolationDelay(), data.getBillboard(),
            data.getGlowColorOverride(), data.getBrightness(), data.getText(),
            data.getLineWidth(), data.getBackgroundColor(), data.getTextOpacity(),
            data.isShadowed(), data.isSeeThrough(), data.isDefaultBackground(),
            data.getAlignment()
        );
    }
    
    @Kapi
    @SuppressWarnings("deprecation")
    @Override
    public void spawn(World world, Vector point) {
        if (entity != null) {
            throw new IllegalStateException("Cannot spawn a text display that has already been spawned");
        }
        entity = world.spawn(point.toLocation(world), TextDisplay.class);
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
        entity.setText(getText());
        entity.setLineWidth(getLineWidth());
        entity.setBackgroundColor(getBackgroundColor());
        entity.setTextOpacity(getTextOpacity());
        entity.setShadowed(isShadowed());
        entity.setSeeThrough(isSeeThrough());
        entity.setDefaultBackground(isDefaultBackground());
        entity.setAlignment(getAlignment());
    }
    
    @Kapi
    @Override
    public void render(World world, Vector point) {
        if (entity == null) {
            throw new IllegalStateException("Cannot render a text display that has not been spawned");
        }
        entity.teleport(point.toLocation(world));
    }
    
    @Kapi
    @Override
    public void despawn(World world, Vector point) {
        if (entity == null) {
            throw new IllegalStateException("Cannot despawn a text display that has not been spawned");
        }
        entity.remove();
    }
    
    @Kapi
    @Override
    public boolean isSpawned() {
        return entity != null;
    }
    
    @Override
    @Kapi
    public void setTransformation(Transformation transformation) {
        super.setTransformation(transformation);
        if (entity != null) entity.setTransformation(transformation);
    }
    
    @Override
    @Kapi
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
    public void setText(@Nullable String text) {
        super.setText(text);
        if (entity != null) entity.setText(text);
    }
    
    @Override
    @Kapi
    public void setLineWidth(int width) {
        super.setLineWidth(width);
        if (entity != null) entity.setLineWidth(width);
    }
    
    @Kapi
    @Override
    @SuppressWarnings("deprecation")
    public void setBackgroundColor(@Nullable Color color) {
        super.setBackgroundColor(color);
        if (entity != null) entity.setBackgroundColor(color);
    }
    
    @Kapi
    @Override
    public void setTextOpacity(byte opacity) {
        super.setTextOpacity(opacity);
        if (entity != null) entity.setTextOpacity(opacity);
    }
    
    @Kapi
    @Override
    public void setShadowed(boolean shadow) {
        super.setShadowed(shadow);
        if (entity != null) entity.setShadowed(shadow);
    }
    
    @Kapi
    @Override
    public void setSeeThrough(boolean seeThrough) {
        super.setSeeThrough(seeThrough);
        if (entity != null) entity.setSeeThrough(seeThrough);
    }
    
    @Kapi
    @Override
    public void setDefaultBackground(boolean defaultBackground) {
        super.setDefaultBackground(defaultBackground);
        if (entity != null) entity.setDefaultBackground(defaultBackground);
    }
    
    @Kapi
    @Override
    public void setAlignment(TextDisplay.TextAlignment alignment) {
        super.setAlignment(alignment);
        if (entity != null) entity.setAlignment(alignment);
    }
    
    /**
     * @return a deep copy of this TextDisplayRender
     */
    @Kapi
    @Override
    public TextDisplayRender clone() {
        return new TextDisplayRender(super.clone());
    }
    
}