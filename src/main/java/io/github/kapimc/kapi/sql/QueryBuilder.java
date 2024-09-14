/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.sql;

import java.util.List;

public class QueryBuilder<T extends QueryBuilder<T>> {
    
    protected final StringBuilder sql = new StringBuilder();
    
    private static final List<String> QUOTED_TYPES = List.of("TEXT", "CHAR", "VARCHAR");
    
    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }
    
    public T raw(String raw) {
        sql.append(raw);
        return self();
    }
    
    public static SelectQueryBuilder select() {
        return new SelectQueryBuilder();
    }
    
    public static CreateTableQueryBuilder createTable(String name) {
        return new CreateTableQueryBuilder(name);
    }
    
    public static class SelectQueryBuilder extends QueryBuilder<SelectQueryBuilder> {
    
    }
    
    public static class CreateTableQueryBuilder extends QueryBuilder<CreateTableQueryBuilder> {
        
        private CreateTableQueryBuilder(String name) {
            sql.append("CREATE TABLE IF NOT EXISTS ").append(name).append(" (");
        }
        
        public CreateTableQueryBuilder column(String name, String type) {
            if (sql.charAt(sql.length() - 1) != '(') {
                sql.append(", ");
            }
            sql.append(name).append(" ").append(type);
            return this;
        }
        
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
        
        public CreateTableQueryBuilder columnAutoPrimaryKey(String name) {
            return column(name, "INTEGER").primaryKey().raw(" AUTOINCREMENT");
        }
        
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
        
        public CreateTableQueryBuilder check(String condition) {
            sql.append(" CHECK (").append(condition).append(")");
            return this;
        }
        
        public CreateTableQueryBuilder checkTable(String condition) {
            sql.append(", CHECK (").append(condition).append(")");
            return this;
        }
        
        public CreateTableQueryBuilder notNull() {
            sql.append(" NOT NULL");
            return this;
        }
        
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
        
        public CreateTableQueryBuilder onDelete(String action) {
            sql.append(" ON DELETE ").append(action);
            return this;
        }
        
        public CreateTableQueryBuilder onUpdate(String action) {
            sql.append(" ON UPDATE ").append(action);
            return this;
        }
    }
}

