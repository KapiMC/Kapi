package me.kyren223.kapi;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.annotations.ScheduledForRefactor;
import me.kyren223.kapi.commands.CommandBuilder;
import me.kyren223.kapi.utility.*;
import org.bukkit.plugin.java.JavaPlugin;

import static me.kyren223.kapi.commands.builtin.IntegerArgumentType.integer;
import static me.kyren223.kapi.commands.builtin.LiteralArgumentType.literal;

/**
 * This class should be extended by the main class of your plugin.<br>
 * Extend this instead of {@link JavaPlugin}<br>
 */
@Kapi
public class KPlugin extends JavaPlugin {
    
    @Kapi
    private static KPlugin instance;
    @Kapi
    @ScheduledForRefactor
    protected CommandRegistry commands;
    @Kapi
    @ScheduledForRefactor
    protected EventRegistry events;
    @Kapi
    @ScheduledForRefactor
    protected boolean debug = false;
    
    @Override
    @Kapi
    public void onEnable() {
        instance = this;
        commands = new CommandRegistry();
        events = new EventRegistry();
        DocumentStore.loadDocuments();
        Log.setPrefix("&8[&bKAPI&8] &r");
        Log.info("KAPI has loaded!");
        
        // TODO delete this, just for testing
        CommandBuilder.create("mycommand")
                .argument(literal("debug")).execute((ctx -> {
                    //mycommand debug
                    Log.info("Debug mode: " + !debug);
                })).build()
                .argument(literal("count")).execute(ctx -> {
                    //mycommand count
                    /*
                    if (ctx.isFinal()) {
                        Log.info("Count is:" + count);
                    } else {
                        // Some shared logic that will also be executed if the path is /mycommand count <amount>
                    }
                     */
                })
                .argument(integer().range(0, 100), "amount").execute(ctx -> {
                    //mycommand count <amount>
                    int amount = ctx.getArg("amount", Integer.class).unwrap();
                    Log.info("Amount: " + amount);
                }).build()
                .build()
                .build().register();
    }
    
    @Override
    @Kapi
    public void onDisable() {
        DocumentStore.saveDocuments();
        Log.info("KAPI has unloaded!");
    }
    
    /**
     * Gets the instance of the plugin
     *
     * @return The instance of the plugin
     * @throws IllegalStateException If the plugin has not been enabled yet
     */
    @Kapi
    public static KPlugin get() {
        if (instance == null) {
            throw new IllegalStateException("Plugin has not been enabled yet!");
        }
        return instance;
    }
    
    @Kapi
    @ScheduledForRefactor
    public boolean isDebug() {
        return debug;
    }
}
