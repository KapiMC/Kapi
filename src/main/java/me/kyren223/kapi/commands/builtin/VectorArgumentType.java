/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package me.kyren223.kapi.commands.builtin;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.commands.ArgumentType;
import me.kyren223.kapi.commands.SuggestionCommandContext;
import me.kyren223.kapi.data.Result;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

/**
 * Represents a command argument type for a vector.
 */
@Kapi
@ApiStatus.Experimental
public class VectorArgumentType implements ArgumentType<Vector> {
    
    @Override
    public Result<Vector,String> parse(List<String> arguments) {
        String xarg = arguments.remove(0);
        if (arguments.isEmpty()) {
            return Result.err("Missing y and z components");
        }
        String yarg = arguments.remove(0);
        if (arguments.isEmpty()) {
            return Result.err("Missing z component");
        }
        String zarg = arguments.remove(0);
        
        double x;
        double y;
        double z;
        
        try {
            x = Double.parseDouble(xarg);
        } catch (NumberFormatException e) {
            return Result.err("Invalid X: " + xarg);
        }
        
        try {
            y = Double.parseDouble(yarg);
        } catch (NumberFormatException e) {
            return Result.err("Invalid Y: " + yarg);
        }
        
        try {
            z = Double.parseDouble(zarg);
        } catch (NumberFormatException e) {
            return Result.err("Invalid Z: " + zarg);
        }
        
        return Result.ok(new Vector(x, y, z));
    }
    
    @Override
    public void getSuggestions(SuggestionCommandContext context) {
        // Do nothing
    }
}
