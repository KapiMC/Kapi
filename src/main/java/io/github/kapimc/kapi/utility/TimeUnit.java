/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.utility;

import io.github.kapimc.kapi.annotations.Kapi;

/**
 * Utility class for converting between different Minecraft time units.
 * <p>
 * The server's TPS (ticks per second) is assumed to be 20,
 * which is usually the case, but it may be lower in cases of lag.
 * <p>
 * {@link java.util.concurrent.TimeUnit TimeUnit} is similar to this class,
 * but it shouldn't be used because it doesn't support Minecraft time units.
 */
@Kapi
public enum TimeUnit {
    
    /**
     * The smallest unit of time in Minecraft, equivalent to 0.05 {@link #SECONDS}.
     * Everything else is based on this unit.
     */
    @Kapi
    TICKS(1),
    
    /**
     * Real-world seconds, equivalent to 20 {@link #TICKS}.
     */
    @Kapi
    SECONDS(20),
    
    /**
     * Real-world minutes, equivalent to 60 {@link #SECONDS}.
     */
    @Kapi
    MINUTES(1200),
    
    /**
     * Minecraft days, equivalent to 20 {@link #MINUTES}.
     */
    @Kapi
    MINECRAFT_DAYS(24000),
    
    /**
     * Real-world hours, equivalent to 60 {@link #MINUTES}.
     */
    @Kapi
    HOURS(72000),
    
    /**
     * Real-world days, equivalent to 24 {@link #HOURS}.
     */
    @Kapi
    DAYS(1728000),
    ;
    
    private final int scaleToTicks;
    
    TimeUnit(int scaleToTicks) {
        this.scaleToTicks = scaleToTicks;
    }
    
    /**
     * Converts the given duration to {@link #TICKS}.
     *
     * @param duration the duration to convert (in the current unit)
     * @return the duration in ticks
     */
    @Kapi
    public long toTicks(long duration) {
        return convert(duration, scaleToTicks, TICKS.scaleToTicks);
    }
    
    /**
     * Converts the given duration to {@link #SECONDS}.
     *
     * @param duration the duration to convert (in the current unit)
     * @return the duration in seconds
     */
    @Kapi
    public long toSeconds(long duration) {
        return convert(duration, scaleToTicks, SECONDS.scaleToTicks);
    }
    
    /**
     * Converts the given duration to {@link #MINUTES}.
     *
     * @param duration the duration to convert (in the current unit)
     * @return the duration in minutes
     */
    @Kapi
    public long toMinutes(long duration) {
        return convert(duration, scaleToTicks, MINUTES.scaleToTicks);
    }
    
    /**
     * Converts the given duration to {@link #MINECRAFT_DAYS}.
     * Minecraft days are 20 {@link #MINUTES} long.
     *
     * @param duration the duration to convert (in the current unit)
     * @return the duration in minecraft days
     */
    @Kapi
    public long toMinecraftDays(long duration) {
        return convert(duration, scaleToTicks, MINECRAFT_DAYS.scaleToTicks);
    }
    
    /**
     * Converts the given duration to {@link #HOURS}.
     *
     * @param duration the duration to convert (in the current unit)
     * @return the duration in hours
     */
    @Kapi
    public long toHours(long duration) {
        return convert(duration, scaleToTicks, HOURS.scaleToTicks);
    }
    
    /**
     * Converts the given duration to {@link #DAYS}.
     *
     * @param duration the duration to convert (in the current unit)
     * @return the duration in days
     */
    @Kapi
    public long toDays(long duration) {
        return convert(duration, scaleToTicks, DAYS.scaleToTicks);
    }
    
    /**
     * Converts the given duration in the current unit to the specified unit.
     *
     * @param duration the duration to convert (in the current unit)
     * @param unit     the unit to convert to
     * @return the duration in the specified unit
     */
    @Kapi
    public long convert(long duration, TimeUnit unit) {
        return convert(duration, this.scaleToTicks, unit.scaleToTicks);
    }
    
    /**
     * Converts the given duration to the specified unit.
     *
     * @param duration the duration to convert
     * @param from     the unit of the duration
     * @param to       the unit to convert to
     * @return The duration in the specified unit
     */
    @Kapi
    public static long convert(long duration, TimeUnit from, TimeUnit to) {
        return from.convert(duration, to);
    }
    
    /**
     * @return the scale of this unit in ticks
     */
    @Kapi
    public int getScaleToTicks() {
        return scaleToTicks;
    }
    
    private long convert(long duration, int scale, int destinationScale) {
        return duration * scale / destinationScale;
    }
}
