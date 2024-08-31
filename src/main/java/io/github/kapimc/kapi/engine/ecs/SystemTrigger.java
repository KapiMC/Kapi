/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.engine.ecs;

import io.github.kapimc.kapi.annotations.Kapi;
import org.jspecify.annotations.Nullable;

/**
 * Represents when a system should be executed.
 */
@Kapi
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
