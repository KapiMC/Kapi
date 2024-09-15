/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.sql;

import io.github.kapimc.kapi.annotations.Kapi;

/**
 * A query to be executed on a {@link SqliteDB}.
 *
 * @param sql    the query to execute
 * @param values the arguments to use in the query
 */
@Kapi
public record SqlQuery(String sql, Object... values) {
    
}
