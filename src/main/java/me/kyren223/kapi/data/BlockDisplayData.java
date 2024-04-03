package me.kyren223.kapi.data;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.engine.renderable.BlockDisplayRender;
import me.kyren223.kapi.utility.BlockDisplayBuilder;
import org.bukkit.Color;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;

/**
 * Holds the data for a block display.
 * See {@link BlockDisplayRender} for rendering block displays.
 * See {@link BlockDisplayBuilder} for creating new instances.
 */
@Kapi
public class BlockDisplayData extends DisplayData {
    
    private @NotNull BlockData block;
    
    @Kapi
    public BlockDisplayData(
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
            @NotNull BlockData block
    ) {
        super(transformation, interpolationDuration, viewRange, shadowRadius, shadowStrength, displayWidth, displayHeight, interpolationDelay, billboard, glowColorOverride, brightness);
        this.block = block;
    }
    
    /**
     * Copy constructor<br>
     * Note: This does not copy the BlockData.
     *
     * @param data The data to copy
     */
    @Kapi
    public BlockDisplayData(@NotNull BlockDisplayData data) {
        super(data);
        this.block = data.getBlock();
    }
    
    /**
     * Gets the displayed block.
     *
     * @return the displayed block
     */
    @NotNull
    @Kapi
    public BlockData getBlock() {
        return block;
    }
    
    /**
     * Sets the displayed block.
     *
     * @param block the new block
     */
    @Kapi
    public void setBlock(@NotNull BlockData block) {
        this.block = block;
    }
}
