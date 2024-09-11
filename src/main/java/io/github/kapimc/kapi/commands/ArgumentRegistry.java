/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.commands.builtin.*;
import io.github.kapimc.kapi.data.Option;
import org.bukkit.Material;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

/**
 * The argument registry.
 * Used to register argument parsers for specific classes (types).
 */
@Kapi
public class ArgumentRegistry {
    
    private static @Nullable ArgumentRegistry instance;
    
    /**
     * @return the instance of the argument registry
     */
    @Kapi
    public static ArgumentRegistry getInstance() {
        if (instance == null) {
            instance = new ArgumentRegistry();
            instance.addBuiltInParsers();
        }
        return instance;
    }
    
    private final HashMap<Class<?>,ArgumentParser<?>> parsers;
    
    private ArgumentRegistry() {
        parsers = new HashMap<>();
    }
    
    /**
     * Sets the parser for the given class.
     * <p>
     * This overrides any existing parsers of the given class.
     *
     * @param clazz  the class to set the parser for
     * @param parser the parser to set
     */
    @Kapi
    public void set(Class<?> clazz, ArgumentParser<?> parser) {
        parsers.put(clazz, parser);
    }
    
    /**
     * Adds the parser for the given class.
     * <p>
     * This does not override any existing parsers of the given class.
     *
     * @param clazz  the class to set the parser for
     * @param parser the parser to set
     * @return true if the parser was added, otherwise false
     */
    @Kapi
    public boolean add(Class<?> clazz, ArgumentParser<?> parser) {
        return parsers.put(clazz, parser) == null;
    }
    
    public Option<ArgumentParser<?>> get(Class<?> clazz) {
        if (parsers.containsKey(clazz)) {
            return Option.some(parsers.get(clazz));
        }
        if (clazz.isArray()) {
            return Option.some(parsers.get(Object[].class));
        }
        if (clazz.isEnum()) {
            return Option.some(parsers.get(Enum.class));
        }
        return Option.of(parsers.get(clazz));
    }
    
    private void addBuiltInParsers() {
        // Built-in Java specific parsers
        add(String.class, StringArgumentParser.INSTANCE);
        add(boolean.class, BooleanArgumentParser.INSTANCE);
        add(Boolean.class, BooleanArgumentParser.INSTANCE);
        add(double.class, DoubleArgumentParser.INSTANCE);
        add(Double.class, DoubleArgumentParser.INSTANCE);
        add(float.class, FloatArgumentParser.INSTANCE);
        add(Float.class, FloatArgumentParser.INSTANCE);
        add(long.class, LongArgumentParser.INSTANCE);
        add(Long.class, LongArgumentParser.INSTANCE);
        add(int.class, IntegerArgumentParser.INSTANCE);
        add(Integer.class, IntegerArgumentParser.INSTANCE);
        add(short.class, ShortArgumentParser.INSTANCE);
        add(Short.class, ShortArgumentParser.INSTANCE);
        add(Enum.class, EnumArgumentParser.INSTANCE);
        
        // Built-in collection parsers
        add(Object[].class, ArrayArgumentParser.INSTANCE);
        add(List.class, ListArgumentParser.INSTANCE);
        
        // Built-in Minecraft specific parsers
        add(Material.class, MaterialArgumentParser.INSTANCE);
    }
    
}
