/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.commands.builtin;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.commands.ArgumentType;
import io.github.kapimc.kapi.commands.SuggestionCommandContext;
import io.github.kapimc.kapi.data.Result;
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
