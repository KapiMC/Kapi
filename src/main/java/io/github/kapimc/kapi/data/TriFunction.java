/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.data;

import io.github.kapimc.kapi.annotations.Kapi;

/**
 * A function that accepts three arguments and produces a result.
 *
 * @param <T> the first argument type
 * @param <U> the second argument type
 * @param <V> the third argument type
 * @param <R> the return type
 */
@Kapi
public interface TriFunction<T, U, V, R> {
    /**
     * Applies this function to the given arguments.
     *
     * @param t the first argument
     * @param u the second argument
     * @param v the third argument
     * @return the result
     */
    @Kapi
    R apply(T t, U u, V v);
}
