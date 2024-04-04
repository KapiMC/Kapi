package me.kyren223.kapi.utility;

import me.kyren223.kapi.annotations.Kapi;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class DisplayBuilder<T extends DisplayBuilder<T>> {
    
    /*
    Note, a lot of methods have @SuppressWarnings("unchecked")
    This is because the generic T indicates the type of the builder.
    Which can be BlockDisplayBuilder, ItemDisplayBuilder, or TextDisplayBuilder.
    This makes sure the subclass is returned
    Casting to T generates a warning that we can suppress
    as it's always safe to cast to the subclass, as long as it's not an instance of
    this class, and the only space we use this class' constructor is as super() in the subclass constructors
     */
    
    protected @NotNull Transformation transformation;
    protected int interpolationDuration;
    protected float viewRange;
    protected float shadowRadius;
    protected float shadowStrength;
    protected float displayWidth;
    protected float displayHeight;
    protected int interpolationDelay;
    protected @NotNull Display.Billboard billboard;
    protected @NotNull Color glowColorOverride;
    protected @NotNull Display.Brightness brightness;
    
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
     * Sets the view range of the display.<br>
     * Default value if not set is 32
     *
     * @param viewRange the view range of the display
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T viewRange(float viewRange) {
        this.viewRange = viewRange;
        return (T) this;
    }
    
    /**
     * Sets the shadow radius of the display.<br>
     * Default value is 0
     *
     * @param shadowRadius the shadow radius of the display
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T shadowRadius(float shadowRadius) {
        this.shadowRadius = shadowRadius;
        return (T) this;
    }
    
    /**
     * Sets the shadow strength of the display.<br>
     * Default value is 0
     *
     * @param shadowStrength the shadow strength of the display
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T shadowStrength(float shadowStrength) {
        this.shadowStrength = shadowStrength;
        return (T) this;
    }
    
    /**
     * Sets the display width of the display.<br>
     * Default value is 1
     *
     * @param displayWidth the display width of the display
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T displayWidth(float displayWidth) {
        this.displayWidth = displayWidth;
        return (T) this;
    }
    
    /**
     * Sets the display height of the display.<br>
     * Default value is 1
     *
     * @param displayHeight the display height of the display
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T displayHeight(float displayHeight) {
        this.displayHeight = displayHeight;
        return (T) this;
    }
    
    /**
     * Sets the interpolation delay of the display.<br>
     * Default value is 0
     *
     * @param interpolationDelay the interpolation delay of the display
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T interpolationDelay(int interpolationDelay) {
        this.interpolationDelay = interpolationDelay;
        return (T) this;
    }
    
    /**
     * Sets the billboard of the display.<br>
     * Default value is FIXED
     *
     * @param billboard the billboard of the display
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T billboard(@NotNull Display.Billboard billboard) {
        this.billboard = billboard;
        return (T) this;
    }
    
    /**
     * Sets the glow color override of the display.<br>
     * Default value is {@link Color#WHITE}
     *
     * @param glowColorOverride the glow color override of the display
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T glowColorOverride(@NotNull Color glowColorOverride) {
        this.glowColorOverride = glowColorOverride;
        return (T) this;
    }
    
    /**
     * Sets the brightness of the display.<br>
     * Default value is max brightness
     *
     * @param brightness the brightness of the display
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T brightness(@NotNull Display.Brightness brightness) {
        this.brightness = brightness;
        return (T) this;
    }
    
    /**
     * Sets the transformation of the display.<br>
     * Default value is a new transformation with zero translation, rotation, and (1, 1, 1) scale
     *
     * @param transformation the transformation of the display
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T transformation(@NotNull Transformation transformation) {
        this.transformation = transformation;
        return (T) this;
    }
    
    /**
     * Sets the interpolation duration of the display.<br>
     * Default value is 0
     *
     * @param interpolationDuration the interpolation duration of the display
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T interpolationDuration(int interpolationDuration) {
        this.interpolationDuration = interpolationDuration;
        return (T) this;
    }
    
    /**
     * Sets the translation of the display.<br>
     * Default value is (0, 0, 0)
     *
     * @param x the x component of the translation
     * @param y the y component of the translation
     * @param z the z component of the translation
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T translation(float x, float y, float z) {
        this.transformation.getTranslation().set(x, y, z);
        return (T) this;
    }
    
    /**
     * Sets the translation of the display.<br>
     * Default value is (0, 0, 0)
     *
     * @param translation the translation
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T translation(@NotNull Vector3f translation) {
        this.transformation.getTranslation().set(translation);
        return (T) this;
    }
    
    /**
     * Sets the translation of the display.<br>
     * Default value is (0, 0, 0)
     *
     * @param translation the translation
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T translation(@NotNull Vector translation) {
        this.transformation.getTranslation().set(translation.toVector3f());
        return (T) this;
    }
    
    /**
     * Sets the left rotation of the display.<br>
     * Default value is the quaternion (x0, y0, z0, w1)
     *
     * @param q The quaternion representing the rotation
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T rotation(Quaternionf q) {
        this.transformation.getLeftRotation().set(q);
        return (T) this;
    }
    
    /**
     * Sets the left rotation of the display.<br>
     * Default value is the quaternion (x0, y0, z0, w1)
     *
     * @param axis the axis of rotation
     * @param angle the angle of rotation
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T rotation(Vector3f axis, float angle) {
        this.transformation.getLeftRotation().set(new AxisAngle4f(angle, axis));
        return (T) this;
    }
    
    /**
     * Sets the left rotation of the display.<br>
     * Default value is the quaternion (x0, y0, z0, w1)
     *
     * @param axis the axis of rotation
     * @param angle the angle of rotation
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T rotation(Vector axis, float angle) {
        this.transformation.getLeftRotation().set(new AxisAngle4f(angle, axis.toVector3f()));
        return (T) this;
    }
    
    /**
     * Sets the left rotation of the display.<br>
     * Default value is the quaternion (x0, y0, z0, w1)
     *
     * @param axisAngle the axis angle of rotation
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T rotation(AxisAngle4f axisAngle) {
        this.transformation.getLeftRotation().set(axisAngle);
        return (T) this;
    }
    
    /**
     * Sets the left rotation of the display.<br>
     * Default value is the quaternion (x0, y0, z0, w1)
     *
     * @param x the x component of the quaternion
     * @param y the y component of the quaternion
     * @param z the z component of the quaternion
     * @param w the w component of the quaternion
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T rotation(float x, float y, float z, float w) {
        this.transformation.getLeftRotation().set(x, y, z, w);
        return (T) this;
    }
    
    /**
     * Sets the right rotation of the display.<br>
     * Default value is the quaternion (x0, y0, z0, w1)
     *
     * @param q The quaternion representing the rotation
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T rightRotation(Quaternionf q) {
        this.transformation.getRightRotation().set(q);
        return (T) this;
    }
    
    /**
     * Sets the right rotation of the display.<br>
     * Default value is the quaternion (x0, y0, z0, w1)
     *
     * @param axis the axis of rotation
     * @param angle the angle of rotation
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T rightRotation(Vector3f axis, float angle) {
        this.transformation.getRightRotation().set(new AxisAngle4f(angle, axis));
        return (T) this;
    }
    
    /**
     * Sets the right rotation of the display.<br>
     * Default value is the quaternion (x0, y0, z0, w1)
     *
     * @param axis the axis of rotation
     * @param angle the angle of rotation
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T rightRotation(Vector axis, float angle) {
        this.transformation.getRightRotation().set(new AxisAngle4f(angle, axis.toVector3f()));
        return (T) this;
    }
    
    /**
     * Sets the right rotation of the display.<br>
     * Default value is the quaternion (x0, y0, z0, w1)
     *
     * @param axisAngle the axis angle of rotation
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T righRotation(AxisAngle4f axisAngle) {
        this.transformation.getRightRotation().set(axisAngle);
        return (T) this;
    }
    
    /**
     * Sets the right rotation of the display.<br>
     * Default value is the quaternion (x0, y0, z0, w1)
     *
     * @param x the x component of the quaternion
     * @param y the y component of the quaternion
     * @param z the z component of the quaternion
     * @param w the w component of the quaternion
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T rightRotation(float x, float y, float z, float w) {
        this.transformation.getRightRotation().set(x, y, z, w);
        return (T) this;
    }
    
    /**
     * Sets the scale of the display.<br>
     * Default value is (1, 1, 1)
     *
     * @param x the x component of the scale
     * @param y the y component of the scale
     * @param z the z component of the scale
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T scale(float x, float y, float z) {
        this.transformation.getScale().set(x, y, z);
        return (T) this;
    }
    
    /**
     * Sets the scale of the display.<br>
     * Default value is (1, 1, 1)
     *
     * @param scale the scale of the display
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T scale(Vector3f scale) {
        this.transformation.getScale().set(scale);
        return (T) this;
    }
    
    /**
     * Sets the scale of the display.<br>
     * Default value is (1, 1, 1)
     *
     * @param scale the scale of the display
     * @return the builder
     */
    @SuppressWarnings("unchecked")
    @Kapi
    public T scale(Vector scale) {
        this.transformation.getScale().set(scale.toVector3f());
        return (T) this;
    }
    
}
