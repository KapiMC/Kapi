package me.kyren223.kapi.utility;

import me.kyren223.kapi.annotations.Kapi;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;

public abstract class DisplayBuilder<T extends DisplayBuilder<T>> {
    
    protected @NotNull Transformation transformation;
    protected int interpolationDuration;
    protected float viewRange;
    protected float shadowRadius;
    protected float shadowStrength;
    protected float displayWidth;
    protected float displayHeight;
    protected int interpolationDelay;
    protected @NotNull Display.Billboard billboard;
    protected @NotNull Color glowColorOverride;
    protected @NotNull Display.Brightness brightness;
    
    @Kapi
    public T viewRange(float viewRange) {
        this.viewRange = viewRange;
        return (T) this;
    }
    
}
