package me.kyren223.kapi.items;

import me.kyren223.kapi.utility.Utils;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    
    private final String id;
    private String displayName;
    private List<String> lore;
    
    private ItemBuilder(String id) {
        this.id = id;
        this.lore = new ArrayList<>();
    }
    
    public static ItemBuilder create(String id) {
        return new ItemBuilder(id);
    }
    
    public ItemBuilder displayName(String name) {
        this.displayName = Utils.col(name);
        return this;
    }
    
    public ItemBuilder lore(String... lore) {
        this.lore.addAll(List.of(lore));
        return this;
    }
}
