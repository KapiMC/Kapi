package me.kyren223.kapi.data;

import me.kyren223.kapi.annotations.Kapi;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Holds the common data for all displays.<br>
 * Do not use this class directly, and do not extend this class.<br>
 * <br>
 * Subclasses of this class are
 * {@link ItemDisplayData}, {@link BlockDisplayData} and {@link TextDisplayData}.
 */
@Kapi
public abstract class DisplayData {
    
    private @NotNull Transformation transformation;
    private int interpolationDuration;
    private float viewRange;
    private float shadowRadius;
    private float shadowStrength;
    private float displayWidth;
    private float displayHeight;
    private int interpolationDelay;
    private @NotNull Display.Billboard billboard;
    private @NotNull Color glowColorOverride;
    private @NotNull Display.Brightness brightness;
    
    protected DisplayData(
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
            @NotNull Display.Brightness brightness
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
     * Copy constructor<br>
     * Note: This deep copies the transformation, and the glow color override.
     *
     * @param data The data to copy
     */
    protected DisplayData(@NotNull DisplayData data) {
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
        this.brightness = new Display.Brightness(data.getBrightness().getBlockLight(),
                data.getBrightness().getSkyLight()
        );
    }
    
    /**
     * Gets the transformation applied to this display.
     *
     * @return the transformation
     */
    @Kapi
    @NotNull
    public Transformation getTransformation() {
        return transformation;
    }
    
    /**
     * Sets the transformation applied to this display
     *
     * @param transformation the new transformation
     */
    @Kapi
    public void setTransformation(@NotNull Transformation transformation) {
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
     * Sets the height if this display.
     *
     * @param height new height
     */
    @Kapi
    public void setDisplayHeight(float height) {
        this.displayHeight = height;
    }
    
    /**
     * Gets the amount of ticks before client-side interpolation will commence.
     *
     * @return interpolation delay ticks
     */
    @Kapi
    public int getInterpolationDelay() {
        return interpolationDelay;
    }
    
    /**
     * Sets the amount of ticks before client-side interpolation will commence.
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
    @NotNull
    @Kapi
    public Display.Billboard getBillboard() {
        return billboard;
    }
    
    /**
     * Sets the billboard setting of this entity.<br>
     *
     * The billboard setting controls the automatic rotation of the entity to
     * face the player.
     *
     * @param billboard new setting
     */
    @Kapi
    public void setBillboard(@NotNull Display.Billboard billboard) {
        this.billboard = billboard;
    }
    
    /**
     * Gets the scoreboard team overridden glow color of this display.
     *
     * @return glow color
     */
    @Kapi
    @NotNull
    public Color getGlowColorOverride() {
        return glowColorOverride;
    }
    
    /**
     * Sets the scoreboard team overridden glow color of this display.
     *
     * @param color new color
     */
    @Kapi
    public void setGlowColorOverride(@NotNull Color color) {
        this.glowColorOverride = color;
    }
    
    /**
     * Gets the brightness override of the entity.
     *
     * @return brightness override, if set
     */
    @NotNull
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
    public void setBrightness(@NotNull Display.Brightness brightness) {
        this.brightness = brightness;
    }
}
