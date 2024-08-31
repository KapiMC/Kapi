/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.utility;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.data.BlockDisplayData;
import org.bukkit.block.data.BlockData;

/**
 * A builder for {@link BlockDisplayData}.
 */
@Kapi
public class BlockDisplayBuilder extends DisplayBuilder<BlockDisplayBuilder> {
    
    private final BlockData block;
    
    private BlockDisplayBuilder(BlockData block) {
        super();
        this.block = block;
    }
    
    /**
     * Creates a new builder for the given block.
     * <p>
     * All other values will be set to their default values, which are:
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
     *     <li>billboard = Display.Billboard.FIXED;
     *     <li>glowColorOverride = Color.WHITE;
     *     <li>brightness = new Display.Brightness(14, 14); // Max brightness
     * </ul>
     *
     * @param block the block to display
     * @return a new builder
     */
    @Kapi
    public static BlockDisplayBuilder create(final BlockData block) {
        return new BlockDisplayBuilder(block);
    }
    
    /**
     * @return a new {@link BlockDisplayData} with the current settings
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
