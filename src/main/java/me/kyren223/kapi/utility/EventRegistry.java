package me.kyren223.kapi.utility;

import me.kyren223.kapi.core.Kplugin;
import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.annotations.ScheduledForRefactor;
import org.bukkit.event.Listener;

/**
 * A utility class for registering event listeners.
 */
@Kapi
@ScheduledForRefactor
public class EventRegistry {
    
    /**
     * Registers a listener on the server
     * @param listener A new instance of a class that implements Listener
     */
    @Kapi
    @ScheduledForRefactor
    public void register(Listener listener) {
        Kplugin.get().getServer().getPluginManager().registerEvents(listener, Kplugin.get());
    }
    
}
