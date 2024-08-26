/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package me.kyren223.kapi.data;

import me.kyren223.kapi.annotations.Kapi;
import org.jspecify.annotations.NullMarked;

/**
 * Utility class for converting minecraft time durations between different units.<br>
 * Similar to {@link java.util.concurrent.TimeUnit},
 * although this class is specifically designed for minecraft time units.<br>
 * Units range from ticks to days. Ticks are the smallest unit in minecraft.
 */
@Kapi
@NullMarked
public enum TimeUnit {
    TICKS(1),
    SECONDS(20),
    MINUTES(1200),
    MINECRAFT_DAYS(2400),
    HOURS(72000),
    DAYS(1728000),
    ;
    
    private final int scaleToTicks;
    
    TimeUnit(int scaleToTicks) {
        this.scaleToTicks = scaleToTicks;
    }
    
    /**
     * Converts the given duration to ticks.
     *
     * @param duration The duration to convert (in the current unit)
     * @return The duration in ticks
     */
    @Kapi
    public long toTicks(long duration) {
        return convert(duration, scaleToTicks, TICKS.scaleToTicks);
    }
    
    /**
     * Converts the given duration to seconds.
     *
     * @param duration The duration to convert (in the current unit)
     * @return The duration in seconds
     */
    @Kapi
    public long toSeconds(long duration) {
        return convert(duration, scaleToTicks, SECONDS.scaleToTicks);
    }
    
    /**
     * Converts the given duration to minutes.
     *
     * @param duration The duration to convert (in the current unit)
     * @return The duration in minutes
     */
    @Kapi
    public long toMinutes(long duration) {
        return convert(duration, scaleToTicks, MINUTES.scaleToTicks);
    }
    
    /**
     * Converts the given duration to minecraft days.
     * Note: Minecraft days are 20 minutes long.
     *
     * @param duration The duration to convert (in the current unit)
     * @return The duration in minecraft days
     */
    @Kapi
    public long toMinecraftDays(long duration) {
        return convert(duration, scaleToTicks, MINECRAFT_DAYS.scaleToTicks);
    }
    
    /**
     * Converts the given duration to hours.
     *
     * @param duration The duration to convert (in the current unit)
     * @return The duration in hours
     */
    @Kapi
    public long toHours(long duration) {
        return convert(duration, scaleToTicks, HOURS.scaleToTicks);
    }
    
    /**
     * Converts the given duration to days.
     *
     * @param duration The duration to convert (in the current unit)
     * @return The duration in days
     */
    @Kapi
    public long toDays(long duration) {
        return convert(duration, scaleToTicks, DAYS.scaleToTicks);
    }
    
    /**
     * Converts the given duration to the specified unit.
     *
     * @param duration The duration to convert
     * @param unit     The unit to convert to
     * @return The duration in the specified unit
     */
    @Kapi
    public long convert(long duration, TimeUnit unit) {
        return convert(duration, scaleToTicks, unit.scaleToTicks);
    }
    
    /**
     * Converts the given duration to the specified unit.
     *
     * @param duration The duration to convert
     * @param from     The unit of the duration
     * @param to       The unit to convert to
     * @return The duration in the specified unit
     */
    @Kapi
    public static long convert(long duration, TimeUnit from, TimeUnit to) {
        return from.convert(duration, to);
    }
    
    /**
     * Gets the scale of this unit in ticks.
     *
     * @return The scale of this unit in ticks
     */
    @Kapi
    public int getScaleToTicks() {
        return scaleToTicks;
    }
    
    private long convert(long duration, int scale, int destinationScale) {
        return duration * scale / destinationScale;
    }
}
