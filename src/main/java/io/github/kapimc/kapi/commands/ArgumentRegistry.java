/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.commands.builtin.ArrayArgumentParser;
import io.github.kapimc.kapi.data.Option;
import io.github.kapimc.kapi.utility.Utils;
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
        if (clazz.isArray()) {
            return Option.some(parsers.get(Object[].class));
        }
        return Option.of(parsers.get(clazz));
    }
    
    private void addBuiltInParsers() {
        // TODO: add built-in parsers
        add(String.class, ArgumentParser.of(
            (args, sender, type) -> Option.of(args.pollFirst()),
            (args, sender, type) -> List.of(),
            p -> Option.some("text"), 0
        ));
        
        add(int.class, ArgumentParser.of(
            (args, sender, type) -> Option.of(args.peek()).andThen(Utils::parseInt).inspect(i -> args.pop()),
            (args, sender, type) -> List.of(),
            p -> Option.some("integer"), 200
        ));
        add(Integer.class, ArgumentParser.of(
            (args, sender, type) -> Option.of(args.peek()).andThen(Utils::parseInt).inspect(i -> args.pop()),
            (args, sender, type) -> List.of(),
            p -> Option.some("integer"), 200
        ));
        
        add(double.class, ArgumentParser.of(
            (args, sender, type) -> Option.of(args.pollFirst()).andThen(Utils::parseDouble),
            (args, sender, type) -> List.of(),
            p -> Option.some("double"), 100
        ));
        add(Double.class, ArgumentParser.of(
            (args, sender, type) -> Option.of(args.pollFirst()).andThen(Utils::parseDouble),
            (args, sender, type) -> List.of(),
            p -> Option.some("double"), 100
        ));
        
        add(Object[].class, new ArrayArgumentParser());
    }
    
}
