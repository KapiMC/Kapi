/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.data;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.engine.renderable.BlockDisplayRender;
import io.github.kapimc.kapi.utility.BlockDisplayBuilder;
import org.bukkit.Color;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;

/**
 * Holds the data for a block display.
 * See {@link BlockDisplayBuilder} for creating new instances.
 * See {@link BlockDisplayRender} for rendering block displays.
 */
@Kapi
public final class BlockDisplayData extends DisplayData {
    
    private BlockData block;
    
    /**
     * Creates a new BlockDisplayData instance.
     * It's recommended to use {@link BlockDisplayBuilder} to easily create this class.
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
     * @param block                 the displayed entity block
     */
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
    
    private BlockDisplayData(BlockDisplayData data) {
        super(data);
        this.block = data.getBlock().clone();
    }
    
    /**
     * @return the displayed entity block
     */
    @Kapi
    public BlockData getBlock() {
        return block;
    }
    
    /**
     * @param block the new displayed entity block
     */
    @Kapi
    public void setBlock(BlockData block) {
        this.block = block;
    }
    
    /**
     * @return a deep copy of this BlockDisplayData
     */
    @Kapi
    @Override
    public BlockDisplayData clone() {
        return new BlockDisplayData(this);
    }
}
