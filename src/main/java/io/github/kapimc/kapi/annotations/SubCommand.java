/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.annotations;

import io.github.kapimc.kapi.commands.ArgumentRegistry;
import io.github.kapimc.kapi.commands.Command;
import org.bukkit.command.CommandSender;

import java.lang.annotation.*;

/**
 * Used to annotate methods within a {@link Command} class to define subcommands.
 * <p>
 * For a subcommand to be valid, it must have the following requirements:
 * <ul>
 *     <li>It must be annotated with this annotation
 *     <li>It must be a public method
 *     <li>It must return nothing (void)
 *     <li>It must have at least one parameter
 *     <li>It must have a {@link CommandSender} (or subclass of it) as the first parameter
 *     <li>All parameters (except the first) must have a type that is registered with {@link ArgumentRegistry}
 * </ul>
 * <p>
 * Subcommands are methods that will be automatically
 * called when the command's input matches all the method's parameters.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Kapi
public @interface SubCommand {
}
