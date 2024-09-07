/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.utility;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.data.ItemDisplayData;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * A builder for {@link ItemDisplayData}.
 */
@Kapi
public final class ItemDisplayBuilder extends DisplayBuilder<ItemDisplayBuilder> {
    
    private @Nullable ItemStack item;
    private ItemDisplay.ItemDisplayTransform itemDisplayTransform;
    
    private ItemDisplayBuilder() {
        itemDisplayTransform = ItemDisplay.ItemDisplayTransform.NONE;
    }
    
    /**
     * Creates a new item display builder (with item being null).
     * <p>
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
     *     <li>billboard = Display.Billboard.FIXED;
     *     <li>glowColorOverride = Color.WHITE;
     *     <li>brightness = new Display.Brightness(14, 14); // Max brightness
     * </ul>
     *
     * @return a new builder
     */
    @Kapi
    public static ItemDisplayBuilder create() {
        return new ItemDisplayBuilder();
    }
    
    /**
     * Creates a new builder with the given item.
     * See {@link #create()} for more information like default values.
     *
     * @param item the item to display
     * @return a new builder
     */
    @Kapi
    public static ItemDisplayBuilder create(ItemStack item) {
        return new ItemDisplayBuilder().item(item);
    }
    
    /**
     * @param item the item to display
     * @return this, for chaining
     */
    @Kapi
    public ItemDisplayBuilder item(final ItemStack item) {
        this.item = item;
        return this;
    }
    
    /**
     * The way to display the item, some items can be displayed differently
     * depending on if they are equipped as an armor, held in the hand or displayed in a GUI.
     *
     * @param itemDisplayTransform the item display transform
     * @return this, for chaining
     */
    @Kapi
    public ItemDisplayBuilder itemDisplayTransform(ItemDisplay.ItemDisplayTransform itemDisplayTransform) {
        this.itemDisplayTransform = itemDisplayTransform;
        return this;
    }
    
    /**
     * @return a new {@link ItemDisplayData} with the current settings
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
