package me.kyren223.kapi.utility;

import me.kyren223.kapi.KPlugin;
import org.bukkit.event.Listener;

public class EventRegistry {
    
    public void register(Listener listener) {
        KPlugin.i.getServer().getPluginManager().registerEvents(listener, KPlugin.i);
    }
}
