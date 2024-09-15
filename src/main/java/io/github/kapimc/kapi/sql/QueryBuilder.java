/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.sql;

import io.github.kapimc.kapi.annotations.Kapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class QueryBuilder<T extends QueryBuilder<T>> {
    
    /**
     * A list of types that should be quoted.
     */
    @Kapi
    public static final List<String> QUOTED_TYPES = List.of("TEXT", "CHAR", "VARCHAR");
    
    
    protected final StringBuilder sql = new StringBuilder();
    
    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }
    
    /**
     * Builds the QueryBuilder into a SQL query.
     *
     * @return the built SQL query
     */
    @Kapi
    public abstract SqlQuery build();
    
    /**
     * Appends raw SQL to the query.
     * <p>
     * This should only be used when necessary,
     * as it can lead to SQL injection vulnerabilities.
     *
     * @param raw the raw SQL to append
     * @return this, for chaining
     */
    @Kapi
    public T raw(String raw) {
        sql.append(raw);
        return self();
    }
    
    /**
     * Creates a query builder for creating a SQL table.
     *
     * @param table the name of the table
     * @return a new CreateTableQueryBuilder
     */
    @Kapi
    public static CreateTableQueryBuilder createTable(String table) {
        return new CreateTableQueryBuilder(table);
    }
    
    /**
     * @param table the name of the table
     * @return a new SqlQuery that drops the table if it exists
     */
    @Kapi
    public static SqlQuery dropTable(String table) {
        return new SqlQuery("DROP TABLE IF EXISTS " + table + ";");
    }
    
    /**
     * Creates a query builder for inserting values into a SQL table.
     *
     * @param table the name of the table
     * @return a new InsertIntoQueryBuilder
     */
    @Kapi
    public static InsertIntoQueryBuilder insertInto(String table) {
        return new InsertIntoQueryBuilder(table);
    }
    
    /**
     * Creates a query builder for deleting rows from a SQL table.
     *
     * @param table the name of the table
     * @return a new DeleteFromQueryBuilder
     */
    @Kapi
    public static DeleteFromQueryBuilder deleteFrom(String table) {
        return new DeleteFromQueryBuilder(table);
    }
    
    /**
     * Creates a query builder for updating rows in a SQL table.
     *
     * @param table the name of the table
     * @return a new UpdateQueryBuilder
     */
    @Kapi
    public static UpdateQueryBuilder update(String table) {
        return new UpdateQueryBuilder(table);
    }
    
    /**
     * @param index the name of the index
     * @return a new SqlQuery that drops the index
     */
    @Kapi
    public static SqlQuery dropIndex(String index) {
        return new SqlQuery("DROP INDEX IF EXISTS " + index + ";");
    }
    
    /**
     * @param index the name of the index
     * @return a new SqlQuery that re-indexes the index
     */
    @Kapi
    public static SqlQuery reindex(String index) {
        return new SqlQuery("REINDEX INDEX " + index + ";");
    }
    
    /**
     * Creates a query builder for creating an index.
     *
     * @param index the name of the index
     * @return a new CreateIndexQueryBuilder
     */
    @Kapi
    public static CreateIndexQueryBuilder createIndex(String index) {
        return new CreateIndexQueryBuilder(index);
    }
    
    /**
     * Creates a query builder for creating a unique index.
     *
     * @param index the name of the index
     * @return a new CreateIndexQueryBuilder
     */
    @Kapi
    public static CreateIndexQueryBuilder createUniqueIndex(String index) {
        return new CreateIndexQueryBuilder(index, "UNIQUE");
    }
    
    /**
     * Creates a query builder for creating a full-text index.
     *
     * @param index the name of the index
     * @return a new CreateIndexQueryBuilder
     */
    @Kapi
    public static CreateIndexQueryBuilder createFullTextIndex(String index) {
        return new CreateIndexQueryBuilder(index, "FULLTEXT");
    }
    
    /**
     * Creates a query builder for creating a spatial index.
     *
     * @param index the name of the index
     * @return a new CreateIndexQueryBuilder
     */
    @Kapi
    public static CreateIndexQueryBuilder createSpatialIndex(String index) {
        return new CreateIndexQueryBuilder(index, "SPATIAL");
    }
    
    /**
     * Creates a query builder for creating a clustered index.
     *
     * @param index the name of the index
     * @return a new CreateIndexQueryBuilder
     */
    @Kapi
    public static CreateIndexQueryBuilder createClusteredIndex(String index) {
        return new CreateIndexQueryBuilder(index, "CLUSTERED");
    }
    
    /**
     * Creates a query builder for selecting columns from a table.
     *
     * @param columns the columns to select
     * @return a new SelectQueryBuilder
     */
    @Kapi
    public static SelectQueryBuilder select(String... columns) {
        return new SelectQueryBuilder(columns);
    }
    
    /**
     * Creates a query builder for selecting all columns from a table.
     *
     * @param table the name of the table
     * @return a new SelectQueryBuilder
     */
    @Kapi
    public static SelectQueryBuilder selectFrom(String table) {
        return new SelectQueryBuilder("*").from(table);
    }
    
    public static class SelectQueryBuilder extends QueryBuilder<SelectQueryBuilder> {
        
        private final List<Object> values = new ArrayList<>();
        
        private SelectQueryBuilder(String... columns) {
            sql.append("SELECT ").append(String.join(", ", columns));
        }
        
        /**
         * {@inheritDoc}
         */
        @Kapi
        @Override
        public SqlQuery build() {
            sql.append(";");
            return new SqlQuery(sql.toString(), values.toArray());
        }
        
        /**
         * Adds the table to select from.
         * <p>
         * This should only be used once.
         *
         * @param table the name of the table
         * @return this, for chaining
         */
        @Kapi
        public SelectQueryBuilder from(String table) {
            sql.append(" FROM ").append(table);
            return this;
        }
        
        /**
         * Adds a condition to filter the results.
         * <p>
         * This should only be used once.
         *
         * @param condition    the condition to add
         * @param placeholders optional placeholders for any {@code ?} in the condition
         * @return this, for chaining
         */
        @Kapi
        public SelectQueryBuilder where(String condition, Object... placeholders) {
            sql.append(" WHERE ").append(condition);
            this.values.addAll(Arrays.asList(placeholders));
            return this;
        }
        
        /**
         * Groups the results by the given columns.
         * <p>
         * This should only be used once.
         *
         * @param columns the columns to group by
         * @return this, for chaining
         */
        @Kapi
        public SelectQueryBuilder groupBy(String... columns) {
            sql.append(" GROUP BY ").append(String.join(", ", columns));
            return this;
        }
        
        /**
         * Adds a condition to filter the groups.
         * <p>
         * This should only be used once.
         *
         * @param condition    the condition to add
         * @param placeholders optional placeholders for any {@code ?} in the condition
         * @return this, for chaining
         */
        @Kapi
        public SelectQueryBuilder having(String condition, Object... placeholders) {
            sql.append(" HAVING ").append(condition);
            this.values.addAll(Arrays.asList(placeholders));
            return this;
        }
        
        /**
         * Orders the results by the given columns.
         * <p>
         * You may add {@code ASC} or {@code DESC} to the end of the column name to specify the order.
         * If nothing was specified, it will default to {@code ASC}.
         * <p>
         * This should only be used once.
         *
         * @param columns the columns to order by
         * @return this, for chaining
         */
        @Kapi
        public SelectQueryBuilder orderBy(String... columns) {
            sql.append(" ORDER BY ").append(String.join(", ", columns));
            return this;
        }
        
        /**
         * Limits the number of results returned.
         * <p>
         * This should only be used once.
         *
         * @param limit the maximum number of results to return
         * @return this, for chaining
         */
        @Kapi
        public SelectQueryBuilder limit(int limit) {
            sql.append(" LIMIT ").append(limit);
            return this;
        }
    }
    
    /**
     * Creates a query builder for creating an index.
     */
    @Kapi
    public static class CreateIndexQueryBuilder extends QueryBuilder<CreateIndexQueryBuilder> {
        
        private CreateIndexQueryBuilder(String name, String type) {
            sql.append("CREATE ").append(type).append(" INDEX IF NOT EXISTS ").append(name).append(" ON ");
        }
        
        private CreateIndexQueryBuilder(String name) {
            sql.append("CREATE INDEX IF NOT EXISTS ").append(name).append(" ON ");
        }
        
        /**
         * {@inheritDoc}
         */
        @Kapi
        @Override
        public SqlQuery build() {
            sql.append(";");
            return new SqlQuery(sql.toString());
        }
        
        /**
         * Adds a list of columns from a table, to index.
         * <p>
         * You may add {@code ASC} or {@code DESC} to the end of the column name to specify the order.
         * If nothing was specified, it will default to {@code ASC}.
         * <p>
         * This should only be used once.
         *
         * @param table   the name of the table
         * @param columns the columns to index
         * @return this, for chaining
         */
        @Kapi
        public CreateIndexQueryBuilder on(String table, String... columns) {
            sql.append(table).append(" (");
            for (int i = 0; i < columns.length; i++) {
                if (i != 0) sql.append(", ");
                sql.append(columns[i]);
            }
            sql.append(")");
            return this;
        }
        
    }
    
    /**
     * Creates a query builder for updating rows in a SQL table.
     */
    @Kapi
    public static class UpdateQueryBuilder extends QueryBuilder<UpdateQueryBuilder> {
        
        private final List<Object> values = new ArrayList<>();
        private boolean firstSet = true;
        
        private UpdateQueryBuilder(String table) {
            sql.append("UPDATE ").append(table).append(" SET ");
        }
        
        /**
         * {@inheritDoc}
         */
        @Kapi
        @Override
        public SqlQuery build() {
            sql.append(";");
            return new SqlQuery(sql.toString(), values.toArray());
        }
        
        /**
         * Adds a condition to the update statement.
         *
         * @param column the column to set
         * @param value  the value to set
         * @return this, for chaining
         */
        @Kapi
        public UpdateQueryBuilder set(String column, Object value) {
            if (!firstSet) sql.append(", ");
            sql.append(column).append(" = ?");
            this.values.add(value);
            firstSet = false;
            return this;
        }
        
        /**
         * Adds a condition to the update statement.
         * <p>
         * Allows inserting raw values to allow for things like nested queries.
         * This should only be used when necessary,
         * as it can lead to SQL injection vulnerabilities.
         *
         * @param column       the column to set
         * @param value        the value to set
         * @param placeholders optional placeholders for any {@code ?} in the value
         * @return this, for chaining
         */
        @Kapi
        public UpdateQueryBuilder setRaw(String column, String value, Object... placeholders) {
            if (!firstSet) sql.append(", ");
            sql.append(column).append(" = ").append(value);
            this.values.addAll(Arrays.asList(placeholders));
            firstSet = false;
            return this;
        }
        
        /**
         * Adds a condition to the update statement.
         *
         * @param condition    the condition to add
         * @param placeholders optional placeholders for any {@code ?} in the condition
         * @return this, for chaining
         */
        @Kapi
        public UpdateQueryBuilder where(String condition, Object... placeholders) {
            sql.append(" WHERE ").append(condition);
            this.values.addAll(Arrays.asList(placeholders));
            return this;
        }
    }
    
    /**
     * Creates a query builder for deleting rows from a SQL table.
     */
    @Kapi
    public static class DeleteFromQueryBuilder extends QueryBuilder<DeleteFromQueryBuilder> {
        
        private DeleteFromQueryBuilder(String table) {
            sql.append("DELETE FROM ").append(table).append(" WHERE ");
        }
        
        /**
         * {@inheritDoc}
         */
        @Kapi
        @Override
        public SqlQuery build() {
            sql.append(";");
            return new SqlQuery(sql.toString());
        }
        
        /**
         * Adds a condition to the delete statement.
         *
         * @param condition the condition to add
         * @return this, for chaining
         */
        @Kapi
        public DeleteFromQueryBuilder where(String condition) {
            sql.append(condition);
            return this;
        }
    }
    
    /**
     * A query builder for inserting values into a SQL table.
     */
    @Kapi
    public static class InsertIntoQueryBuilder extends QueryBuilder<InsertIntoQueryBuilder> {
        
        private final List<Object> values = new ArrayList<>();
        
        private InsertIntoQueryBuilder(String table) {
            sql.append("INSERT INTO ").append(table).append(" (");
        }
        
        /**
         * {@inheritDoc}
         */
        @Kapi
        @Override
        public SqlQuery build() {
            sql.append(";");
            return new SqlQuery(sql.toString(), values.toArray());
        }
        
        /**
         * Adds columns to the insert statement.
         * <p>
         * Must be called immediately after {@link #insertInto(String)}.
         *
         * @param columns the columns to insert into
         * @return this, for chaining
         */
        @Kapi
        public InsertIntoQueryBuilder columns(String... columns) {
            for (int i = 0; i < columns.length; i++) {
                if (i != 0) sql.append(", ");
                sql.append(columns[i]);
            }
            sql.append(")\n");
            return this;
        }
        
        /**
         * Adds values to the insert statement.
         * <p>
         * This can be used multiple times to add multiple rows.
         *
         * @param values the values to insert
         * @return this, for chaining
         */
        @Kapi
        public InsertIntoQueryBuilder values(Object... values) {
            if (sql.charAt(sql.length() - 1) == '\n') {
                sql.append("VALUES ");
            }
            if (sql.charAt(sql.length() - 1) == ')') {
                sql.append(", ");
            }
            sql.append("(");
            for (int i = 0; i < values.length; i++) {
                if (i != 0) sql.append(", ");
                sql.append("?");
                this.values.add(values[i]);
            }
            sql.append(")");
            return this;
        }
        
        /**
         * Adds values to the insert statement.
         * <p>
         * This can be used multiple times to add multiple rows.
         *
         * @param values       the values to insert, requires commas between each value but no surrounding parentheses
         * @param placeholders optional placeholders for any {@code ?} in the values
         * @return this, for chaining
         */
        @Kapi
        public InsertIntoQueryBuilder values(String values, Object... placeholders) {
            if (sql.charAt(sql.length() - 1) == '\n') {
                sql.append("VALUES ");
            }
            if (sql.charAt(sql.length() - 1) == ')') {
                sql.append(", ");
            }
            sql.append("(").append(values).append(")");
            this.values.addAll(Arrays.asList(placeholders));
            return this;
        }
        
        // TODO: Add support for SELECT queries (after it's implemented)
    }
    
    /**
     * A query builder for creating SQL tables.
     */
    @Kapi
    public static class CreateTableQueryBuilder extends QueryBuilder<CreateTableQueryBuilder> {
        
        private CreateTableQueryBuilder(String name) {
            sql.append("CREATE TABLE IF NOT EXISTS ").append(name).append(" (");
        }
        
        /**
         * {@inheritDoc}
         */
        @Kapi
        @Override
        public SqlQuery build() {
            sql.append(");");
            return new SqlQuery(sql.toString());
        }
        
        /**
         * Adds a column to the table.
         *
         * @param name the name of the column
         * @param type the type of the column
         * @return this, for chaining
         */
        @Kapi
        public CreateTableQueryBuilder column(String name, String type) {
            if (sql.charAt(sql.length() - 1) != '(') {
                sql.append(", ");
            }
            sql.append(name).append(" ").append(type);
            return this;
        }
        
        /**
         * Adds a column to the table with a default value.
         * <p>
         * The default value will be quoted if the type requires it.
         * Supported types are {@link QueryBuilder#QUOTED_TYPES}.
         *
         * @param name         the name of the column
         * @param type         the type of the column
         * @param defaultValue the default value of the column
         * @return this, for chaining
         */
        @Kapi
        public CreateTableQueryBuilder column(String name, String type, String defaultValue) {
            column(name, type);
            sql.append(" DEFAULT ");
            if (QUOTED_TYPES.contains(type.toUpperCase())) {
                sql.append("'").append(defaultValue).append("'");
            } else {
                sql.append(defaultValue);
            }
            return this;
        }
        
        /**
         * Adds an auto-incrementing integer primary key to the table.
         *
         * @param name the name of the column
         * @return this, for chaining
         */
        @Kapi
        public CreateTableQueryBuilder columnAutoPrimaryKey(String name) {
            return column(name, "INTEGER").primaryKey().raw(" AUTOINCREMENT");
        }
        
        /**
         * Adds a primary key to the table.
         * <p>
         * If no columns are provided, it is assumed that the primary key is applied to the column.
         * If one or more columns are provided, it is assumed that the primary key is applied to the table.
         *
         * @param columns the columns to add as a primary key
         * @return this, for chaining
         */
        @Kapi
        public CreateTableQueryBuilder primaryKey(String... columns) {
            if (columns.length == 0) {
                // On column
                sql.append(" PRIMARY KEY");
            } else {
                // On table
                sql.append(", ").append("PRIMARY KEY (");
                for (int i = 0; i < columns.length; i++) {
                    if (i != 0) sql.append(", ");
                    sql.append(columns[i]);
                }
                sql.append(")");
            }
            return this;
        }
        
        /**
         * Adds a unique constraint to the table.
         * <p>
         * If no columns are provided, it is assumed that the unique constraint is applied to the column.
         * If one or more columns are provided, it is assumed that the unique constraint is applied to the table.
         *
         * @param columns the columns to add as a unique constraint
         * @return this, for chaining
         */
        @Kapi
        public CreateTableQueryBuilder unique(String... columns) {
            if (columns.length == 0) {
                // On column
                sql.append(" UNIQUE");
            } else {
                // On table
                sql.append(", ").append("UNIQUE (");
                for (int i = 0; i < columns.length; i++) {
                    if (i != 0) sql.append(", ");
                    sql.append(columns[i]);
                }
                sql.append(")");
            }
            return this;
        }
        
        /**
         * Adds a check constraint to the column.
         *
         * @param condition the condition to check
         * @return this, for chaining
         */
        @Kapi
        public CreateTableQueryBuilder check(String condition) {
            sql.append(" CHECK (").append(condition).append(")");
            return this;
        }
        
        /**
         * Adds a check constraint to the table.
         *
         * @param condition the condition to check
         * @return this, for chaining
         */
        @Kapi
        public CreateTableQueryBuilder checkTable(String condition) {
            sql.append(", CHECK (").append(condition).append(")");
            return this;
        }
        
        /**
         * Adds a not null constraint to the column.
         *
         * @return this, for chaining
         */
        @Kapi
        public CreateTableQueryBuilder notNull() {
            sql.append(" NOT NULL");
            return this;
        }
        
        /**
         * Adds a foreign key to the column.
         *
         * @param column      the column to add the foreign key to
         * @param table       the referenced table name
         * @param otherColumn the referenced column name in the referenced table
         * @return this, for chaining
         */
        @Kapi
        public CreateTableQueryBuilder foreignKey(String column, String table, String otherColumn) {
            sql.append(", FOREIGN KEY (")
                .append(column)
                .append(") REFERENCES ")
                .append(table)
                .append("(")
                .append(otherColumn)
                .append(")");
            return this;
        }
        
        /**
         * Adds an ON DELETE action to the foreign key.
         * <p>
         * This should only be used immediately after {@link #foreignKey(String, String, String)}.
         *
         * @param action the action to take when the referenced column is deleted
         * @return this, for chaining
         */
        @Kapi
        public CreateTableQueryBuilder onDelete(String action) {
            sql.append(" ON DELETE ").append(action);
            return this;
        }
        
        /**
         * Adds an ON UPDATE action to the foreign key.
         * <p>
         * This should only be used immediately after {@link #foreignKey(String, String, String)}.
         *
         * @param action the action to take when the referenced column is updated
         * @return this, for chaining
         */
        @Kapi
        public CreateTableQueryBuilder onUpdate(String action) {
            sql.append(" ON UPDATE ").append(action);
            return this;
        }
    }
}

