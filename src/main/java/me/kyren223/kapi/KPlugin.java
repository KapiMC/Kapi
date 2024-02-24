package me.kyren223.kapi;

import me.kyren223.kapi.utility.CommandRegistry;
import me.kyren223.kapi.utility.EventRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public class KPlugin extends JavaPlugin {
    
    public static KPlugin i;
    protected CommandRegistry commands;
    protected EventRegistry events;
    
    @Override
    public void onEnable() {
        i = this;
        commands = new CommandRegistry();
        events = new EventRegistry();
    }
    
}
