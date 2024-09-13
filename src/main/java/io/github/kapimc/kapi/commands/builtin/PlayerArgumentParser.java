/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands.builtin;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.commands.ArgumentParser;
import io.github.kapimc.kapi.commands.ArgumentRepresentation;
import io.github.kapimc.kapi.data.Option;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.AnnotatedType;
import java.util.Deque;
import java.util.List;

/**
 * Argument parser for {@link Player}.
 */
@Kapi
public class PlayerArgumentParser implements ArgumentParser<Player> {
    
    /**
     * The priority of this parser.
     */
    @Kapi
    public static final int PRIORITY = 21000;
    
    /**
     * The singleton instance of this parser.
     */
    @Kapi
    public static final PlayerArgumentParser INSTANCE = new PlayerArgumentParser();
    
    private PlayerArgumentParser() {
    }
    
    @Override
    public Option<Player> parse(AnnotatedType type, CommandSender sender, Deque<String> args) {
        String name = args.pollFirst();
        if (name == null) {
            return Option.none();
        }
        Player player = Bukkit.getPlayer(name);
        return Option.of(player);
    }
    
    @Override
    public List<String> getSuggestions(AnnotatedType type, CommandSender sender) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }
    
    @Override
    public int getPriority(AnnotatedType type) {
        return PRIORITY;
    }
    
    @Override
    public Option<ArgumentRepresentation> getRepresentation(AnnotatedType type) {
        return Option.of(ArgumentRepresentation.of("<", "player", ">"));
    }
}
