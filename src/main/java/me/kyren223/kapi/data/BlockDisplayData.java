/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package me.kyren223.kapi.data;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.engine.renderable.BlockDisplayRender;
import me.kyren223.kapi.utility.BlockDisplayBuilder;
import org.bukkit.Color;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;

/**
 * Holds the data for a block display.<br>
 * See {@link BlockDisplayRender} for rendering block displays.<br>
 * See {@link BlockDisplayBuilder} for creating new instances.
 */
@Kapi
public class BlockDisplayData extends DisplayData {
    
    private BlockData block;
    
    @Kapi
    public BlockDisplayData(
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
            BlockData block
    ) {
        super(
                transformation, interpolationDuration, viewRange, shadowRadius, shadowStrength, displayWidth,
                displayHeight, interpolationDelay, billboard, glowColorOverride, brightness
        );
        this.block = block;
    }
    
    /**
     * Copy constructor.<br>
     * Note: This does not copy the BlockData.
     *
     * @param data The data to copy
     */
    @Kapi
    public BlockDisplayData(BlockDisplayData data) {
        super(data);
        this.block = data.getBlock();
    }
    
    /**
     * Gets the displayed block.
     *
     * @return the displayed block
     */
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
    public void setBlock(BlockData block) {
        this.block = block;
    }
}
