/*
 * Copyright (c) 2024 Kapi Contributors. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted if the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions, the following disclaimer and the list of contributors.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation and/or
 *    other materials provided with the distribution.
 *
 * 3. The buyer of the "Kapi" API is granted the right to use this software
 *    as a dependency in their own software projects. However, the buyer
 *    may not resell or distribute the "Kapi" API, in whole or in part, to other parties.
 *
 * 4. The buyer may include the "Kapi" API in a "fat jar" along with their own code.
 *    The license for the "fat jar" is at the buyer's discretion and may allow
 *    redistribution of the "fat jar", but the "Kapi" API code inside the "fat jar"
 *    must not be modified.
 *
 * 5. Neither the name "Kapi" nor the names of its contributors may be used to endorse
 *    or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY "Kapi" API, AND ITS CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL "Kapi" API, AND CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Kapi Contributors:
 * - Kyren223
 */

package me.kyren223.kapi.engine.renderable;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.data.ItemDisplayData;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Used to render item displays.
 */
@Kapi
@NullMarked
public class ItemDisplayRender extends ItemDisplayData implements Renderable {
    
    private @Nullable ItemDisplay entity;
    
    @Kapi
    public ItemDisplayRender(
            Transformation transformation,
            int interpolationDuration,
            float viewRange,
            float shadowRadius,
            float shadowStrength,
            float displayWidth,
            float displayHeight,
            int interpolationDelay,
            Display.Billboard billboard,
            Color glowColorOverride,
            Display.Brightness brightness,
            ItemStack itemStack,
            ItemDisplay.ItemDisplayTransform itemDisplayTransform
    ) {
        super(
                transformation, interpolationDuration, viewRange, shadowRadius, shadowStrength,
                displayWidth, displayHeight, interpolationDelay, billboard, glowColorOverride,
                brightness, itemStack, itemDisplayTransform
        );
    }
    
    @Kapi
    public ItemDisplayRender(ItemDisplayData data) {
        super(data);
    }
    
    @Override
    public void spawn(World world, Vector point) {
        if (entity != null) {
            throw new IllegalStateException(
                    "Cannot spawn an item display that has already been spawned");
        }
        entity = world.spawn(point.toLocation(world), ItemDisplay.class);
        entity.setTransformation(getTransformation());
        entity.setInterpolationDuration(getInterpolationDuration());
        entity.setViewRange(getViewRange());
        entity.setShadowRadius(getShadowRadius());
        entity.setShadowStrength(getShadowStrength());
        entity.setDisplayWidth(getDisplayWidth());
        entity.setDisplayHeight(getDisplayHeight());
        entity.setInterpolationDelay(getInterpolationDelay());
        entity.setBillboard(getBillboard());
        entity.setGlowColorOverride(getGlowColorOverride());
        entity.setBrightness(getBrightness());
        entity.setItemStack(getItemStack());
        entity.setItemDisplayTransform(getItemDisplayTransform());
    }
    
    @Override
    public void render(World world, Vector point) {
        // No need to render, the entity will automatically render itself
    }
    
    @Override
    public void despawn(World world, Vector point) {
        if (entity == null) {
            throw new IllegalStateException(
                    "Cannot despawn an item display that has not been spawned");
        }
        entity.remove();
    }
    
    @Kapi
    @Override
    public boolean isSpawned() {
        return entity != null;
    }
    
    @Kapi
    @Override
    public void setTransformation(Transformation transformation) {
        super.setTransformation(transformation);
        if (entity != null) entity.setTransformation(transformation);
    }
    
    @Kapi
    @Override
    public void setInterpolationDuration(int duration) {
        super.setInterpolationDuration(duration);
        if (entity != null) entity.setInterpolationDuration(duration);
    }
    
    @Kapi
    @Override
    public void setViewRange(float range) {
        super.setViewRange(range);
        if (entity != null) entity.setViewRange(range);
    }
    
    @Kapi
    @Override
    public void setShadowRadius(float radius) {
        super.setShadowRadius(radius);
        if (entity != null) entity.setShadowRadius(radius);
    }
    
    @Kapi
    @Override
    public void setShadowStrength(float strength) {
        super.setShadowStrength(strength);
        if (entity != null) entity.setShadowStrength(strength);
    }
    
    @Kapi
    @Override
    public void setDisplayWidth(float width) {
        super.setDisplayWidth(width);
        if (entity != null) entity.setDisplayWidth(width);
    }
    
    @Kapi
    @Override
    public void setDisplayHeight(float height) {
        super.setDisplayHeight(height);
        if (entity != null) entity.setDisplayHeight(height);
    }
    
    @Kapi
    @Override
    public void setInterpolationDelay(int ticks) {
        super.setInterpolationDelay(ticks);
        if (entity != null) entity.setInterpolationDelay(ticks);
    }
    
    @Kapi
    @Override
    public void setBillboard(Display.Billboard billboard) {
        super.setBillboard(billboard);
        if (entity != null) entity.setBillboard(billboard);
    }
    
    @Kapi
    @Override
    public void setGlowColorOverride(Color color) {
        super.setGlowColorOverride(color);
        if (entity != null) entity.setGlowColorOverride(color);
    }
    
    @Kapi
    @Override
    public void setBrightness(Display.Brightness brightness) {
        super.setBrightness(brightness);
        if (entity != null) entity.setBrightness(brightness);
    }
    
    @Kapi
    @Override
    public void setItemStack(@Nullable ItemStack item) {
        super.setItemStack(item);
        if (entity != null) entity.setItemStack(item);
    }
    
    @Kapi
    @Override
    public void setItemDisplayTransform(ItemDisplay.ItemDisplayTransform display) {
        super.setItemDisplayTransform(display);
        if (entity != null) entity.setItemDisplayTransform(display);
    }
    
    @Kapi
    @Override
    public Renderable clone() {
        return new ItemDisplayRender(new ItemDisplayData(this));
    }
    
}
