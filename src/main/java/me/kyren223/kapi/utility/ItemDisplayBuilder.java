/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
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
