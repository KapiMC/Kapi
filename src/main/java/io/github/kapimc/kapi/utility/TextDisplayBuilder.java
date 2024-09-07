/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.utility;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.data.TextDisplayData;
import org.bukkit.Color;
import org.bukkit.entity.TextDisplay;
import org.jspecify.annotations.Nullable;


/**
 * A builder for {@link TextDisplayData}.
 */
@Kapi
public final class TextDisplayBuilder extends DisplayBuilder<TextDisplayBuilder> {
    
    private @Nullable String text;
    private int lineWidth;
    private @Nullable Color backgroundColor;
    private byte textOpacity;
    private boolean shadowed;
    private boolean seeThrough;
    private boolean defaultBackground;
    private TextDisplay.TextAlignment alignment;
    
    private TextDisplayBuilder() {
        text = null;
        lineWidth = 0;
        backgroundColor = null;
        textOpacity = 0;
        shadowed = false;
        seeThrough = false;
        defaultBackground = true;
        alignment = TextDisplay.TextAlignment.LEFT;
    }
    
    /**
     * Creates a new text display builder.
     * <br>
     * All values will be set to their default values, which are:
     * <ul>
     *     <li>text = null;
     *     <li>lineWidth = 0;
     *     <li>backgroundColor = null;
     *     <li>textOpacity = 0;
     *     <li>shadowed = false;
     *     <li>seeThrough = false;
     *     <li>defaultBackground = true;
     *     <li>alignment = TextDisplay.TextAlignment.LEFT;
     *     <li>transformation = new Transformation(
     *          new Vector3f(),new Quaternionf(),new Vector3f(1, 1, 1),new Quaternionf());
     *     <li>interpolationDuration = 0;
     *     <li>viewRange = 32; // same as particles
     *     <li>shadowRadius = 0;
     *     <li>shadowStrength = 0;
     *     <li>displayWidth = 1;
     *     <li>displayHeight = 1;
     *     <li>interpolationDelay = 0;
     *     <li>billboard = Display.Billboard.FIXED;
     *     <li>glowColorOverride = Color.WHITE;
     *     <li>brightness = new Display.Brightness(14, 14); // Max brightness
     * </ul>
     *
     * @return a new builder
     */
    @Kapi
    public static TextDisplayBuilder create() {
        return new TextDisplayBuilder();
    }
    
    /**
     * Creates a new builder with the given text.
     * See {@link #create()} for more information like default values.
     *
     * @param text the text to be displayed
     * @return a new builder
     */
    @Kapi
    public TextDisplayBuilder create(String text) {
        return new TextDisplayBuilder().text(text);
    }
    
    /**
     * @param text the text to be displayed
     * @return this, for chaining
     */
    @Kapi
    public TextDisplayBuilder text(String text) {
        this.text = text;
        return this;
    }
    
    /**
     * TODO: if you know what this does please open a PR and document it
     *
     * @param lineWidth The line width of the text
     * @return this, for chaining
     */
    @Kapi
    public TextDisplayBuilder lineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }
    
    /**
     * @param backgroundColor the background color of the text
     * @return this, for chaining
     */
    @Kapi
    public TextDisplayBuilder backgroundColor(final @Nullable Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }
    
    /**
     * @param textOpacity the text opacity, or -1 to not set
     * @return this, for chaining
     */
    @Kapi
    public TextDisplayBuilder textOpacity(byte textOpacity) {
        this.textOpacity = textOpacity;
        return this;
    }
    
    /**
     * @param shadowed whether to render the text with a shadow
     * @return this, for chaining
     */
    @Kapi
    public TextDisplayBuilder shadowed(boolean shadowed) {
        this.shadowed = shadowed;
        return this;
    }
    
    /**
     * TODO: if you know what this does please open a PR and document it
     *
     * @param seeThrough whether to render the text with a see-through effect
     * @return this, for chaining
     */
    @Kapi
    public TextDisplayBuilder seeThrough(boolean seeThrough) {
        this.seeThrough = seeThrough;
        return this;
    }
    
    /**
     * TODO: if you know what this does please open a PR and document it
     *
     * @param defaultBackground whether to use the default background
     * @return this, for chaining
     */
    @Kapi
    public TextDisplayBuilder defaultBackground(boolean defaultBackground) {
        this.defaultBackground = defaultBackground;
        return this;
    }
    
    /**
     * @param alignment the alignment of the text (LEFT, CENTER, RIGHT)
     * @return this, for chaining
     */
    @Kapi
    public TextDisplayBuilder alignment(final TextDisplay.TextAlignment alignment) {
        this.alignment = alignment;
        return this;
    }
    
    /**
     * @return a new {@link TextDisplayData} with the current settings
     */
    @Kapi
    public TextDisplayData build() {
        return new TextDisplayData(
            transformation,
            interpolationDuration,
            viewRange,
            shadowRadius,
            shadowStrength,
            displayWidth,
            displayHeight,
            interpolationDelay,
            billboard,
            glowColorOverride,
            brightness,
            text,
            lineWidth,
            backgroundColor,
            textOpacity,
            shadowed,
            seeThrough,
            defaultBackground,
            alignment
        );
    }
}
