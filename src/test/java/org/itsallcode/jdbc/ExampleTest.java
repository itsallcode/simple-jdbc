package org.itsallcode.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

import org.itsallcode.jdbc.resultset.SimpleResultSet;
import org.itsallcode.jdbc.resultset.generic.Row;
import org.junit.jupiter.api.Test;

class ExampleTest {
    record Name(int id, String name) {
        static void setPreparedStatement(final Name row, final PreparedStatement stmt) throws SQLException {
            stmt.setInt(1, row.id);
            stmt.setString(2, row.name);
        }
    }

    @Test
    void example() {
        final ConnectionFactory connectionFactory = ConnectionFactory
                .create(Context.builder().build());
        try (SimpleConnection connection = connectionFactory.create("jdbc:h2:mem:", "user", "password")) {
            connection.executeScript(readResource("/schema.sql"));
            connection.batchInsert(Name.class)
                    .into("NAMES", List.of("ID", "NAME"))
                    .rows(Stream.of(new Name(1, "a"), new Name(2, "b"), new Name(3, "c")))
                    .mapping(Name::setPreparedStatement)
                    .start();

            try (SimpleResultSet<Row> rs = connection.query("select * from names order by id")) {
                final List<Row> result = rs.stream().toList();
                assertEquals(3, result.size());
                assertEquals(1, result.get(0).get(0).value());
            }
        }
    }

    private String readResource(final String resourceName) {
        final URL resource = getClass().getResource(resourceName);
        if (resource == null) {
            throw new IllegalArgumentException("No resource found for name '" + resourceName + "'");
        }
        try (InputStream stream = resource.openStream()) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (final IOException e) {
            throw new UncheckedIOException("Error reading resource " + resource, e);
        }
    }
}
