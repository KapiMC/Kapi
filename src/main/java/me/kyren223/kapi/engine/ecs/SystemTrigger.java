package me.kyren223.kapi.engine.ecs;

import me.kyren223.kapi.annotations.Kapi;

/**
 * Represents when a system should be executed.
 */
@Kapi
public final class SystemTrigger {
    
    // Built-in event names
    @Kapi public static final String SPAWN_EVENT = "spawn";
    @Kapi public static final String DESPAWN_EVENT = "despawn";
    @Kapi public static final String SCALE_CHANGED_EVENT = "scale_changed";
    
    // Built-in triggers
    @Kapi public static final SystemTrigger ON_SPAWN = event(SPAWN_EVENT);
    @Kapi public static final SystemTrigger ON_DESPAWN = event(DESPAWN_EVENT);
    @Kapi public static final SystemTrigger TICK = interval(1);
    @Kapi public static final SystemTrigger ON_SCALE_CHANGED = event(SCALE_CHANGED_EVENT);
    
    private final int delay;
    private final int period;
    private final String event;
    private final boolean isEvent;
    
    private SystemTrigger(int delay, int period) {
        this.event = null;
        this.delay = delay;
        this.period = period;
        this.isEvent = false;
    }
    
    private SystemTrigger(String event) {
        this.delay = 0;
        this.period = 0;
        this.event = event;
        this.isEvent = true;
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
     * @param delay The delay in ticks
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
     * Gets the delay of this trigger.<br>
     * If the trigger is an event trigger, the value is considered undefined.
     *
     * @return The delay
     */
    @Kapi
    public int getDelay() {
        return delay;
    }
    
    /**
     * Gets the period of this trigger.<br>
     * If the trigger is an event trigger, the value is considered undefined.
     *
     * @return The period
     */
    @Kapi
    public int getPeriod() {
        return period;
    }
    
    /**
     * Gets the event of this trigger.<br>
     * If the trigger is not an event trigger, the value is considered undefined.
     *
     * @return The event's name
     */
    @Kapi
    public String getEvent() {
        return event;
    }
    
    /**
     * Checks if this trigger is an event trigger.
     *
     * @return True if this trigger is an event trigger, false otherwise
     */
    @Kapi
    public boolean isEvent() {
        return isEvent;
    }
}
