/*
 * Copyright (c) 2024 Kapi Contributors. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted if the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions, the following disclaimer and the list of contributors.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation and/or
 *    other materials provided with the distribution.
 *
 * 3. The buyer of the "Kapi" API is granted the right to use this software
 *    as a dependency in their own software projects. However, the buyer
 *    may not resell or distribute the "Kapi" API, in whole or in part, to other parties.
 *
 * 4. The buyer may include the "Kapi" API in a "fat jar" along with their own code.
 *    The license for the "fat jar" is at the buyer's discretion and may allow
 *    redistribution of the "fat jar", but the "Kapi" API code inside the "fat jar"
 *    must not be modified.
 *
 * 5. Neither the name "Kapi" nor the names of its contributors may be used to endorse
 *    or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY "Kapi" API, AND ITS CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL "Kapi" API, AND CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Kapi Contributors:
 * - Kyren223
 */

package me.kyren223.kapi.data;

import me.kyren223.kapi.annotations.Kapi;

/**
 * Utility class for converting minecraft time durations between different units.<br>
 * Similar to {@link java.util.concurrent.TimeUnit},
 * although this class is specifically designed for minecraft time units.<br>
 * Units range from ticks to days. Ticks are the smallest unit in minecraft.
 */
@Kapi
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
     * @param unit The unit to convert to
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
     * @param from The unit of the duration
     * @param to The unit to convert to
     * @return The duration in the specified unit
     */
    @Kapi
    public static long convert (long duration, TimeUnit from, TimeUnit to) {
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
