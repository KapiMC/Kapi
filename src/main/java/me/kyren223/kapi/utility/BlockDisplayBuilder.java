package me.kyren223.kapi.utility;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.data.BlockDisplayData;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

/**
 * A builder for {@link BlockDisplayData}.
 */
@Kapi
public class BlockDisplayBuilder extends DisplayBuilder<BlockDisplayBuilder> {
    
    private final @NotNull BlockData block;
    
    private BlockDisplayBuilder(@NotNull BlockData block) {
        super();
        this.block = block;
    }
    
    /**
     * Creates a new builder for the given block.<br>
     * <br>
     * All other values will be set to their default values, which are:<br>
     * <ul>
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
     * <ul>
     *
     * @param block the block to display
     * @return a new builder
     */
    @Kapi
    public static BlockDisplayBuilder create(@NotNull BlockData block) {
        return new BlockDisplayBuilder(block);
    }
    
    /**
     * Builds the {@link BlockDisplayData}.
     *
     * @return A new {@link BlockDisplayData} with the current settings
     */
    @Kapi
    public BlockDisplayData build() {
        return new BlockDisplayData(
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
                block
        );
    }
}
