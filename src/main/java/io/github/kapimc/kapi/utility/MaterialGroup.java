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
    
    
    @Kapi
    SWORDS(Set.of(
        Material.WOODEN_SWORD,
        Material.STONE_SWORD,
        Material.IRON_SWORD,
        Material.DIAMOND_SWORD,
        Material.GOLDEN_SWORD,
        Material.NETHERITE_SWORD
    )),
    
    @Kapi
    PICKAXES(Set.of(
        Material.WOODEN_PICKAXE,
        Material.STONE_PICKAXE,
        Material.IRON_PICKAXE,
        Material.GOLDEN_PICKAXE,
        Material.DIAMOND_PICKAXE,
        Material.NETHERITE_PICKAXE
    )),
    
    @Kapi
    AXES(Set.of(
        Material.WOODEN_AXE,
        Material.STONE_AXE,
        Material.IRON_AXE,
        Material.GOLDEN_AXE,
        Material.DIAMOND_AXE,
        Material.NETHERITE_AXE
    )),
    
    @Kapi
    SHOVELS(Set.of(
        Material.WOODEN_SHOVEL,
        Material.STONE_SHOVEL,
        Material.IRON_SHOVEL,
        Material.GOLDEN_SHOVEL,
        Material.DIAMOND_SHOVEL,
        Material.NETHERITE_SHOVEL
    )),
    
    @Kapi
    HOES(Set.of(
        Material.WOODEN_HOE,
        Material.STONE_HOE,
        Material.IRON_HOE,
        Material.GOLDEN_HOE,
        Material.DIAMOND_HOE,
        Material.NETHERITE_HOE));
    
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
