/*
 * Copyright (c) 2024 Kapi Contributors. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted if the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions, the following disclaimer and the list of contributors.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation and/or
 *    other materials provided with the distribution.
 *
 * 3. The buyer of the "Kapi" API is granted the right to use this software
 *    as a dependency in their own software projects. However, the buyer
 *    may not resell or distribute the "Kapi" API, in whole or in part, to other parties.
 *
 * 4. The buyer may include the "Kapi" API in a "fat jar" along with their own code.
 *    The license for the "fat jar" is at the buyer's discretion and may allow
 *    redistribution of the "fat jar", but the "Kapi" API code inside the "fat jar"
 *    must not be modified.
 *
 * 5. Neither the name "Kapi" nor the names of its contributors may be used to endorse
 *    or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY "Kapi" API, AND ITS CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL "Kapi" API, AND CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Kapi Contributors:
 * - Kyren223
 */

package me.kyren223.kapi.data;

import me.kyren223.kapi.annotations.Kapi;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.jspecify.annotations.NullMarked;

/**
 * Holds the common data for all displays.<br>
 * Do not use this class directly, and do not extend this class.<br>
 * <br>
 * Subclasses of this class are
 * {@link ItemDisplayData}, {@link BlockDisplayData} and {@link TextDisplayData}.
 */
@Kapi
@NullMarked
public abstract class DisplayData {
    
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
     * Copy constructor.<br>
     * Note: This deep copies the transformation, and the glow color override.
     *
     * @param data The data to copy
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
     * Gets the transformation applied to this display.
     *
     * @return the transformation
     */
    @Kapi
    public Transformation getTransformation() {
        return transformation;
    }
    
    /**
     * Sets the transformation applied to this display.
     *
     * @param transformation the new transformation
     */
    @Kapi
    public void setTransformation(Transformation transformation) {
        this.transformation = transformation;
    }
    
    /**
     * Gets the interpolation duration of this display.
     *
     * @return interpolation duration
     */
    @Kapi
    public int getInterpolationDuration() {
        return interpolationDuration;
    }
    
    /**
     * Sets the interpolation duration of this display.
     *
     * @param duration new duration
     */
    @Kapi
    public void setInterpolationDuration(int duration) {
        this.interpolationDuration = duration;
    }
    
    /**
     * Gets the view distance/range of this display.
     *
     * @return view range
     */
    @Kapi
    public float getViewRange() {
        return viewRange;
    }
    
    /**
     * Sets the view distance/range of this display.
     *
     * @param range new range
     */
    @Kapi
    public void setViewRange(float range) {
        this.viewRange = range;
    }
    
    /**
     * Gets the shadow radius of this display.
     *
     * @return radius
     */
    @Kapi
    public float getShadowRadius() {
        return shadowRadius;
    }
    
    /**
     * Sets the shadow radius of this display.
     *
     * @param radius new radius
     */
    @Kapi
    public void setShadowRadius(float radius) {
        this.shadowRadius = radius;
    }
    
    /**
     * Gets the shadow strength of this display.
     *
     * @return shadow strength
     */
    @Kapi
    public float getShadowStrength() {
        return shadowStrength;
    }
    
    /**
     * Sets the shadow strength of this display.
     *
     * @param strength new strength
     */
    @Kapi
    public void setShadowStrength(float strength) {
        this.shadowStrength = strength;
    }
    
    /**
     * Gets the width of this display.
     *
     * @return width
     */
    @Kapi
    public float getDisplayWidth() {
        return displayWidth;
    }
    
    /**
     * Sets the width of this display.
     *
     * @param width new width
     */
    @Kapi
    public void setDisplayWidth(float width) {
        this.displayWidth = width;
    }
    
    /**
     * Gets the height of this display.
     *
     * @return height
     */
    @Kapi
    public float getDisplayHeight() {
        return displayHeight;
    }
    
    /**
     * Sets the height of this display.
     *
     * @param height new height
     */
    @Kapi
    public void setDisplayHeight(float height) {
        this.displayHeight = height;
    }
    
    /**
     * Gets the number of ticks before client-side interpolation will begin.
     *
     * @return interpolation delay ticks
     */
    @Kapi
    public int getInterpolationDelay() {
        return interpolationDelay;
    }
    
    /**
     * Sets the number of ticks before client-side interpolation will begin.
     *
     * @param ticks interpolation delay ticks
     */
    @Kapi
    public void setInterpolationDelay(int ticks) {
        this.interpolationDelay = ticks;
    }
    
    /**
     * Gets the view distance/range of this display.
     *
     * @return view range
     */
    @Kapi
    public Display.Billboard getBillboard() {
        return billboard;
    }
    
    /**
     * Sets the billboard setting of this entity.<br>
     * The billboard setting controls the automatic rotation of the entity to
     * face the player.
     *
     * @param billboard new setting
     */
    @Kapi
    public void setBillboard(Display.Billboard billboard) {
        this.billboard = billboard;
    }
    
    /**
     * Gets the scoreboard team overridden glow color of this display.
     *
     * @return glow color
     */
    @Kapi
    public Color getGlowColorOverride() {
        return glowColorOverride;
    }
    
    /**
     * Sets the scoreboard team overridden glow color of this display.
     *
     * @param color new color
     */
    @Kapi
    public void setGlowColorOverride(Color color) {
        this.glowColorOverride = color;
    }
    
    /**
     * Gets the brightness override of the entity.
     *
     * @return brightness override, if set
     */
    @Kapi
    public Display.Brightness getBrightness() {
        return brightness;
    }
    
    /**
     * Sets the brightness override of the entity.
     *
     * @param brightness new brightness override
     */
    @Kapi
    public void setBrightness(Display.Brightness brightness) {
        this.brightness = brightness;
    }
}
