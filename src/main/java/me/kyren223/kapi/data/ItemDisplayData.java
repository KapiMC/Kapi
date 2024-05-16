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
import me.kyren223.kapi.engine.renderable.ItemDisplayRender;
import me.kyren223.kapi.utility.ItemDisplayBuilder;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Holds the data for an item display.
 * See {@link ItemDisplayRender} for rendering particles.
 * See {@link ItemDisplayBuilder} for creating new instances.
 */
@Kapi
@NullMarked
public class ItemDisplayData extends DisplayData {
    
    private @Nullable ItemStack itemStack;
    private ItemDisplay.ItemDisplayTransform itemDisplayTransform;
    
    @Kapi
    public ItemDisplayData(
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
            @Nullable ItemStack itemStack,
            ItemDisplay.ItemDisplayTransform itemDisplayTransform
    ) {
        super(
                transformation, interpolationDuration, viewRange, shadowRadius, shadowStrength, displayWidth,
                displayHeight, interpolationDelay, billboard, glowColorOverride, brightness
        );
        this.itemStack = itemStack;
        this.itemDisplayTransform = itemDisplayTransform;
    }
    
    /**
     * Copy constructor.<br>
     * Note: Does not clone the item stack.
     *
     * @param data The data to copy
     */
    @Kapi
    public ItemDisplayData(ItemDisplayData data) {
        super(data);
        this.itemStack = data.getItemStack();
        this.itemDisplayTransform = data.getItemDisplayTransform();
    }
    
    /**
     * Gets the displayed item stack.
     *
     * @return the displayed item stack
     */
    @Kapi
    @Nullable
    public ItemStack getItemStack() {
        return itemStack;
    }
    
    /**
     * Sets the displayed item stack.
     *
     * @param item the new item stack
     */
    @Kapi
    public void setItemStack(@Nullable ItemStack item) {
        this.itemStack = item;
    }
    
    /**
     * Gets the item display transform for this entity.<br>
     * Defaults to {@link ItemDisplay.ItemDisplayTransform#FIXED}.
     *
     * @return item display transform
     */
    @Kapi
    public ItemDisplay.ItemDisplayTransform getItemDisplayTransform() {
        return itemDisplayTransform;
    }
    
    /**
     * Sets the item display transform for this entity.<br>
     * Defaults to {@link ItemDisplay.ItemDisplayTransform#FIXED}.
     *
     * @param display new display
     */
    @Kapi
    public void setItemDisplayTransform(ItemDisplay.ItemDisplayTransform display) {
        this.itemDisplayTransform = display;
    }
}
