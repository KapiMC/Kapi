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
import me.kyren223.kapi.data.ItemDisplayData;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A builder for {@link ItemDisplayData}.
 */
@Kapi
@NullMarked
public class ItemDisplayBuilder extends DisplayBuilder<ItemDisplayBuilder> {
    
    private @Nullable ItemStack item;
    private ItemDisplay.ItemDisplayTransform itemDisplayTransform;
    
    private ItemDisplayBuilder() {
        itemDisplayTransform = ItemDisplay.ItemDisplayTransform.NONE;
    }
    
    /**
     * Creates a new builder (with item being null).<br>
     * <br>
     * All values will be set to their default values, which are:
     * <ul>
     *     <li>item = null;
     *     <li>itemDisplayTransform = NONE;
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
    public static ItemDisplayBuilder create() {
        return new ItemDisplayBuilder();
    }
    
    /**
     * Creates a new builder for the given item.<br>
     * ItemDisplayTransform will set to NONE by default,
     * for other values, see {@link #create()}.
     *
     * @param item the item to display
     * @return a new builder
     */
    @Kapi
    public static ItemDisplayBuilder create(ItemStack item) {
        return new ItemDisplayBuilder().item(item);
    }
    
    /**
     * Sets the item to display.
     *
     * @param item the item to display
     * @return this builder
     */
    @Kapi
    public ItemDisplayBuilder item(final ItemStack item) {
        this.item = item;
        return this;
    }
    
    /**
     * Sets the item display transform.
     *
     * @param itemDisplayTransform the item display transform
     * @return this builder
     */
    @Kapi
    public ItemDisplayBuilder itemDisplayTransform(
            ItemDisplay.ItemDisplayTransform itemDisplayTransform
    ) {
        this.itemDisplayTransform = itemDisplayTransform;
        return this;
    }
    
    /**
     * Builds the {@link ItemDisplayData}.
     *
     * @return A new {@link ItemDisplayData} with the current settings
     */
    @Kapi
    public ItemDisplayData build() {
        return new ItemDisplayData(
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
                item,
                itemDisplayTransform
        );
    }
}
