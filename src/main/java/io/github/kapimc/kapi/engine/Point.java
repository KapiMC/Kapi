/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.engine;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.engine.renderable.Renderable;
import org.bukkit.util.Vector;

/**
 * Represents a point in 3D space and a renderable object.
 */
@Kapi
public class Point {
    private double x;
    private double y;
    private double z;
    private Renderable renderable;
    
    @Kapi
    public Point(double x, double y, double z, final Renderable renderable) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.renderable = renderable;
    }
    
    @Kapi
    public Point(final Vector vector, final Renderable renderable) {
        this(vector.getX(), vector.getY(), vector.getZ(), renderable);
    }
    
    /**
     * Copy constructor<br>
     * Note: Clones the renderable object
     *
     * @param point The point to copy
     */
    @Kapi
    public Point(final Point point) {
        this(point.x, point.y, point.z, point.getRenderable().clone());
    }
    
    @Kapi
    public double getX() {
        return x;
    }
    
    @Kapi
    public void setX(double x) {
        this.x = x;
    }
    
    @Kapi
    public double getY() {
        return y;
    }
    
    @Kapi
    public void setY(double y) {
        this.y = y;
    }
    
    @Kapi
    public double getZ() {
        return z;
    }
    
    @Kapi
    public void setZ(double z) {
        this.z = z;
    }
    
    /**
     * Returns the point as a vector
     *
     * @return A newly created vector with the point's coordinates
     */
    @Kapi
    public Vector getVector() {
        return new Vector(x, y, z);
    }
    
    /**
     * Sets the point's coordinates to the vector's coordinates<br>
     * Note: Changing the vector after calling this method will not change the point
     *
     * @param vector The vector to set the point to
     */
    @Kapi
    public void setVector(Vector vector) {
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
    }
    
    @Kapi
    public Renderable getRenderable() {
        return renderable;
    }
    
    @Kapi
    public void setRenderable(Renderable renderable) {
        this.renderable = renderable;
    }
}




