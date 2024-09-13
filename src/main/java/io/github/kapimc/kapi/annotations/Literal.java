/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.annotations;

import java.lang.annotation.*;

/**
 * Used on a {@link String} type, to make the string a literal argument.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
@Kapi
public @interface Literal {
    
    /**
     * @return the value of the literal argument
     */
    @Kapi
    String value();
    
    /**
     * @return the aliases of the literal argument
     */
    @Kapi
    String[] aliases() default {};
    
    /**
     * @return true for case sensitivity, false for case insensitivity
     */
    @Kapi
    boolean caseSensitive() default false;
    
}
