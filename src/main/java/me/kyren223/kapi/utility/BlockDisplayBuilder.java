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
        this.block = block;
    }
    
    /**
     * Creates a new builder for the given block.
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
