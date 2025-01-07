package org.itsallcode.jdbc.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

import org.itsallcode.jdbc.*;
import org.itsallcode.jdbc.batch.PreparedStatementBatch;
import org.itsallcode.jdbc.batch.StatementBatch;
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
    void exampleInsertSelect() {
        final ConnectionFactory connectionFactory = ConnectionFactory
                .create(Context.builder().build());
        try (SimpleConnection connection = connectionFactory.create("jdbc:h2:mem:", "user", "password")) {
            connection.executeScript(readResource("/schema.sql"));
            connection.executeUpdate("insert into names (id, name) values (1, 'a'), (2, 'b'), (3, 'c')");

            try (SimpleResultSet<Row> rs = connection.query("select * from names order by id")) {
                final List<Row> result = rs.stream().toList();
                assertEquals(3, result.size());
                assertEquals(1, result.get(0).get(0).value());
            }
        }
    }

    @Test
    void examplePreparedStatementWithRowMapper() {
        final ConnectionFactory connectionFactory = ConnectionFactory
                .create(Context.builder().build());
        try (SimpleConnection connection = connectionFactory.create("jdbc:h2:mem:", "user", "password")) {
            connection.executeScript(readResource("/schema.sql"));
            connection.executeUpdate("insert into names (id, name) values (1, 'a'), (2, 'b'), (3, 'c')");

            try (SimpleResultSet<Name> result = connection.query("select id, name from names where id = ?",
                    ps -> ps.setInt(1, 2),
                    (rs, idx) -> new Name(rs.getInt("id"), rs.getString("name")))) {
                final List<Name> names = result.stream().toList();
                assertEquals(1, names.size());
                assertEquals(new Name(2, "b"), names.get(0));
            }
        }
    }

    @Test
    void exampleBatchStatement() {
        final ConnectionFactory connectionFactory = ConnectionFactory
                .create(Context.builder().build());
        try (SimpleConnection connection = connectionFactory.create("jdbc:h2:mem:", "user", "password")) {
            try (StatementBatch batch = connection.batch().maxBatchSize(3).build()) {
                batch.addBatch("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
                batch.addBatch("INSERT INTO TEST VALUES (1, 'a')");
                batch.addBatch("INSERT INTO TEST VALUES (2, 'b')");
                batch.addBatch("INSERT INTO TEST VALUES (3, 'c')");
                batch.addBatch("INSERT INTO TEST VALUES (4, 'd')");
            }

            final List<Row> result = connection.query("select count(*) from test").stream().toList();
            assertAll(
                    () -> assertThat(result).hasSize(1),
                    () -> assertThat(result.get(0).columnValues()).hasSize(1),
                    () -> assertThat(result.get(0).get(0).value()).isEqualTo(4L));
        }
    }

    @Test
    void exampleRowBatchInsert() {
        final ConnectionFactory connectionFactory = ConnectionFactory
                .create(Context.builder().build());
        try (SimpleConnection connection = connectionFactory.create("jdbc:h2:mem:", "user", "password")) {
            connection.executeScript(readResource("/schema.sql"));
            connection.batchInsert(Name.class)
                    .into("NAMES", List.of("ID", "NAME"))
                    .rows(Stream.of(new Name(1, "a"), new Name(2, "b"), new Name(3, "c")))
                    .mapping(Name::setPreparedStatement)
                    .maxBatchSize(100)
                    .start();

            try (SimpleResultSet<Row> rs = connection.query("select * from names order by id")) {
                final List<Row> result = rs.stream().toList();
                assertEquals(3, result.size());
                assertEquals(1, result.get(0).get(0).value());
            }
        }
    }

    @Test
    void exampleDirectBatchInsert() {
        final ConnectionFactory connectionFactory = ConnectionFactory
                .create(Context.builder().build());
        try (SimpleConnection connection = connectionFactory.create("jdbc:h2:mem:", "user", "password")) {
            try (Transaction transaction = connection.startTransaction()) {
                transaction.executeScript(readResource("/schema.sql"));
                try (PreparedStatementBatch batch = transaction.batchInsert()
                        .into("NAMES", List.of("ID", "NAME"))
                        .maxBatchSize(100)
                        .build()) {
                    for (int i = 0; i < 5; i++) {
                        final int id = i + 1;
                        batch.add(ps -> {
                            ps.setInt(1, id);
                            ps.setString(2, "name" + id);
                        });
                    }
                }
                transaction.commit();
            }

            try (SimpleResultSet<Row> rs = connection.query("select * from names order by id")) {
                final List<Row> result = rs.stream().toList();
                assertEquals(5, result.size());
                final Row firstRow = result.get(0);
                assertEquals(1, firstRow.get(0).value());
                assertEquals("name1", firstRow.get(1).value());
            }
        }
    }

    @Test
    void exampleRawBatchInsert() throws SQLException {
        final ConnectionFactory connectionFactory = ConnectionFactory
                .create(Context.builder().build());
        try (SimpleConnection connection = connectionFactory.create("jdbc:h2:mem:", "user", "password")) {
            try (Transaction transaction = connection.startTransaction()) {
                transaction.executeScript(readResource("/schema.sql"));
                try (PreparedStatementBatch batch = transaction.batchInsert()
                        .into("NAMES", List.of("ID", "NAME"))
                        .maxBatchSize(100)
                        .build()) {
                    final PreparedStatement statement = batch.getStatement();
                    for (int i = 0; i < 5; i++) {
                        final int id = i + 1;
                        statement.setInt(1, id);
                        statement.setString(2, "name" + id);
                        batch.addBatch();
                    }
                }
                transaction.commit();
            }

            try (SimpleResultSet<Row> rs = connection.query("select * from names order by id")) {
                final List<Row> result = rs.stream().toList();
                assertEquals(5, result.size());
                final Row firstRow = result.get(0);
                assertEquals(1, firstRow.get(0).value());
                assertEquals("name1", firstRow.get(1).value());
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
