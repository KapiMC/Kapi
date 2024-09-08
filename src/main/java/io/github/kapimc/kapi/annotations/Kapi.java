/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.annotations;

import java.lang.annotation.*;

/**
 * This annotation is used to mark the public interface of Kapi.
 * Do not use this annotation in your own code.
 * <p>
 * All code that does not have this annotation is
 * subject to change without notice and may be undocumented.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(
    {
        ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
        ElementType.FIELD, ElementType.METHOD, ElementType.RECORD_COMPONENT
    }
)
public @interface Kapi {

}
