/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.sql;

import io.github.kapimc.kapi.data.Result;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SqliteDBTest {
    
    @Test
    public void testCreate() throws SQLException {
        String table = "test";
        List<Map<String,Object>> data = List.of(
            Map.of("name", "Kyren", "age", 18),
            Map.of("name", "Kapi", "age", 223),
            Map.of("name", "JavaScript user", "age", 0),
            Map.of("name", "Rust + NeoVim GigaChad", "age", Integer.MAX_VALUE)
        );
        
        SqliteDB db = SqliteDB.createUnchecked("jdbc:sqlite::memory:");
        assertNotNull(db);
        SqlQuery query = QueryBuilder.createTable(table)
            .columnAutoPrimaryKey("id")
            .column("name", "TEXT")
            .column("age", "INTEGER")
            .build();
        db.executeUpdate(query);
        
        query = QueryBuilder.insertInto(table)
            .columns("name", "age")
            .values("Kyren", 18)
            .values("Kapi", 223)
            .values("JavaScript user", 0)
            .values("Rust + NeoVim GigaChad", Integer.MAX_VALUE)
            .build();
        db.executeUpdate(query);
        
        query = QueryBuilder.select("id", "name", "age").from(table).build();
        Result<ResultSet,SQLException> result = db.executeQuery(query);
        if (result.isErr()) {
            throw result.unwrapErr();
        }
        
        try (ResultSet rs = result.unwrap()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                
                assertEquals(data.get(id).get("name"), name);
                assertEquals(data.get(id).get("age"), age);
            }
        }
    }
    
}