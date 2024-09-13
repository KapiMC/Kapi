/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.commands.builtin;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.commands.ArgumentType;
import io.github.kapimc.kapi.commands.SuggestionCommandContext;
import io.github.kapimc.kapi.data.Result;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

/**
 * Represents a command argument type for an online player.
 */
@Kapi
@ApiStatus.Experimental
public class PlayerArgumentType implements ArgumentType<Player> {
    
    private boolean suggest;
    
    private PlayerArgumentType() {
        this.suggest = false;
    }
    
    /**
     * Creates a new player argument type.
     *
     * @return the player argument type
     */
    @Kapi
    public static PlayerArgumentType player() {
        return new PlayerArgumentType();
    }
    
    /**
     * Adds suggestions for online players.
     */
    @Kapi
    public void autoSuggest() {
        suggest = true;
    }
    
    @Kapi
    @Override
    public Result<Player,String> parse(List<String> arguments) {
        String name = arguments.remove(0);
        Player player = Bukkit.getPlayer(name);
        if (player == null) {
            return Result.err("Player " + name + " was not found");
        } else {
            return Result.ok(player);
        }
    }
    
    @Kapi
    @Override
    public void getSuggestions(SuggestionCommandContext context) {
        if (!suggest) return;
        for (Player player : context.getSender().getServer().getOnlinePlayers()) {
            context.addSuggestion(player.getName());
        }
    }
}
