/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.utility;

import io.github.kapimc.kapi.annotations.Kapi;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class DisplayBuilder<T extends DisplayBuilder<T>> {
    
    protected Transformation transformation;
    protected int interpolationDuration;
    protected float viewRange;
    protected float shadowRadius;
    protected float shadowStrength;
    protected float displayWidth;
    protected float displayHeight;
    protected int interpolationDelay;
    protected Display.Billboard billboard;
    protected Color glowColorOverride;
    protected Display.Brightness brightness;
    
    protected DisplayBuilder() {
        this.transformation = new Transformation(
            new Vector3f(),
            new Quaternionf(),
            new Vector3f(1, 1, 1),
            new Quaternionf()
        );
        this.interpolationDuration = 0;
        this.viewRange = 32; // Same as particles
        this.shadowRadius = 0;
        this.shadowStrength = 0;
        this.displayWidth = 1;
        this.displayHeight = 1;
        this.interpolationDelay = 0;
        this.billboard = Display.Billboard.FIXED;
        this.glowColorOverride = Color.WHITE;
        this.brightness = new Display.Brightness(14, 14); // Max brightness
    }
    
    /**
     * This pattern uses generics to return the subclass of this builder.
     * This is useful for sharing code between builders.
     * While still returning the subclass, instead of the parent class.
     *
     * @return the subclass of this builder
     */
    
    @SuppressWarnings("unchecked")
    private T self() {
        return (T) this;
    }
    
    /**
     * Indicates from how far away players can see the display.
     *
     * @param viewRange the view range of the display
     * @return this, for chaining
     */
    @Kapi
    public T viewRange(float viewRange) {
        this.viewRange = viewRange;
        return self();
    }
    
    /**
     * TODO: if you know what this does please open a PR and document it
     *
     * @param shadowRadius the shadow radius of the display
     * @return this, for chaining
     */
    @Kapi
    public T shadowRadius(float shadowRadius) {
        this.shadowRadius = shadowRadius;
        return self();
    }
    
    /**
     * TODO: if you know what this does please open a PR and document it
     *
     * @param shadowStrength the shadow strength of the display
     * @return this, for chaining
     */
    @Kapi
    public T shadowStrength(float shadowStrength) {
        this.shadowStrength = shadowStrength;
        return self();
    }
    
    /**
     * @param displayWidth the display width in blocks
     * @return this, for chaining
     */
    @Kapi
    public T displayWidth(float displayWidth) {
        this.displayWidth = displayWidth;
        return self();
    }
    
    /**
     * @param displayHeight the display height in blocks
     * @return this, for chaining
     */
    @Kapi
    public T displayHeight(float displayHeight) {
        this.displayHeight = displayHeight;
        return self();
    }
    
    /**
     * TODO: if you know what this does please open a PR and document it
     *
     * @param interpolationDelay the interpolation delay in ticks
     * @return this, for chaining
     */
    @Kapi
    public T interpolationDelay(int interpolationDelay) {
        this.interpolationDelay = interpolationDelay;
        return self();
    }
    
    /**
     * TODO: if you know what this does please open a PR and document it
     *
     * @param billboard the billboard of the display
     * @return this, for chaining
     */
    @Kapi
    public T billboard(Display.Billboard billboard) {
        this.billboard = billboard;
        return self();
    }
    
    /**
     * This is the glowing outline color of the entity when it glows.
     *
     * @param glowColorOverride the glow color override of the display
     * @return this, for chaining
     */
    @Kapi
    public T glowColorOverride(Color glowColorOverride) {
        this.glowColorOverride = glowColorOverride;
        return self();
    }
    
    /**
     * The brightness of the display entity.
     * The values are the same as Minecraft's light levels.
     *
     * @param brightness the brightness of the display
     * @return this, for chaining
     */
    @Kapi
    public T brightness(Display.Brightness brightness) {
        this.brightness = brightness;
        return self();
    }
    
    /**
     * @param transformation the transformation of the display
     * @return this, for chaining
     */
    @Kapi
    public T transformation(Transformation transformation) {
        this.transformation = transformation;
        return self();
    }
    
    /**
     * @param interpolationDuration the interpolation duration in ticks
     * @return this, for chaining
     */
    @Kapi
    public T interpolationDuration(int interpolationDuration) {
        this.interpolationDuration = interpolationDuration;
        return self();
    }
    
    /**
     * The translation is the position in the world where the display entity is located.
     *
     * @param x the x component of the translation
     * @param y the y component of the translation
     * @param z the z component of the translation
     * @return this, for chaining
     */
    @Kapi
    public T translation(float x, float y, float z) {
        this.transformation.getTranslation().set(x, y, z);
        return self();
    }
    
    /**
     * @param translation the position in the world where the display entity is located
     * @return this, for chaining
     */
    @Kapi
    public T translation(Vector3f translation) {
        this.transformation.getTranslation().set(translation);
        return self();
    }
    
    /**
     * @param translation the position in the world where the display entity is located
     * @return this, for chaining
     */
    @Kapi
    public T translation(Vector translation) {
        this.transformation.getTranslation().set(translation.toVector3f());
        return self();
    }
    
    /**
     * @param q the left quaternion, which represents the rotation of the display entity
     * @return this, for chaining
     */
    @Kapi
    public T rotation(Quaternionf q) {
        this.transformation.getLeftRotation().set(q);
        return self();
    }
    
    /**
     * Sets the rotation of the display entity based on the given axis and angle.
     *
     * @param axis  the axis of rotation
     * @param angle the angle of rotation
     * @return this, for chaining
     */
    @Kapi
    public T rotation(Vector3f axis, float angle) {
        this.transformation.getLeftRotation().set(new AxisAngle4f(angle, axis));
        return self();
    }
    
    /**
     * Sets the rotation of the display entity based on the given axis and angle.
     *
     * @param axis  the axis of rotation
     * @param angle the angle of rotation
     * @return this, for chaining
     */
    @Kapi
    public T rotation(Vector axis, float angle) {
        this.transformation.getLeftRotation().set(new AxisAngle4f(angle, axis.toVector3f()));
        return self();
    }
    
    /**
     * Sets the rotation of the display entity based on the given axis angle.
     *
     * @param axisAngle the axis and angle of the rotation
     * @return this, for chaining
     */
    @Kapi
    public T rotation(AxisAngle4f axisAngle) {
        this.transformation.getLeftRotation().set(axisAngle);
        return self();
    }
    
    /**
     * The quaternion represents the rotation of the display entity
     *
     * @param x the x component of the quaternion
     * @param y the y component of the quaternion
     * @param z the z component of the quaternion
     * @param w the w component of the quaternion
     * @return this, for chaining
     */
    @Kapi
    public T rotation(float x, float y, float z, float w) {
        this.transformation.getLeftRotation().set(x, y, z, w);
        return self();
    }
    
    /**
     * Sets the right rotation of the display entity.
     *
     * @param q the quaternion representing the rotation
     * @return this, for chaining
     */
    @Kapi
    public T rightRotation(Quaternionf q) {
        this.transformation.getRightRotation().set(q);
        return self();
    }
    
    /**
     * Sets the right rotation of the display entity based on the given axis and angle.
     *
     * @param axis  the axis of rotation
     * @param angle the angle of rotation
     * @return this, for chaining
     */
    @Kapi
    public T rightRotation(Vector3f axis, float angle) {
        this.transformation.getRightRotation().set(new AxisAngle4f(angle, axis));
        return self();
    }
    
    /**
     * Sets the right rotation of the display entity based on the given axis and angle.
     *
     * @param axis  the axis of rotation
     * @param angle the angle of rotation
     * @return this, for chaining
     */
    @Kapi
    public T rightRotation(Vector axis, float angle) {
        this.transformation.getRightRotation().set(new AxisAngle4f(angle, axis.toVector3f()));
        return self();
    }
    
    /**
     * Sets the right rotation of the display entity based on the given axis angle.
     *
     * @param axisAngle the axis angle of rotation
     * @return this, for chaining
     */
    @Kapi
    public T rightRotation(AxisAngle4f axisAngle) {
        this.transformation.getRightRotation().set(axisAngle);
        return self();
    }
    
    /**
     * Sets the right rotation of the display entity based on the given quaternion.
     *
     * @param x the x component of the quaternion
     * @param y the y component of the quaternion
     * @param z the z component of the quaternion
     * @param w the w component of the quaternion
     * @return this, for chaining
     */
    @Kapi
    public T rightRotation(float x, float y, float z, float w) {
        this.transformation.getRightRotation().set(x, y, z, w);
        return self();
    }
    
    /**
     * @param x the x component of the scale in blocks
     * @param y the y component of the scale in blocks
     * @param z the z component of the scale in blocks
     * @return this, for chaining
     */
    @Kapi
    public T scale(float x, float y, float z) {
        this.transformation.getScale().set(x, y, z);
        return self();
    }
    
    /**
     * @param scale the scale of the display entity in blocks
     * @return this, for chaining
     */
    @Kapi
    public T scale(Vector3f scale) {
        this.transformation.getScale().set(scale);
        return self();
    }
    
    /**
     * @param scale the scale of the display entity in blocks
     * @return this, for chaining
     */
    @Kapi
    public T scale(Vector scale) {
        this.transformation.getScale().set(scale.toVector3f());
        return self();
    }
    
}
