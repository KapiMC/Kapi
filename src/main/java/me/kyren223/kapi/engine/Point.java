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

package me.kyren223.kapi.engine;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.engine.renderable.Renderable;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a point in 3D space and a renderable object.
 */
@Kapi
@NullMarked
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




