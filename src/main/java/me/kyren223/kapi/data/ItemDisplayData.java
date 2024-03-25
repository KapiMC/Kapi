package me.kyren223.kapi.data;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.engine.renderable.ItemDisplayRender;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Holds the data for an item display.
 * See {@link ItemDisplayRender} for rendering particles.
 */
@Kapi
public class ItemDisplayData extends DisplayData {
    
    private @Nullable ItemStack itemStack;
    private @NotNull ItemDisplay.ItemDisplayTransform itemDisplayTransform;
    
    @Kapi
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
     * Copy constructor<br>
     * Note: Does not clone the item stack
     *
     * @param data The data to copy
     */
    @Kapi
    public ItemDisplayData(@NotNull ItemDisplayData data) {
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
    @NotNull
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
    public void setItemDisplayTransform(@NotNull ItemDisplay.ItemDisplayTransform display) {
        this.itemDisplayTransform = display;
    }
}
