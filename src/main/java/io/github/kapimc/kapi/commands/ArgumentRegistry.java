/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.data.Option;
import io.github.kapimc.kapi.data.Result;
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
        return Option.of(parsers.get(clazz));
    }
    
    private void addBuiltInParsers() {
        // TODO: add built-in parsers
        add(String.class, ArgumentParser.of(
            args -> !args.isEmpty(),
            (args, sender, parameter) -> Result.ok(args.removeFirst()),
            (args, sender, parameter) -> List.of(),
            0
        ));
    }
}
