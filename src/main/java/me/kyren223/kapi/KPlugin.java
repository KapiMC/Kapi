package me.kyren223.kapi;

import me.kyren223.kapi.utility.CommandRegistry;
import me.kyren223.kapi.utility.EventRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public class KPlugin extends JavaPlugin {
    
    private static KPlugin instance;
    protected CommandRegistry commands;
    protected EventRegistry events;
    
    @Override
    public void onEnable() {
        instance = this;
        commands = new CommandRegistry();
        events = new EventRegistry();
    }
    
    public static KPlugin get() {
        if (instance == null) {
            throw new IllegalStateException("Plugin has not been enabled yet!");
        }
        return instance;
    }
    
}
