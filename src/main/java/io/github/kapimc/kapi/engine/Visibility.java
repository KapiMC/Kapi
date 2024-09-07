/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.engine;

import io.github.kapimc.kapi.annotations.Kapi;

/**
 * Determines how to calculate a {@link Object3D}'s visibility.
 */
@Kapi
public enum Visibility {
    /**
     * The object is always visible.
     */
    @Kapi
    VISIBLE,
    
    /**
     * The object is always hidden.
     */
    @Kapi
    HIDDEN,
    
    /**
     * The object is visible if its parent is visible and hidden if its parent is hidden.
     * <p>
     * If the object has no parent, it is always visible.
     */
    @Kapi
    INHERIT,
}
