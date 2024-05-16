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

package me.kyren223.kapi.engine.ecs;

import me.kyren223.kapi.annotations.Kapi;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents when a system should be executed.
 */
@Kapi
@NullMarked
public final class SystemTrigger {
    
    // Built-in event names
    @Kapi
    public static final String SPAWN_EVENT = "spawn";
    @Kapi
    public static final String DESPAWN_EVENT = "despawn";
    
    @Kapi
    public static final String SCALE_CHANGED_EVENT = "scale_changed";
    @Kapi
    public static final String ENTITY_COLLISION_EVENT = "entity_collision";
    
    // Built-in triggers
    @Kapi
    public static final SystemTrigger ON_SPAWN = event(SPAWN_EVENT);
    @Kapi
    public static final SystemTrigger ON_DESPAWN = event(DESPAWN_EVENT);
    @Kapi
    public static final SystemTrigger TICK = interval(1);
    
    @Kapi
    public static final SystemTrigger ON_SCALE_CHANGED = event(SCALE_CHANGED_EVENT);
    @Kapi
    public static final SystemTrigger ON_ENTITY_COLLISION = event(ENTITY_COLLISION_EVENT);
    
    private final int delay;
    private final int period;
    private final @Nullable String event;
    
    private SystemTrigger(int delay, int period) {
        this.event = null;
        this.delay = delay;
        this.period = period;
    }
    
    private SystemTrigger(String event) {
        this.delay = 0;
        this.period = 0;
        this.event = event;
    }
    
    /**
     * Creates a trigger that executes the system every {@code period} ticks.
     *
     * @param period Every how many ticks the system should be executed
     * @return A new trigger
     */
    @Kapi
    public static SystemTrigger interval(int period) {
        return new SystemTrigger(0, period);
    }
    
    /**
     * Creates a trigger that executes the system once, after {@code delay} ticks.
     *
     * @param delay The delay in ticks
     * @return A new trigger
     */
    @Kapi
    public static SystemTrigger delay(int delay) {
        return new SystemTrigger(delay, 0);
    }
    
    /**
     * Creates a trigger that executes the system once,
     * after {@code delay} ticks,
     * and then every {@code period} ticks.
     *
     * @param delay  The delay in ticks
     * @param period Every how many ticks the system should be executed
     * @return A new trigger
     */
    @Kapi
    public static SystemTrigger delayedInterval(int delay, int period) {
        return new SystemTrigger(delay, period);
    }
    
    /**
     * Creates a trigger that executes the system
     * when the specified event is triggered.
     *
     * @param event The event to listen for
     * @return A new trigger
     */
    @Kapi
    public static SystemTrigger event(String event) {
        return new SystemTrigger(event);
    }
    
    /**
     * Gets the delay of this trigger.
     *
     * @return The delay
     * @throws IllegalStateException if the trigger is an event trigger
     */
    @Kapi
    public int getDelay() {
        if (event != null) {
            throw new IllegalStateException("This trigger is an event trigger");
        }
        return delay;
    }
    
    /**
     * Gets the period of this trigger.
     *
     * @return The period
     * @throws IllegalStateException if the trigger is an event trigger
     */
    @Kapi
    public int getPeriod() {
        if (event != null) {
            throw new IllegalStateException("This trigger is an event trigger");
        }
        return period;
    }
    
    /**
     * Gets the event of this trigger.
     *
     * @return The event's name
     * @throws IllegalStateException If the trigger is not an event trigger
     */
    @Kapi
    public String getEvent() {
        if (event == null) {
            throw new IllegalStateException("This trigger is not an event trigger");
        }
        return event;
    }
    
    /**
     * Checks if this trigger is an event trigger.
     *
     * @return True if this trigger is an event trigger, false otherwise
     */
    @Kapi
    public boolean isEvent() {
        return event != null;
    }
}
