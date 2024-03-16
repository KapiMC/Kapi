package me.kyren223.kapi.engine.ecs;

public class SystemTrigger {
    
    public static final String SPAWN_EVENT = "spawn";
    public static final String DESPAWN_EVENT = "despawn";
    
    // Built-in triggers
    public static final SystemTrigger SPAWN = event(SPAWN_EVENT);
    public static final SystemTrigger DESPAWN = event(DESPAWN_EVENT);
    public static final SystemTrigger TICK = interval(1);
    
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
    
    public static SystemTrigger interval(int period) {
        return new SystemTrigger(0, period);
    }
    
    public static SystemTrigger delay(int delay) {
        return new SystemTrigger(delay, 0);
    }
    
    public static SystemTrigger delayedInterval(int delay, int period) {
        return new SystemTrigger(delay, period);
    }
    
    public static SystemTrigger event(String event) {
        return new SystemTrigger(event);
    }
    
    public int getDelay() {
        return delay;
    }
    
    public int getPeriod() {
        return period;
    }
    
    public String getEvent() {
        return event;
    }
    
    public boolean isEvent() {
        return isEvent;
    }
}
