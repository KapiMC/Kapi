/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.sql;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.core.KapiPlugin;
import io.github.kapimc.kapi.data.Result;
import io.github.kapimc.kapi.utility.TaskBuilder;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.io.File;
import java.sql.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A wrapper for a SQLite database.
 * <p>
 * To more easily make SQL queries,
 * see {@link QueryBuilder}.
 */
@Kapi
public class SqliteDB {
    
    /**
     * The extension of a SQLite database file.
     */
    @Kapi
    public static final String DB_EXTENSION = ".db";
    
    private final String url;
    
    private SqliteDB(String url) {
        this.url = url;
    }
    
    /**
     * Creates a new SqliteDB instance.
     * <p>
     * The path of the database is relative to the data folder,
     * the path will be appended with ".db" if it doesn't end with it.
     * For example, a path of "kapi" will be converted to "kapi.db",
     * and the actual file will be located at {@code <data folder>/kapi.db},
     * where {@code <data folder>} is {@link KapiPlugin#getDataFolder()}.
     *
     * @param path the path of the database
     * @return a new SqliteDB instance
     */
    @Kapi
    public static SqliteDB create(String path) {
        File dataFolder = KapiPlugin.get().getDataFolder();
        path = path.endsWith(DB_EXTENSION) ? path : path + DB_EXTENSION;
        File dbFile = new File(dataFolder, path);
        
        File parentDir = dbFile.getParentFile();
        if (!parentDir.exists()) {
            boolean ignored = parentDir.mkdirs();
        }
        
        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        return new SqliteDB(url);
    }
    
    /**
     * Creates a new SqliteDB instance without any checks.
     * <p>
     * It's recommended to use {@link #create(String)} instead,
     * unless you know what you're doing.
     * <p>
     * The given url is what you would normally pass to {@link DriverManager#getConnection(String)}.
     *
     * @param url the "raw" url of the database
     * @return a new SqliteDB instance
     */
    @Kapi
    public static SqliteDB createUnchecked(String url) {
        return new SqliteDB(url);
    }
    
    /**
     * Executes an SQL statement.
     * <p>
     * Must be either an INSERT, UPDATE, or DELETE statement,
     * or another statement that returns nothing.
     * <p>
     * Tip: for asynchronous operations, use {@link TaskBuilder#async()}
     * to create a new async task.
     *
     * @param sql    the SQL statement to execute
     * @param params zero or more placeholders for the SQL statement
     * @return the number of rows affected by the statement or
     *     a {@link SQLException} if an error occurred
     */
    @Kapi
    public Result<Integer,SQLException> executeUpdate(String sql, Object... params) {
        return operate(connection -> {
            try {
                PreparedStatement stmt = connection.prepareStatement(sql);
                setParameters(stmt, params);
                return Result.ok(stmt.executeUpdate());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    /**
     * Executes a given SQL query.
     * <p>
     * Must be a query that returns a result set, like a SELECT statement,
     * otherwise a {@link SQLException} is guaranteed to be returned.
     * <p>
     * Tip: for asynchronous operations, use {@link TaskBuilder#async()}
     * to create a new async task.
     *
     * @param sql    the SQL query to execute
     * @param params zero or more placeholders for the SQL query
     * @return the result of the query or a {@link SQLException} if an error occurred
     */
    @Kapi
    public Result<ResultSet,SQLException> executeQuery(String sql, Object... params) {
        return operate(connection -> {
            try {
                PreparedStatement stmt = connection.prepareStatement(sql);
                setParameters(stmt, params);
                return Result.ok(stmt.executeQuery());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    private <T> Result<T,SQLException> operate(Function<Connection,Result<T,SQLException>> consumer) {
        try {
            Connection connection = DriverManager.getConnection(url);
            return consumer.apply(connection);
        } catch (SQLException e) {
            return Result.err(e);
        }
    }
    
    private static void setParameters(PreparedStatement stmt, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }
    
}
