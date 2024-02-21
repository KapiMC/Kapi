package me.kyren223.kapi;

import me.kyren223.kapi.utility.CommandRegistry;
import me.kyren223.kapi.utility.EventRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public final class KPlugin extends JavaPlugin {
    
    public static KPlugin i;
    private CommandRegistry commands;
    private EventRegistry events;
    
    @Override
    public void onEnable() {
        i = this;
        commands = new CommandRegistry();
        events = new EventRegistry();
    }

    @Override
    public void onDisable() {
    }
}
