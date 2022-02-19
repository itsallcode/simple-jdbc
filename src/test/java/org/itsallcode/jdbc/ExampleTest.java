package org.itsallcode.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Stream;

import org.itsallcode.jdbc.resultset.Row;
import org.itsallcode.jdbc.resultset.SimpleResultSet;
import org.junit.jupiter.api.Test;

class ExampleTest {
    @Test
    void example() {
        record Name(int id, String name) {
            Object[] toRow() {
                return new Object[] { id, name };
            }
        }
        ConnectionFactory connectionFactory = ConnectionFactory.create();
        try (SimpleConnection connection = connectionFactory.create("jdbc:h2:mem:", "user", "password")) {
            connection.executeScriptFromResource("/schema.sql");
            connection.insert("NAMES", List.of("ID", "NAME"), Name::toRow,
                    Stream.of(new Name(1, "a"), new Name(2, "b"), new Name(3, "c")));
            try (SimpleResultSet<Row> rs = connection.query("select * from names order by id")) {
                List<Row> result = rs.stream().toList();
                assertEquals(3, result.size());
                assertEquals(1, result.get(0).getColumnValue(0).getValue());
            }
        }
    }
}
