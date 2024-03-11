package me.kyren223.kapi.utility;

import me.kyren223.kapi.KPlugin;
import org.bukkit.event.Listener;

public class EventRegistry {
    
    /**
     * Registers a listener on the server
     * @param listener A new instance of a class that implements Listener
     */
    public void register(Listener listener) {
        KPlugin.get().getServer().getPluginManager().registerEvents(listener, KPlugin.get());
    }
    
}
