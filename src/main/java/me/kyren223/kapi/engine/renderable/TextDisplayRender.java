package me.kyren223.kapi.engine.renderable;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.annotations.ScheduledForRefactor;
import me.kyren223.kapi.engine.data.TextDisplayData;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Used to render text displays.
 */
@Kapi
@ScheduledForRefactor
public class TextDisplayRender extends TextDisplayData implements Renderable {
    private TextDisplay entity;
    
    @Kapi
    @ScheduledForRefactor
    public TextDisplayRender(
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
            @Nullable String text,
            int lineWidth,
            @Nullable Color backgroundColor,
            byte textOpacity,
            boolean shadowed,
            boolean seeThrough,
            boolean defaultBackground,
            @NotNull TextDisplay.TextAlignment alignment
    ) {
        super(transformation, interpolationDuration, viewRange, shadowRadius, shadowStrength, displayWidth, displayHeight, interpolationDelay, billboard, glowColorOverride, brightness, text, lineWidth, backgroundColor, textOpacity, shadowed, seeThrough, defaultBackground, alignment);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void spawn(World world, Vector point) {
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
    
    public TextDisplayRender(@NotNull TextDisplayData data) {
        super(data);
    }
    
    @Override
    public void render(World world, Vector point) {
        // No need to render, the entity will automatically render itself
    }
    
    @Override
    public void despawn(World world, Vector point) {
        entity.remove();
    }
    
    @Override
    @Kapi
    @ScheduledForRefactor
    public void setTransformation(@NotNull Transformation transformation) {
        super.setTransformation(transformation);
        entity.setTransformation(transformation);
    }
    
    @Override
    @Kapi
    @ScheduledForRefactor
    public void setInterpolationDuration(int duration) {
        super.setInterpolationDuration(duration);
        entity.setInterpolationDuration(duration);
    }
    
    @Kapi
    @ScheduledForRefactor
    @Override
    public void setViewRange(float range) {
        super.setViewRange(range);
        entity.setViewRange(range);
    }
    
    @Kapi
    @ScheduledForRefactor
    @Override
    public void setShadowRadius(float radius) {
        super.setShadowRadius(radius);
        entity.setShadowRadius(radius);
    }
    
    @Kapi
    @ScheduledForRefactor
    @Override
    public void setShadowStrength(float strength) {
        super.setShadowStrength(strength);
        entity.setShadowStrength(strength);
    }
    
    @Kapi
    @ScheduledForRefactor
    @Override
    public void setDisplayWidth(float width) {
        super.setDisplayWidth(width);
        entity.setDisplayWidth(width);
    }
    
    @Kapi
    @ScheduledForRefactor
    @Override
    public void setDisplayHeight(float height) {
        super.setDisplayHeight(height);
        entity.setDisplayHeight(height);
    }
    
    @Kapi
    @ScheduledForRefactor
    @Override
    public void setInterpolationDelay(int ticks) {
        super.setInterpolationDelay(ticks);
        entity.setInterpolationDelay(ticks);
    }
    
    @Kapi
    @ScheduledForRefactor
    @Override
    public void setBillboard(Display.@NotNull Billboard billboard) {
        super.setBillboard(billboard);
        entity.setBillboard(billboard);
    }
    
    @Kapi
    @ScheduledForRefactor
    @Override
    public void setGlowColorOverride(@NotNull Color color) {
        super.setGlowColorOverride(color);
        entity.setGlowColorOverride(color);
    }
    
    @Kapi
    @ScheduledForRefactor
    @Override
    public void setBrightness(Display.@NotNull Brightness brightness) {
        super.setBrightness(brightness);
        entity.setBrightness(brightness);
    }
    
    @Kapi
    @ScheduledForRefactor
    @Override
    public void setText(@Nullable String text) {
        super.setText(text);
        entity.setText(text);
    }
    
    @Override
    @Kapi
    @ScheduledForRefactor
    public void setLineWidth(int width) {
        super.setLineWidth(width);
        entity.setLineWidth(width);
    }
    
    /**
     * Sets the text background color.
     *
     * @param color new background color
     * @deprecated API subject to change
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    @Kapi
    @ScheduledForRefactor
    @Override
    public void setBackgroundColor(@Nullable Color color) {
        super.setBackgroundColor(color);
        entity.setBackgroundColor(color);
    }
    
    @Override
    @Kapi
    @ScheduledForRefactor
    public void setTextOpacity(byte opacity) {
        super.setTextOpacity(opacity);
        entity.setTextOpacity(opacity);
    }
    
    @Kapi
    @ScheduledForRefactor
    @Override
    public void setShadowed(boolean shadow) {
        super.setShadowed(shadow);
        entity.setShadowed(shadow);
    }
    
    @Kapi
    @ScheduledForRefactor
    @Override
    public void setSeeThrough(boolean seeThrough) {
        super.setSeeThrough(seeThrough);
        entity.setSeeThrough(seeThrough);
    }
    
    @Kapi
    @ScheduledForRefactor
    @Override
    public void setDefaultBackground(boolean defaultBackground) {
        super.setDefaultBackground(defaultBackground);
        entity.setDefaultBackground(defaultBackground);
    }
    
    @Kapi
    @ScheduledForRefactor
    @Override
    public void setAlignment(TextDisplay.@NotNull TextAlignment alignment) {
        super.setAlignment(alignment);
        entity.setAlignment(alignment);
    }
    
    @Kapi
    @ScheduledForRefactor
    @Override
    public TextDisplayRender clone() {
        return new TextDisplayRender(new TextDisplayData(this));
    }
    
}