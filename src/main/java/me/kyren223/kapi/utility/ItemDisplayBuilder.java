package me.kyren223.kapi.utility;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.data.ItemDisplayData;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A builder for {@link ItemDisplayData}.
 */
@Kapi
public class ItemDisplayBuilder extends DisplayBuilder<ItemDisplayBuilder> {
    
    private @Nullable ItemStack item;
    private @NotNull ItemDisplay.ItemDisplayTransform itemDisplayTransform;
    
    private ItemDisplayBuilder() {
        itemDisplayTransform = ItemDisplay.ItemDisplayTransform.NONE;
    }
    
    /**
     * Creates a new builder (with item being null).<br>
     * Item will be set to null by default.<br>
     * ItemDisplayTransform will set to NONE by default.
     *
     * @return a new builder
     */
    @Kapi
    public static ItemDisplayBuilder create() {
        return new ItemDisplayBuilder();
    }
    
    /**
     * Creates a new builder for the given item.<br>
     * ItemDisplayTransform will set to NONE by default.
     *
     * @param item the item to display
     * @return a new builder
     */
    @Kapi
    public static ItemDisplayBuilder create(@NotNull ItemStack item) {
        return new ItemDisplayBuilder().item(item);
    }
    
    /**
     * Sets the item to display.
     *
     * @param item the item to display
     * @return this builder
     */
    @Kapi
    public ItemDisplayBuilder item(@NotNull ItemStack item) {
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
    public ItemDisplayBuilder itemDisplayTransform(@NotNull ItemDisplay.ItemDisplayTransform itemDisplayTransform) {
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
