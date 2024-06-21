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

package me.kyren223.kapi.utility;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.data.TextDisplayData;
import org.bukkit.Color;
import org.bukkit.entity.TextDisplay;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;


/**
 * A builder for {@link TextDisplayData}.
 */
@Kapi
@NullMarked
public class TextDisplayBuilder extends DisplayBuilder<TextDisplayBuilder> {
    
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
        defaultBackground = false;
        alignment = TextDisplay.TextAlignment.LEFT;
    }
    
    /**
     * Sets the text to display.<br>
     * <br>
     * All values will be set to their default values, which are:
     * <ul>
     *     <li>text = null;
     *     <li>lineWidth = 0;
     *     <li>backgroundColor = null;
     *     <li>textOpacity = 0;
     *     <li>shadowed = false;
     *     <li>seeThrough = false;
     *     <li>defaultBackground = false;
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
     *     <li>billboard = display.billboard.fiXED;
     *     <li>glowColorOverride = color.white;
     * </ul>
     *
     * @return a new builder
     */
    @Kapi
    public static TextDisplayBuilder create() {
        return new TextDisplayBuilder();
    }
    
    /**
     * Creates a new builder for the given text.
     * For other values, see {@link #create()}.
     *
     * @param text The text to display
     * @return a new builder
     */
    @Kapi
    public TextDisplayBuilder create(String text) {
        return new TextDisplayBuilder().text(text);
    }
    
    /**
     * Sets the text to display.
     *
     * @param text The text to display
     * @return this builder
     */
    @Kapi
    public TextDisplayBuilder text(String text) {
        this.text = text;
        return this;
    }
    
    /**
     * Sets the line width.
     *
     * @param lineWidth The line width
     * @return this builder
     */
    public TextDisplayBuilder lineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }
    
    /**
     * Sets the background color.
     *
     * @param backgroundColor The background color
     * @return this builder
     */
    @Kapi
    public TextDisplayBuilder backgroundColor(final @Nullable Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }
    
    /**
     * Sets the text opacity.
     *
     * @param textOpacity The text opacity
     * @return this builder
     */
    @Kapi
    public TextDisplayBuilder textOpacity(byte textOpacity) {
        this.textOpacity = textOpacity;
        return this;
    }
    
    /**
     * Sets whether the text should be shadowed.
     *
     * @param shadowed Whether the text should be shadowed
     * @return this builder
     */
    @Kapi
    public TextDisplayBuilder shadowed(boolean shadowed) {
        this.shadowed = shadowed;
        return this;
    }
    
    /**
     * Sets whether the text should be see-through.
     *
     * @param seeThrough Whether the text should be see-through
     * @return this builder
     */
    @Kapi
    public TextDisplayBuilder seeThrough(boolean seeThrough) {
        this.seeThrough = seeThrough;
        return this;
    }
    
    /**
     * Sets the default background.
     *
     * @param defaultBackground Whether the default background should be used
     * @return this builder
     */
    @Kapi
    public TextDisplayBuilder defaultBackground(boolean defaultBackground) {
        this.defaultBackground = defaultBackground;
        return this;
    }
    
    /**
     * Sets the alignment of the text.
     *
     * @param alignment The alignment of the text
     * @return this builder
     */
    @Kapi
    public TextDisplayBuilder alignment(final TextDisplay.TextAlignment alignment) {
        this.alignment = alignment;
        return this;
    }
    
    /**
     * Builds the {@link TextDisplayData}.
     *
     * @return A new {@link TextDisplayData} with the current settings
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
