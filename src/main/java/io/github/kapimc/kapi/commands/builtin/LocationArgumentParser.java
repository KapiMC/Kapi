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
import io.github.kapimc.kapi.utility.Utils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import java.lang.reflect.AnnotatedType;
import java.util.Deque;
import java.util.List;

/**
 * Argument parser for {@link Location}.
 */
@Kapi
public class LocationArgumentParser implements ArgumentParser<Location> {
    
    /**
     * The priority of this parser.
     */
    @Kapi
    public static final int PRIORITY = 22000;
    
    /**
     * Singleton instance of this parser.
     */
    @Kapi
    public static final LocationArgumentParser INSTANCE = new LocationArgumentParser();
    
    private LocationArgumentParser() {
    }
    
    
    @Override
    public Option<Location> parse(AnnotatedType type, CommandSender sender, Deque<String> args) {
        String xString = args.pollFirst();
        if (xString == null) {
            return Option.none();
        }
        String yString = args.pollFirst();
        if (yString == null) {
            return Option.none();
        }
        String zString = args.pollFirst();
        if (zString == null) {
            return Option.none();
        }
        
        Option<Double> x, y, z;
        if (!(sender instanceof Entity entity)) {
            x = Utils.parseDouble(xString);
            y = Utils.parseDouble(yString);
            z = Utils.parseDouble(zString);
            if (x.isNone() || y.isNone() || z.isNone()) {
                return Option.none();
            }
            return Option.some(new Location(null, x.unwrap(), y.unwrap(), z.unwrap()));
        }
        Location location = entity.getLocation();
        x = parseRelative(xString, location.getX());
        y = parseRelative(yString, location.getY());
        z = parseRelative(zString, location.getZ());
        if (x.isNone() || y.isNone() || z.isNone()) {
            return Option.none();
        }
        return Option.some(new Location(
            location.getWorld(), x.unwrap(), y.unwrap(), z.unwrap(),
            location.getYaw(), location.getPitch()
        ));
    }
    
    @Override
    public List<String> getSuggestions(AnnotatedType type, CommandSender sender) {
        if (sender instanceof Entity) {
            return List.of("~");
        }
        return List.of();
    }
    
    @Override
    public int getPriority(AnnotatedType type) {
        return PRIORITY;
    }
    
    @Override
    public Option<ArgumentRepresentation> getRepresentation(AnnotatedType type) {
        return Option.some(ArgumentRepresentation.of("<", "location", ">"));
    }
    
    private Option<Double> parseRelative(String string, double base) {
        if (string.startsWith("~")) {
            return Utils.parseDouble(string.substring(1)).map(d -> base + d);
        }
        return Utils.parseDouble(string);
    }
}
