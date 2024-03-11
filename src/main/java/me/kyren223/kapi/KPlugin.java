package me.kyren223.kapi;

import me.kyren223.kapi.utility.CommandRegistry;
import me.kyren223.kapi.utility.DocumentStore;
import me.kyren223.kapi.utility.EventRegistry;
import me.kyren223.kapi.utility.Log;
import org.bukkit.plugin.java.JavaPlugin;

public class KPlugin extends JavaPlugin {
    
    private static KPlugin instance;
    protected CommandRegistry commands;
    protected EventRegistry events;
    protected boolean debug = false;
    
    @Override
    public void onEnable() {
        instance = this;
        commands = new CommandRegistry();
        events = new EventRegistry();
        DocumentStore.loadDocuments();
        Log.setPrefix("&8[&bKAPI&8] &r");
        Log.info("KAPI has loaded!");
    }
    
    @Override
    public void onDisable() {
        DocumentStore.saveDocuments();
        Log.info("KAPI has unloaded!");
    }
    
    public static KPlugin get() {
        if (instance == null) {
            throw new IllegalStateException("Plugin has not been enabled yet!");
        }
        return instance;
    }
    
    public boolean isDebug() {
        return debug;
    }
}
