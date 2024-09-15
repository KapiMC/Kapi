/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.sql;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.data.Result;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A query to be executed on a database.
 *
 * @param sql    the query to execute
 * @param values the arguments to use in the query
 */
@Kapi
public record SqlQuery(String sql, Object... values) {
    
    /**
     * Executes the query on the given database.
     *
     * @param db the database to execute the query on
     * @return the result of the query
     * @see SqliteDB#executeUpdate(String, Object...)
     */
    @Kapi
    public Result<Integer,SQLException> executeUpdate(SqliteDB db) {
        return db.executeUpdate(sql, values);
    }
    
    /**
     * Executes the query on the given database.
     *
     * @param db the database to execute the query on
     * @return the result of the query
     * @see SqliteDB#executeQuery(String, Object...)
     */
    @Kapi
    public Result<ResultSet,SQLException> executeQuery(SqliteDB db) {
        return db.executeQuery(sql, values);
    }
}
