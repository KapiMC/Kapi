/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands;

import io.github.kapimc.kapi.data.Option;
import org.bukkit.command.CommandSender;

import java.lang.reflect.AnnotatedType;
import java.util.Deque;
import java.util.List;

public interface ArgumentParser<T> {
    
    Option<T> parse(AnnotatedType type, String paramName, Deque<String> args, CommandSender sender);
    
    List<String> getSuggestions(AnnotatedType type, String paramName, Deque<String> args, CommandSender sender);
    
    int getPriority(AnnotatedType type);
    
    Option<ArgumentRepresentation> getRepresentation(AnnotatedType type, String paramName);
    
}
