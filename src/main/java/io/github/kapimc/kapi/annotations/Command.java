/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.annotations;

import java.lang.annotation.*;

/**
 * Marks a class as a command.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Kapi
public @interface Command {
    
    /**
     * The command's name.
     * <p>
     * The regex {@code [\w\d/]+} produces valid command names,
     * but it may not cover all possible names.
     *
     * @return the command name.
     */
    String name();
    
}
