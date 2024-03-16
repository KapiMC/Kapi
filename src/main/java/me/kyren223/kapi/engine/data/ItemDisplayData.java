package me.kyren223.kapi.engine.data;

import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ALL")
public class ItemDisplayData extends DisplayData {
    
    private @Nullable ItemStack itemStack;
    private @NotNull ItemDisplay.ItemDisplayTransform itemDisplayTransform;
    
    public ItemDisplayData(
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
            @Nullable ItemStack itemStack,
            @NotNull ItemDisplay.ItemDisplayTransform itemDisplayTransform
    ) {
        super(transformation, interpolationDuration, viewRange, shadowRadius, shadowStrength, displayWidth, displayHeight, interpolationDelay, billboard, glowColorOverride, brightness);
        this.itemStack = itemStack;
        this.itemDisplayTransform = itemDisplayTransform;
    }
    
    /**
     * Copy constructor
     *
     * @param data The data to copy
     */
    public ItemDisplayData(@NotNull ItemDisplayData data) {
        super(data);
        this.itemStack = data.getItemStack().clone();
        this.itemDisplayTransform = data.getItemDisplayTransform();
    }
    
    /**
     * Gets the displayed item stack.
     *
     * @return the displayed item stack
     */
    @Nullable
    public ItemStack getItemStack() {
        return itemStack;
    }
    
    /**
     * Sets the displayed item stack.
     *
     * @param item the new item stack
     */
    public void setItemStack(@Nullable ItemStack item) {
        this.itemStack = item;
    }
    
    /**
     * Gets the item display transform for this entity.
     *
     * Defaults to {@link ItemDisplay.ItemDisplayTransform#FIXED}.
     *
     * @return item display transform
     */
    @NotNull
    public ItemDisplay.ItemDisplayTransform getItemDisplayTransform() {
        return itemDisplayTransform;
    }
    
    /**
     * Sets the item display transform for this entity.
     *
     * Defaults to {@link ItemDisplay.ItemDisplayTransform#FIXED}.
     *
     * @param display new display
     */
    public void setItemDisplayTransform(@NotNull ItemDisplay.ItemDisplayTransform display) {
        this.itemDisplayTransform = display;
    }
}
