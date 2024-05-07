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

package me.kyren223.kapi.engine.ecs;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.engine.Object3D;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.function.Consumer;

/**
 * A collection of built-in systems that can be applied to an {@link Object3D}.
 */
@Kapi
public class System {
    
    private System() {
        throw new AssertionError("System should not be instantiated");
    }
    
    /**
     * Moves the object by its velocity.<br>
     * <br>
     * The object must have a {@value Component#VELOCITY} component.
     * The type of the velocity can be
     * either {@link Vector3fc}, {@link Vector3f} or {@link Vector}.<br>
     * <br>
     * If the component is not present or is not of the correct type,
     * this system will silently do nothing.
     */
    @Kapi
    public static final Consumer<Object3D> VELOCITY = instance -> {
        if (!instance.has(Component.VELOCITY)) return;
        Object object = instance.get(Component.VELOCITY);
        if (object instanceof Vector3fc velocity) {
            instance.getMutableTransform().translate(velocity);
        } else if (object instanceof Vector velocity) {
            instance.getMutableTransform().translate(velocity.toVector3f());
        }
        // else do nothing
    };
    
    /**
     * Modifies the object's velocity by its acceleration.<br>
     * <br>
     * The object must have a {@value Component#VELOCITY} component
     * and an {@value Component#ACCELERATION} component.<br>
     *
     * The type of both the velocity and acceleration can be
     * either {@link Vector3fc}, {@link Vector3f} or {@link Vector}.<br>
     * <br>
     * If either the velocity or acceleration component is not present
     * or are not of the correct type, this system will silently do nothing.
     */
    @Kapi
    public static final Consumer<Object3D> ACCELERATION = instance -> {
        if (!instance.has(Component.ACCELERATION)) return;
        if (!instance.has(Component.VELOCITY)) return;
        
        Object object = instance.get(Component.ACCELERATION);
        Vector3fc acceleration;
        Vector3f velocity;
        
        if (object instanceof Vector3fc acc) {
            acceleration = acc;
        } else if (object instanceof Vector acc) {
            acceleration = acc.toVector3f();
        } else return;
        
        object = instance.get(Component.VELOCITY);
        if (object instanceof Vector3fc vel) {
            velocity = new Vector3f(vel);
        } else if (object instanceof Vector vel) {
            velocity = vel.toVector3f();
        } else return;
        
        instance.set(Component.VELOCITY, velocity.add(acceleration));
    };
}
