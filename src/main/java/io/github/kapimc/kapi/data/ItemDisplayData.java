/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.data;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.engine.renderable.ItemDisplayRender;
import io.github.kapimc.kapi.utility.ItemDisplayBuilder;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.jspecify.annotations.Nullable;

/**
 * Holds the data for an item display.
 * See {@link ItemDisplayBuilder} for creating new instances.
 * See {@link ItemDisplayRender} for rendering particles.
 */
@Kapi
public non-sealed class ItemDisplayData extends DisplayData {
    
    private @Nullable ItemStack itemStack;
    private ItemDisplay.ItemDisplayTransform itemDisplayTransform;
    
    /**
     * Creates a new ItemDisplayData instance.
     * It's recommended to use {@link ItemDisplayBuilder} to easily create this class.
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
     * @param itemStack             the displayed entity item stack
     * @param itemDisplayTransform  the item display transform for this display entity
     */
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
    
    private ItemDisplayData(ItemDisplayData data) {
        super(data);
        this.itemStack = data.getItemStack() != null ? data.getItemStack().clone() : null;
        this.itemDisplayTransform = data.getItemDisplayTransform();
    }
    
    /**
     * @return the displayed entity item stack
     */
    @Kapi
    @Nullable
    public ItemStack getItemStack() {
        return itemStack;
    }
    
    /**
     * @param item the new displayed entity item stack
     */
    @Kapi
    public void setItemStack(@Nullable ItemStack item) {
        this.itemStack = item;
    }
    
    /**
     * The way to display the item, some items can be displayed differently
     * depending on if they are equipped as an armor, held in the hand or displayed in a GUI.
     *
     * @return the item display transform for this entity
     */
    @Kapi
    public ItemDisplay.ItemDisplayTransform getItemDisplayTransform() {
        return itemDisplayTransform;
    }
    
    /**
     * The way to display the item, some items can be displayed differently
     * depending on if they are equipped as an armor, held in the hand or displayed in a GUI.
     *
     * @param display the new item display transform for this entity
     */
    @Kapi
    public void setItemDisplayTransform(ItemDisplay.ItemDisplayTransform display) {
        this.itemDisplayTransform = display;
    }
    
    /**
     * @return a deep copy of this ItemDisplayData
     */
    @Kapi
    @Override
    public ItemDisplayData clone() {
        return new ItemDisplayData(this);
    }
}
