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
import me.kyren223.kapi.engine.renderable.TextDisplayRender;
import me.kyren223.kapi.utility.TextDisplayBuilder;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Holds the data for a text display.
 * See {@link TextDisplayRender} for rendering text displays.
 * See {@link TextDisplayBuilder} for creating new instances.
 */
@Kapi
@NullMarked
public class TextDisplayData extends DisplayData {
    
    private @Nullable String text;
    private int lineWidth;
    private @Nullable Color backgroundColor;
    private byte textOpacity;
    private boolean shadowed;
    private boolean seeThrough;
    private boolean defaultBackground;
    private TextDisplay.TextAlignment alignment;
    
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
    
    /**
     * Copy constructor.<br>
     * Note: Does not clone the background color.
     *
     * @param data The data to copy
     */
    @Kapi
    public TextDisplayData(TextDisplayData data) {
        super(data);
        this.text = data.getText();
        this.lineWidth = data.getLineWidth();
        this.backgroundColor = data.getBackgroundColor();
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
    @Kapi
    public @Nullable String getText() {
        return text;
    }
    
    /**
     * Sets the displayed text.
     *
     * @param text the new text
     */
    @Kapi
    public void setText(@Nullable String text) {
        this.text = text;
    }
    
    /**
     * Gets the maximum line width before wrapping.
     *
     * @return the line width
     */
    @Kapi
    public int getLineWidth() {
        return lineWidth;
    }
    
    /**
     * Sets the maximum line width before wrapping.
     *
     * @param width new line width
     */
    @Kapi
    public void setLineWidth(int width) {
        this.lineWidth = width;
    }
    
    /**
     * Gets the text background color.
     *
     * @return the background color
     * @deprecated API subject to change
     */
    @Kapi
    @Deprecated
    public @Nullable Color getBackgroundColor() {
        return backgroundColor;
    }
    
    /**
     * Sets the text background color.
     *
     * @param color new background color
     * @deprecated API subject to change
     */
    @Kapi
    @Deprecated
    public void setBackgroundColor(@Nullable Color color) {
        this.backgroundColor = color;
    }
    
    /**
     * Gets the text opacity.
     *
     * @return opacity or -1 if not set
     */
    @Kapi
    public byte getTextOpacity() {
        return textOpacity;
    }
    
    /**
     * Sets the text opacity.
     *
     * @param opacity new opacity or -1 if default
     */
    @Kapi
    public void setTextOpacity(byte opacity) {
        this.textOpacity = opacity;
    }
    
    /**
     * Gets if the text is shadowed.
     *
     * @return shadow status
     */
    @Kapi
    public boolean isShadowed() {
        return shadowed;
    }
    
    /**
     * Sets if the text is shadowed.
     *
     * @param shadow if shadowed
     */
    @Kapi
    public void setShadowed(boolean shadow) {
        this.shadowed = shadow;
    }
    
    /**
     * Gets if the text is see through.
     *
     * @return see through status
     */
    @Kapi
    public boolean isSeeThrough() {
        return seeThrough;
    }
    
    /**
     * Sets if the text is see through.
     *
     * @param seeThrough if see through
     */
    @Kapi
    public void setSeeThrough(boolean seeThrough) {
        this.seeThrough = seeThrough;
    }
    
    /**
     * Gets if the text has its default background.
     *
     * @return default background
     */
    @Kapi
    public boolean isDefaultBackground() {
        return defaultBackground;
    }
    
    /**
     * Sets if the text has its default background.
     *
     * @param defaultBackground if default
     */
    @Kapi
    public void setDefaultBackground(boolean defaultBackground) {
        this.defaultBackground = defaultBackground;
    }
    
    /**
     * Gets the text alignment for this display.
     *
     * @return text alignment
     */
    @Kapi
    public TextDisplay.TextAlignment getAlignment() {
        return alignment;
    }
    
    /**
     * Sets the text alignment for this display.
     *
     * @param alignment new alignment
     */
    @Kapi
    public void setAlignment(TextDisplay.TextAlignment alignment) {
        this.alignment = alignment;
    }
}
