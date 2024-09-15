/*
 * Copyright (c) 2024 Imabanana80
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.utility;

import io.github.kapimc.kapi.annotations.Kapi;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility class that groups {@link org.bukkit.Material}s together based on commonalities .
 */

@Kapi
public enum MaterialGroup{
    
    /**
     * The swords of all 5 basic materials, wood, stone, iron, gold, diamond and netherite
     */
    @Kapi
    SWORDS(Set.of(
        Material.WOODEN_SWORD,
        Material.STONE_SWORD,
        Material.IRON_SWORD,
        Material.GOLDEN_SWORD,
        Material.DIAMOND_SWORD,
        Material.NETHERITE_SWORD
    )),
    /**
     * The pickaxes of all 5 basic materials, wood, stone, iron, gold, diamond and netherite
     */
    @Kapi
    PICKAXES(Set.of(
        Material.WOODEN_PICKAXE,
        Material.STONE_PICKAXE,
        Material.IRON_PICKAXE,
        Material.GOLDEN_PICKAXE,
        Material.DIAMOND_PICKAXE,
        Material.NETHERITE_PICKAXE
    )),
    /**
     * The axes of all 5 basic materials, wood, stone, iron, gold, diamond and netherite
     */
    @Kapi
    AXES(Set.of(
        Material.WOODEN_AXE,
        Material.STONE_AXE,
        Material.IRON_AXE,
        Material.GOLDEN_AXE,
        Material.DIAMOND_AXE,
        Material.NETHERITE_AXE
    )),
    /**
     * The shovels of all 5 basic materials, wood, stone, iron, gold, diamond and netherite
     */
    @Kapi
    SHOVELS(Set.of(
        Material.WOODEN_SHOVEL,
        Material.STONE_SHOVEL,
        Material.IRON_SHOVEL,
        Material.GOLDEN_SHOVEL,
        Material.DIAMOND_SHOVEL,
        Material.NETHERITE_SHOVEL
    )),
    /**
     * The hoes of all 5 basic materials, wood, stone, iron, gold, diamond and netherite
     */
    @Kapi
    HOES(Set.of(
        Material.WOODEN_HOE,
        Material.STONE_HOE,
        Material.IRON_HOE,
        Material.GOLDEN_HOE,
        Material.DIAMOND_HOE,
        Material.NETHERITE_HOE
    )),
    /**
     * All the basic tools not including swords
     */
    @Kapi
    TOOLS(Set.of(
        Material.WOODEN_PICKAXE,
        Material.STONE_PICKAXE,
        Material.IRON_PICKAXE,
        Material.GOLDEN_PICKAXE,
        Material.DIAMOND_PICKAXE,
        Material.NETHERITE_PICKAXE,
        Material.WOODEN_AXE,
        Material.STONE_AXE,
        Material.IRON_AXE,
        Material.GOLDEN_AXE,
        Material.DIAMOND_AXE,
        Material.NETHERITE_AXE,
        Material.WOODEN_SHOVEL,
        Material.STONE_SHOVEL,
        Material.IRON_SHOVEL,
        Material.GOLDEN_SHOVEL,
        Material.DIAMOND_SHOVEL,
        Material.NETHERITE_SHOVEL,
        Material.WOODEN_HOE,
        Material.STONE_HOE,
        Material.IRON_HOE,
        Material.GOLDEN_HOE,
        Material.DIAMOND_HOE,
        Material.NETHERITE_HOE
    )),
    /**
     * Items that are intended for use in combat.
     */
    @Kapi
    WEAPONS(Set.of(
        Material.WOODEN_SWORD,
        Material.STONE_SWORD,
        Material.IRON_SWORD,
        Material.GOLDEN_SWORD,
        Material.DIAMOND_SWORD,
        Material.NETHERITE_SWORD,
        Material.WOODEN_AXE,
        Material.STONE_AXE,
        Material.IRON_AXE,
        Material.GOLDEN_AXE,
        Material.DIAMOND_AXE,
        Material.NETHERITE_AXE,
        Material.TRIDENT,
        Material.BOW,
        Material.CROSSBOW
        // TODO: add mace when updating to 1.21+
    )),
    /**
     * {@link #WEAPONS Weapons} that are used to deal damage within a player's reach distance
     */
    @Kapi
    MELEE_WEAPONS(Set.of(
        Material.WOODEN_SWORD,
        Material.STONE_SWORD,
        Material.IRON_SWORD,
        Material.GOLDEN_SWORD,
        Material.DIAMOND_SWORD,
        Material.NETHERITE_SWORD,
        Material.WOODEN_AXE,
        Material.STONE_AXE,
        Material.IRON_AXE,
        Material.GOLDEN_AXE,
        Material.DIAMOND_AXE,
        Material.NETHERITE_AXE,
        Material.TRIDENT
        // TODO: add mace when updating to 1.21+
    )),
    /**
     * {@link #WEAPONS Weapons} Weapons that launch (as) projectiles and are more effective at range
     */
    @Kapi
    RANGED_WEAPONS(Set.of(
        Material.BOW,
        Material.CROSSBOW,
        Material.TRIDENT
        
    )),
    /**
     * Projectiles are thrown ore launched using {@link #RANGED_WEAPONS ranged weapons} or by hand
     */
    @Kapi
    PROJECTILES(Set.of(
        Material.TRIDENT,
        Material.ARROW,
        Material.TIPPED_ARROW,
        Material.SPECTRAL_ARROW,
        // TODO: Add wind burst when updating to 1.21+
        Material.SNOWBALL,
        Material.EGG
    )),
    /**
     * {@link #PROJECTILES Projectiles} that deal damage players
     */
    @Kapi
    DAMAGE_PROJECTILES(Set.of(
        Material.TRIDENT,
        Material.ARROW,
        Material.TIPPED_ARROW,
        Material.SPECTRAL_ARROW
        // TODO: Add wind burst when updating to 1.21+
    )),
    /**
     * {@link #PROJECTILES Projectiles} that do not deal damage to players
     */
    @Kapi
    SAFE_PROJECTILES(Set.of(
        Material.SNOWBALL,
        Material.EGG
    )),
    
    
    /**
     * Both the {@link #STONE_ORES Stone} and {@link #DEEPSLATE_ORES Deepslate} varients of all ores,
     * as well as the {@link #NETHER_ORES nether} ores including ancient debris
     */
    @Kapi
    ORES(Set.of(
        Material.COAL_ORE,
        Material.IRON_ORE,
        Material.COPPER_ORE,
        Material.GOLD_ORE,
        Material.EMERALD_ORE,
        Material.LAPIS_ORE,
        Material.REDSTONE_ORE,
        Material.DIAMOND_ORE,
        Material.DEEPSLATE_COAL_ORE,
        Material.DEEPSLATE_IRON_ORE,
        Material.DEEPSLATE_COPPER_ORE,
        Material.DEEPSLATE_GOLD_ORE,
        Material.DEEPSLATE_EMERALD_ORE,
        Material.DEEPSLATE_LAPIS_ORE,
        Material.DEEPSLATE_REDSTONE_ORE,
        Material.DEEPSLATE_DIAMOND_ORE,
        Material.NETHER_GOLD_ORE,
        Material.NETHER_QUARTZ_ORE,
        Material.ANCIENT_DEBRIS
    )),
    /**
     * Stone varients of all ores
     */
    @Kapi
    STONE_ORES(Set.of(
        Material.COAL_ORE,
        Material.IRON_ORE,
        Material.COPPER_ORE,
        Material.GOLD_ORE,
        Material.EMERALD_ORE,
        Material.LAPIS_ORE,
        Material.REDSTONE_ORE,
        Material.DIAMOND_ORE
    )),
    /**
     * Deepslate varients of all ores
     */
    @Kapi
    DEEPSLATE_ORES(Set.of(
        Material.DEEPSLATE_COAL_ORE,
        Material.DEEPSLATE_IRON_ORE,
        Material.DEEPSLATE_COPPER_ORE,
        Material.DEEPSLATE_GOLD_ORE,
        Material.DEEPSLATE_EMERALD_ORE,
        Material.DEEPSLATE_LAPIS_ORE,
        Material.DEEPSLATE_REDSTONE_ORE,
        Material.DEEPSLATE_DIAMOND_ORE
    )),
    /**
     * All ores that naturally generate in the nether
     */
    @Kapi
    NETHER_ORES(Set.of(
        Material.NETHER_GOLD_ORE,
        Material.NETHER_QUARTZ_ORE,
        Material.ANCIENT_DEBRIS
    )),
    /**
     * ALl {@link #ORES ores} that typically require smelting to obtain the useful product or drop raw varients
     */
    @Kapi
    SMELTABLE_ORES(Set.of(
        Material.IRON_ORE,
        Material.COPPER_ORE,
        Material.GOLD_ORE,
        Material.DEEPSLATE_IRON_ORE,
        Material.DEEPSLATE_COPPER_ORE,
        Material.DEEPSLATE_GOLD_ORE,
        Material.NETHER_GOLD_ORE,
        Material.NETHER_QUARTZ_ORE,
        Material.ANCIENT_DEBRIS
    )),
    /**
     * All {@link #ORES ores} that typically do not require smelting to obtain the useful product and do not drop raw varients
     */
    @Kapi
    UNSMELTABLE_ORES(Set.of(
        Material.COAL_ORE,
        Material.EMERALD_ORE,
        Material.LAPIS_ORE,
        Material.REDSTONE_ORE,
        Material.DIAMOND_ORE,
        Material.DEEPSLATE_COAL_ORE,
        Material.DEEPSLATE_EMERALD_ORE,
        Material.DEEPSLATE_LAPIS_ORE,
        Material.DEEPSLATE_REDSTONE_ORE,
        Material.DEEPSLATE_DIAMOND_ORE
    )),
    /**
     * The raw drops of all {@link #ORES ores}
     */
    @Kapi
    RAW_RESOURCES(Set.of(
        Material.COAL,
        Material.RAW_COPPER,
        Material.LAPIS_LAZULI,
        Material.RAW_GOLD,
        Material.RAW_IRON,
        Material.REDSTONE,
        Material.DIAMOND,
        Material.EMERALD,
        Material.QUARTZ,
        Material.GOLD_NUGGET,
        Material.ANCIENT_DEBRIS
    )),
    /**
     * The smelted (if needed) varients of {@link #RAW_RESOURCES raw resources}, includes netherite scrap, not netherite ingot
     */
    @Kapi
    INGOTS(Set.of(
        Material.IRON_INGOT,
        Material.GOLD_INGOT,
        Material.COPPER_INGOT,
        Material.NETHERITE_SCRAP
    )),
    /**
     * All the final useful products of {@link #ORES ores}
     */
    @Kapi
    ORE_PRODUCTS(Set.of(
        Material.IRON_INGOT,
        Material.GOLD_INGOT,
        Material.COPPER_INGOT,
        Material.NETHERITE_INGOT,
        Material.COAL,
        Material.LAPIS_LAZULI,
        Material.REDSTONE,
        Material.DIAMOND,
        Material.EMERALD,
        Material.QUARTZ
    ));
    
    private final Set<Material> materials;
    
    MaterialGroup(Set<Material> materials){
        this.materials = new HashSet<>(materials);
    }
    
    /**
     * @return the group of materials as a set
     */
    @Kapi
    public Set<Material> get(){
        return materials;
    }
}
