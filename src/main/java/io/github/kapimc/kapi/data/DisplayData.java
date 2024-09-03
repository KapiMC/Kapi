/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.data;

import io.github.kapimc.kapi.annotations.Kapi;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public sealed abstract class DisplayData permits BlockDisplayData, ItemDisplayData, TextDisplayData {
    
    private Transformation transformation;
    private int interpolationDuration;
    private float viewRange;
    private float shadowRadius;
    private float shadowStrength;
    private float displayWidth;
    private float displayHeight;
    private int interpolationDelay;
    private Display.Billboard billboard;
    private Color glowColorOverride;
    private Display.Brightness brightness;
    
    protected DisplayData(
        Transformation transformation,
        int interpolationDuration,
        float viewRange,
        float shadowRadius,
        float shadowStrength,
        float displayWidth,
        float displayHeight,
        int interpolationDelay,
        Display.Billboard billboard,
        Color glowColorOverride,
        Display.Brightness brightness
    ) {
        this.transformation = transformation;
        this.interpolationDuration = interpolationDuration;
        this.viewRange = viewRange;
        this.shadowRadius = shadowRadius;
        this.shadowStrength = shadowStrength;
        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
        this.interpolationDelay = interpolationDelay;
        this.billboard = billboard;
        this.glowColorOverride = glowColorOverride;
        this.brightness = brightness;
    }
    
    /**
     * Copy constructor (deep copy).
     *
     * @param data the data to copy
     */
    protected DisplayData(DisplayData data) {
        Vector3f translation = new Vector3f();
        Quaternionf leftRotation = new Quaternionf();
        Vector3f scale = new Vector3f();
        Quaternionf rightRotation = new Quaternionf();
        data.getTransformation().getTranslation().get(translation);
        data.getTransformation().getLeftRotation().get(leftRotation);
        data.getTransformation().getScale().get(scale);
        data.getTransformation().getRightRotation().get(rightRotation);
        this.transformation = new Transformation(translation, leftRotation, scale, rightRotation);
        
        this.interpolationDuration = data.getInterpolationDuration();
        this.viewRange = data.getViewRange();
        this.shadowRadius = data.getShadowRadius();
        this.shadowStrength = data.getShadowStrength();
        this.displayWidth = data.getDisplayWidth();
        this.displayHeight = data.getDisplayHeight();
        this.interpolationDelay = data.getInterpolationDelay();
        
        this.billboard = data.getBillboard();
        this.glowColorOverride = Color.fromARGB(data.getGlowColorOverride().asARGB());
        this.brightness = new Display.Brightness(
            data.getBrightness().getBlockLight(),
            data.getBrightness().getSkyLight()
        );
    }
    
    /**
     * @return the transformation of this display entity
     */
    @Kapi
    public Transformation getTransformation() {
        return transformation;
    }
    
    /**
     * @param transformation the new transformation of this display entity
     */
    @Kapi
    public void setTransformation(Transformation transformation) {
        this.transformation = transformation;
    }
    
    /**
     * TODO: if you know what this does please open a PR and document it
     *
     * @return the interpolation duration in ticks
     */
    @Kapi
    public int getInterpolationDuration() {
        return interpolationDuration;
    }
    
    /**
     * @param duration the new interpolation duration in ticks
     */
    @Kapi
    public void setInterpolationDuration(int duration) {
        this.interpolationDuration = duration;
    }
    
    /**
     * @return how far away players can see the display entity in blocks
     */
    @Kapi
    public float getViewRange() {
        return viewRange;
    }
    
    /**
     * View range indicates how far away players can see the display entity in blocks.
     *
     * @param range new range in blocks
     */
    @Kapi
    public void setViewRange(float range) {
        this.viewRange = range;
    }
    
    /**
     * TODO: if you know what this does please open a PR and document it
     *
     * @return the shadow radius of this display entity
     */
    @Kapi
    public float getShadowRadius() {
        return shadowRadius;
    }
    
    /**
     * TODO: if you know what this does please open a PR and document it
     *
     * @param radius the new shadow radius of this display entity
     */
    @Kapi
    public void setShadowRadius(float radius) {
        this.shadowRadius = radius;
    }
    
    /**
     * TODO: if you know what this does please open a PR and document it
     *
     * @return the shadow strength of this display entity
     */
    @Kapi
    public float getShadowStrength() {
        return shadowStrength;
    }
    
    /**
     * TODO: if you know what this does please open a PR and document it
     *
     * @param strength the new shadow strength of this display entity
     */
    @Kapi
    public void setShadowStrength(float strength) {
        this.shadowStrength = strength;
    }
    
    /**
     * @return the width of this display entity in blocks
     */
    @Kapi
    public float getDisplayWidth() {
        return displayWidth;
    }
    
    /**
     * @param width the new width of this display entity in blocks
     */
    @Kapi
    public void setDisplayWidth(float width) {
        this.displayWidth = width;
    }
    
    /**
     * @return the height of this display entity in blocks
     */
    @Kapi
    public float getDisplayHeight() {
        return displayHeight;
    }
    
    /**
     * @param height the new height of this display entity in blocks
     */
    @Kapi
    public void setDisplayHeight(float height) {
        this.displayHeight = height;
    }
    
    /**
     * @return the number of ticks before client-side interpolation will begin
     */
    @Kapi
    public int getInterpolationDelay() {
        return interpolationDelay;
    }
    
    /**
     * @param ticks the new number of ticks before client-side interpolation will begin
     */
    @Kapi
    public void setInterpolationDelay(int ticks) {
        this.interpolationDelay = ticks;
    }
    
    /**
     * The billboard controls the automatic rotation of the entity to face the player.
     *
     * @return the billboard of this display entity
     */
    @Kapi
    public Display.Billboard getBillboard() {
        return billboard;
    }
    
    /**
     * The billboard controls the automatic rotation of the entity to face the player.
     *
     * @param billboard the new billboard of this display entity
     */
    @Kapi
    public void setBillboard(Display.Billboard billboard) {
        this.billboard = billboard;
    }
    
    /**
     * This is the glowing outline color of the entity when it glows.
     *
     * @return the scoreboard team overridden glow color of this display entity
     */
    @Kapi
    public Color getGlowColorOverride() {
        return glowColorOverride;
    }
    
    /**
     * This is the glowing outline color of the entity when it glows.
     *
     * @param color the new scoreboard team overridden glow color of this
     */
    @Kapi
    public void setGlowColorOverride(Color color) {
        this.glowColorOverride = color;
    }
    
    /**
     * The brightness values are the same as Minecraft's light levels.
     *
     * @return the brightness of this display entity
     */
    @Kapi
    public Display.Brightness getBrightness() {
        return brightness;
    }
    
    /**
     * The brightness values are the same as Minecraft's light levels.
     *
     * @param brightness the new brightness of this display entity
     */
    @Kapi
    public void setBrightness(Display.Brightness brightness) {
        this.brightness = brightness;
    }
}
