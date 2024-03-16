package me.kyren223.kapi.engine.data;

import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextDisplayData extends DisplayData {
    
    private @Nullable String text;
    private int lineWidth;
    private @Nullable Color backgroundColor;
    private byte textOpacity;
    private boolean shadowed;
    private boolean seeThrough;
    private boolean defaultBackground;
    private @NotNull TextDisplay.TextAlignment alignment;
    
    public TextDisplayData(
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
        super(transformation, interpolationDuration, viewRange, shadowRadius, shadowStrength, displayWidth, displayHeight, interpolationDelay, billboard, glowColorOverride, brightness);
        this.text = text;
        this.lineWidth = lineWidth;
        this.backgroundColor = backgroundColor;
        this.textOpacity = textOpacity;
        this.shadowed = shadowed;
        this.seeThrough = seeThrough;
        this.defaultBackground = defaultBackground;
        this.alignment = alignment;
    }
    
    /**
     * Copy constructor
     *
     * @param data The data to copy
     */
    public TextDisplayData(@NotNull TextDisplayData data) {
        super(data);
        this.text = data.getText();
        this.lineWidth = data.getLineWidth();
        this.backgroundColor = data.getBackgroundColor() == null ?
                null :Color.fromARGB(data.getBackgroundColor().asARGB());
        this.textOpacity = data.getTextOpacity();
        this.shadowed = data.isShadowed();
        this.seeThrough = data.isSeeThrough();
        this.defaultBackground = data.isDefaultBackground();
        this.alignment = data.getAlignment();
    }
    
    /**
     * Gets the displayed text.
     *
     * @return the displayed text.
     */
    @Nullable
    public String getText() {
        return text;
    }
    
    /**
     * Sets the displayed text.
     *
     * @param text the new text
     */
    public void setText(@Nullable String text) {
        this.text = text;
    }
    
    /**
     * Gets the maximum line width before wrapping.
     *
     * @return the line width
     */
    public int getLineWidth() {
        return lineWidth;
    }
    
    /**
     * Sets the maximum line width before wrapping.
     *
     * @param width new line width
     */
    public void setLineWidth(int width) {
        this.lineWidth = width;
    }
    
    /**
     * Gets the text background color.
     *
     * @return the background color
     * @deprecated API subject to change
     */
    @Nullable
    @Deprecated
    public Color getBackgroundColor() {
        return backgroundColor;
    }
    
    /**
     * Sets the text background color.
     *
     * @param color new background color
     * @deprecated API subject to change
     */
    @Deprecated
    public void setBackgroundColor(@Nullable Color color) {
        this.backgroundColor = color;
    }
    
    /**
     * Gets the text opacity.
     *
     * @return opacity or -1 if not set
     */
    public byte getTextOpacity() {
        return textOpacity;
    }
    
    /**
     * Sets the text opacity.
     *
     * @param opacity new opacity or -1 if default
     */
    public void setTextOpacity(byte opacity) {
        this.textOpacity = opacity;
    }
    
    /**
     * Gets if the text is shadowed.
     *
     * @return shadow status
     */
    public boolean isShadowed() {
        return shadowed;
    }
    
    /**
     * Sets if the text is shadowed.
     *
     * @param shadow if shadowed
     */
    public void setShadowed(boolean shadow) {
        this.shadowed = shadow;
    }
    
    /**
     * Gets if the text is see through.
     *
     * @return see through status
     */
    public boolean isSeeThrough() {
        return seeThrough;
    }
    
    /**
     * Sets if the text is see through.
     *
     * @param seeThrough if see through
     */
    public void setSeeThrough(boolean seeThrough) {
        this.seeThrough = seeThrough;
    }
    
    /**
     * Gets if the text has its default background.
     *
     * @return default background
     */
    public boolean isDefaultBackground() {
        return defaultBackground;
    }
    
    /**
     * Sets if the text has its default background.
     *
     * @param defaultBackground if default
     */
    public void setDefaultBackground(boolean defaultBackground) {
        this.defaultBackground = defaultBackground;
    }
    
    /**
     * Gets the text alignment for this display.
     *
     * @return text alignment
     */
    @NotNull
    public TextDisplay.TextAlignment getAlignment() {
        return alignment;
    }
    
    /**
     * Sets the text alignment for this display.
     *
     * @param alignment new alignment
     */
    public void setAlignment(@NotNull TextDisplay.TextAlignment alignment) {
        this.alignment = alignment;
    }
}
