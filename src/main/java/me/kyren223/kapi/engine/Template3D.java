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
import me.kyren223.kapi.data.Option;
import me.kyren223.kapi.data.Pair;
import me.kyren223.kapi.engine.ecs.SystemTrigger;
import org.bukkit.Location;
import org.bukkit.World;
import org.joml.Matrix4f;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Represents a 3D object template.
 */
@Kapi
@NullMarked
public class Template3D {
    private final List<Point> points;
    private final HashMap<String,Pair<Matrix4f,Template3D>> children;
    private final HashMap<String,@Nullable Object> components;
    private final HashMap<String,List<Consumer<Object3D>>> events;
    private final List<Pair<SystemTrigger,Consumer<Object3D>>> tasks;
    
    
    @Kapi
    public Template3D(List<Point> points) {
        this.points = points;
        this.children = new HashMap<>();
        this.components = new HashMap<>();
        this.events = new HashMap<>();
        this.tasks = new ArrayList<>();
    }
    
    /**
     * Gets the points of this template.<br>
     * Modifying the returned list will affect the template's points.<br>
     * <br>
     * To add a point, use {@link #addPoint(Point)}<br>
     * To remove a point, use {@link #removePointIf(Predicate)}<br>
     *
     * @return A stream of points
     */
    @Kapi
    public Stream<Point> getPoints() {
        return points.stream();
    }
    
    /**
     * Adds a point to this template.<br>
     * <br>
     * If you want to add multiple points and know them in advance,
     * consider using {@link #addPoints(List)} or {@link #addPoints(Point...)}<br>
     * This can improve performance by reducing the amount of resizing the list has to do.<br>
     *
     * @param point The point to add
     */
    @Kapi
    public void addPoint(Point point) {
        points.add(point);
    }
    
    /**
     * Adds multiple points to this template.<br>
     * Also see {@link #addPoint(Point)} and {@link #addPoints(Point...)}<br>
     * <br>
     * This can improve performance by reducing the amount of resizing the list has to do.<br>
     *
     * @param points The points to add
     */
    @Kapi
    public void addPoints(List<Point> points) {
        this.points.addAll(points);
    }
    
    /**
     * Adds multiple points to this template.<br>
     * Also see {@link #addPoint(Point)} and {@link #addPoints(List)}<br>
     * <br>
     * This can improve performance by reducing the amount of resizing the list has to do.<br>
     *
     * @param points The points to add
     */
    @Kapi
    public void addPoints(Point... points) {
        this.points.addAll(Arrays.asList(points));
    }
    
    /**
     * Removes all points that the predicate returns true for.
     *
     * @param predicate The predicate
     */
    @Kapi
    public void removePointIf(Predicate<Point> predicate) {
        points.removeIf(predicate);
    }
    
    /**
     * Adds a child to this template.<br>
     * See {@link #addChild(String, Template3D, Matrix4f)} for adding a child with a transform.<br>
     *
     * @param name  The name of the child (used to retrieve it later)
     * @param child The child's template
     */
    @Kapi
    public void addChild(String name, Template3D child) {
        children.put(name, Pair.of(new Matrix4f(), child));
    }
    
    /**
     * Adds a child to this template with a transform.<br>
     * See {@link #addChild(String, Template3D)} for adding a child without a transform.<br>
     *
     * @param name      The name of the child (used to retrieve it later)
     * @param child     The child's template
     * @param transform The transform of the child relative to this template
     */
    @Kapi
    public void addChild(
            String name, Template3D child, Matrix4f transform
    ) {
        children.put(name, Pair.of(transform, child));
    }
    
    /**
     * Removes a child from this template.
     *
     * @param name The name of the child to remove
     * @return The removed child's template
     */
    @Kapi
    public Template3D removeChild(String name) {
        return children.remove(name).getSecond();
    }
    
    /**
     * Removes all children that the predicate returns true for.
     *
     * @param predicate The predicate
     */
    @Kapi
    public void removeChildIf(
            Predicate<Map.Entry<String,Pair<Matrix4f,Template3D>>> predicate
    ) {
        children.entrySet().removeIf(predicate);
    }
    
    /**
     * Gets the children of this template.
     *
     * @return A stream of the children names, transforms, and templates
     */
    @Kapi
    public Stream<Map.Entry<String,Pair<Matrix4f,Template3D>>> getChildren() {
        return children.entrySet().stream();
    }
    
    /**
     * Gets a child by name.
     *
     * @param name The name of the child
     * @return The child's template and it's relative transform or null if the child doesn't exist
     */
    @Kapi
    public Option<Pair<Matrix4f,Template3D>> getChild(String name) {
        return Option.ofNullable(children.get(name));
    }
    
    
    /**
     * Gets the default value of a component.<br>
     * This value will be set on all instances of this template when they are spawned.
     *
     * @param key   The name of the component
     * @param value The default value of the component
     */
    @Kapi
    public void setDefault(String key, @Nullable Object value) {
        components.put(key, value);
    }
    
    /**
     * Adds a system to this template.
     *
     * @param trigger The trigger to activate the system
     * @param system  The system
     * @return This template for chaining
     */
    @Kapi
    public Template3D addSystem(
            SystemTrigger trigger, Consumer<Object3D> system
    ) {
        if (trigger.isEvent()) {
            events.computeIfAbsent(trigger.getEvent(), k -> new ArrayList<>()).add(system);
        } else {
            tasks.add(Pair.of(trigger, system));
        }
        return this;
    }
    
    // Package-private
    HashMap<String,List<Consumer<Object3D>>> getEvents() {
        Consumer<Object3D> setDefaultsSystem = instance -> components.forEach(instance::set);
        events.computeIfAbsent(SystemTrigger.SPAWN_EVENT, k -> new ArrayList<>())
              .add(setDefaultsSystem);
        return events;
    }
    
    List<Pair<SystemTrigger,Consumer<Object3D>>> getTasks() {
        return tasks;
    }
    
    /**
     * Creates a new instance of this template at the specified world with the given transform.<br>
     * See {@link #newInstance(World, Matrix4f)} for spawning the object without a parent.<br>
     * See {@link #newInstance(Location, Object3D)} for spawning the object using a location.
     *
     * @param world     The world of the object
     * @param transform The transform of the object
     * @param parent    The parent object
     * @return A new Object3D instance
     */
    @Kapi
    public Object3D newInstance(
            World world, Matrix4f transform, Object3D parent
    ) {
        return new Object3D(this, world, transform, parent);
    }
    
    /**
     * Creates a new instance of this template at the specified world with the given transform.<br>
     * See {@link #newInstance(World, Matrix4f, Object3D)} for spawning the object with a
     * parent.<br>
     * See {@link #newInstance(Location)} for spawning the object using a location.
     *
     * @param world     The world of the object
     * @param transform The transform of the object
     * @return A new Object3D instance
     */
    @Kapi
    public Object3D newInstance(World world, Matrix4f transform) {
        return new Object3D(this, world, transform, null);
    }
    
    /**
     * Creates a new instance of this template at the specified location.<br>
     * See {@link #newInstance(Location)} for spawning the object without a parent.<br>
     * See {@link #newInstance(World, Matrix4f, Object3D)} for spawning the object using a
     * transform.
     *
     * @param location The location to spawn the object at
     * @param parent   The parent object
     * @return A new Object3D instance
     * @throws IllegalArgumentException If the world of the location is null
     */
    @Kapi
    public Object3D newInstance(Location location, Object3D parent) {
        World world = location.getWorld();
        if (world == null) {
            throw new IllegalArgumentException("Location world cannot be null");
        }
        return new Object3D(
                this,
                world,
                new Matrix4f().translate(location.toVector().toVector3f()),
                parent
        );
    }
    
    /**
     * Creates a new instance of this template at the specified location.<br>
     * See {@link #newInstance(Location, Object3D)} for spawning the object with a parent.<br>
     * See {@link #newInstance(World, Matrix4f)} for spawning the object using a transform.
     *
     * @param location The location to spawn the object at
     * @return A new Object3D instance
     * @throws IllegalArgumentException If the world of the location is null
     */
    @Kapi
    public Object3D newInstance(Location location) {
        World world = location.getWorld();
        if (world == null) {
            throw new IllegalArgumentException("Location world cannot be null");
        }
        return new Object3D(
                this,
                world,
                new Matrix4f().translate(location.toVector().toVector3f()),
                null
        );
    }
}
