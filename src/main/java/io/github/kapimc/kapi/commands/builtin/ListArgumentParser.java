/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands.builtin;

import io.github.kapimc.kapi.commands.ArgumentParser;
import io.github.kapimc.kapi.commands.ArgumentRepresentation;
import io.github.kapimc.kapi.data.Option;
import org.bukkit.command.CommandSender;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Parameter;
import java.util.Deque;
import java.util.List;

public class ListArgumentParser implements ArgumentParser<List<?>> {
    @Override
    public Option<List<?>> parse(AnnotatedType type, String paramName, Deque<String> args, CommandSender sender) {
        // TODO: get type name
        return Option.none();
//        if (!(type instanceof AnnotatedParameterizedType paramType)) {
//            return Option.none();
//        }
//        Class<?> componentType = paramType.getAnnotatedActualTypeArguments()[0].get
//        ArgumentParser<?> parser = ArgumentRegistry.getInstance().get(componentType)
//            .expect("Failed to get parser for component type " + componentType.getName());
//
//        List<Object> parsedArgs = new ArrayList<>();
//        while (true) {
//            Option<?> parsedArg = parser.parse(args, sender, paramType);
//            if (parsedArg.isNone()) {
//                break;
//            }
//            parsedArgs.add(parsedArg.unwrap());
//        }
//
//        return Option.some(parsedArgs);
    }
    
    @Override
    public List<String> getSuggestions(
        AnnotatedType type, String paramName, Deque<String> args, CommandSender sender
    ) {
        // TODO: get type name
        return List.of();
    }
    
    @Override
    public int getPriority(AnnotatedType type) {
        return 50;
    }
    
    @Override
    public Option<ArgumentRepresentation> getRepresentation(AnnotatedType type, String paramName) {
        // TODO: get type name
        return Option.some(ArgumentRepresentation.of("TODO"));
    }
}
