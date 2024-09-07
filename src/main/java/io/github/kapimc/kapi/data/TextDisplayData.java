/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.data;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.engine.renderable.TextDisplayRender;
import io.github.kapimc.kapi.utility.TextDisplayBuilder;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.jspecify.annotations.Nullable;

/**
 * Holds the data for a text display.
 * See {@link TextDisplayBuilder} for creating new instances.
 * See {@link TextDisplayRender} for rendering text displays.
 */
@Kapi
public non-sealed class TextDisplayData extends DisplayData {
    
    private @Nullable String text;
    private int lineWidth;
    private @Nullable Color backgroundColor;
    private byte textOpacity;
    private boolean shadowed;
    private boolean seeThrough;
    private boolean defaultBackground;
    private TextDisplay.TextAlignment alignment;
    
    /**
     * Creates a new TextDisplayData instance.
     * It's recommended to use {@link TextDisplayBuilder} to easily create this class.
     *
     * @param transformation        the transformation of the display entity
     * @param interpolationDuration the interpolation duration in ticks
     * @param viewRange             how far away players can see the display entity in blocks
     * @param shadowRadius          the shadow radius of the display entity
     * @param shadowStrength        the shadow strength of the display entity
     * @param displayWidth          the width of the display entity in blocks
     * @param displayHeight         the height of the display entity in blocks
     * @param interpolationDelay    the number of ticks before client-side interpolation will begin
     * @param billboard             the billboard of the display entity
     * @param glowColorOverride     the scoreboard team overridden glow color of the display entity
     * @param brightness            the brightness of the display entity (same as Minecraft's light levels)
     * @param text                  the text of the display entity
     * @param lineWidth             the maximum line width before wrapping
     * @param backgroundColor       the background color of the text
     * @param textOpacity           the text opacity, or -1 to not set
     * @param shadowed              whether to render the text with a shadow
     * @param seeThrough            whether to render the text with a see-through effect
     * @param defaultBackground     whether to use the default background
     * @param alignment             the alignment of the text (LEFT, CENTER, RIGHT)
     */
    @Kapi
    public TextDisplayData(
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
        Display.Brightness brightness,
        @Nullable String text,
        int lineWidth,
        @Nullable Color backgroundColor,
        byte textOpacity,
        boolean shadowed,
        boolean seeThrough,
        boolean defaultBackground,
        TextDisplay.TextAlignment alignment
    ) {
        super(
            transformation, interpolationDuration, viewRange, shadowRadius, shadowStrength,
            displayWidth, displayHeight, interpolationDelay, billboard, glowColorOverride,
            brightness
        );
        this.text = text;
        this.lineWidth = lineWidth;
        this.backgroundColor = backgroundColor;
        this.textOpacity = textOpacity;
        this.shadowed = shadowed;
        this.seeThrough = seeThrough;
        this.defaultBackground = defaultBackground;
        this.alignment = alignment;
    }
    
    private TextDisplayData(TextDisplayData data) {
        super(data);
        this.text = data.getText();
        this.lineWidth = data.getLineWidth();
        this.backgroundColor = data.getBackgroundColor() != null ?
            Color.fromARGB(data.getBackgroundColor().asARGB()) : null;
        this.textOpacity = data.getTextOpacity();
        this.shadowed = data.isShadowed();
        this.seeThrough = data.isSeeThrough();
        this.defaultBackground = data.isDefaultBackground();
        this.alignment = data.getAlignment();
    }
    
    /**
     * @return the display entity text
     */
    @Kapi
    public @Nullable String getText() {
        return text;
    }
    
    /**
     * @param text the new display entity text
     */
    @Kapi
    public void setText(@Nullable String text) {
        this.text = text;
    }
    
    /**
     * @return the maximum line width before wrapping
     */
    @Kapi
    public int getLineWidth() {
        return lineWidth;
    }
    
    /**
     * @param width the new maximum line width before wrapping
     */
    @Kapi
    public void setLineWidth(int width) {
        this.lineWidth = width;
    }
    
    /**
     * @return the text background color
     */
    @Kapi
    public @Nullable Color getBackgroundColor() {
        return backgroundColor;
    }
    
    /**
     * @param color the new text background color
     */
    @Kapi
    public void setBackgroundColor(@Nullable Color color) {
        this.backgroundColor = color;
    }
    
    /**
     * @return the text opacity, or -1 if not set
     */
    @Kapi
    public byte getTextOpacity() {
        return textOpacity;
    }
    
    /**
     * @param opacity the text opacity, or -1 to not set
     */
    @Kapi
    public void setTextOpacity(byte opacity) {
        this.textOpacity = opacity;
    }
    
    /**
     * @return whether the text has a shadow
     */
    @Kapi
    public boolean isShadowed() {
        return shadowed;
    }
    
    /**
     * @param shadow whether the text has a shadow
     */
    @Kapi
    public void setShadowed(boolean shadow) {
        this.shadowed = shadow;
    }
    
    /**
     * @return whether the text is see-through
     */
    @Kapi
    public boolean isSeeThrough() {
        return seeThrough;
    }
    
    /**
     * @param seeThrough whether the text is see-through
     */
    @Kapi
    public void setSeeThrough(boolean seeThrough) {
        this.seeThrough = seeThrough;
    }
    
    /**
     * @return whether the text has the default background
     */
    @Kapi
    public boolean isDefaultBackground() {
        return defaultBackground;
    }
    
    /**
     * @param defaultBackground whether the text has the default background
     */
    @Kapi
    public void setDefaultBackground(boolean defaultBackground) {
        this.defaultBackground = defaultBackground;
    }
    
    /**
     * @return the text alignment for this display entity
     */
    @Kapi
    public TextDisplay.TextAlignment getAlignment() {
        return alignment;
    }
    
    /**
     * @param alignment the new text alignment for this display entity
     */
    @Kapi
    public void setAlignment(TextDisplay.TextAlignment alignment) {
        this.alignment = alignment;
    }
    
    /**
     * @return a deep copy of this TextDisplayData
     */
    @Kapi
    @Override
    public TextDisplayData clone() {
        return new TextDisplayData(this);
    }
}
