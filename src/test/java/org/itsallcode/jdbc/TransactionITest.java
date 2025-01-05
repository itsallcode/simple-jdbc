package org.itsallcode.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Stream;

import org.itsallcode.jdbc.resultset.generic.Row;
import org.junit.jupiter.api.Test;

class TransactionITest {
    @Test
    void commit() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeUpdate("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            try (Transaction tx = connection.startTransaction()) {
                tx.executeUpdate("insert into test (id, name) values (1, 'test')");
                final List<Row> resultSet = tx.query("select count(*) as result from test").toList();
                assertEquals(1L, resultSet.get(0).get(0).getValue(Long.class));
                tx.commit();
            }
            final List<Row> resultSet = connection.query("select count(*) as result from test").toList();
            assertEquals(1L, resultSet.get(0).get(0).getValue(Long.class));
        }
    }

    @Test
    void explicitRollback() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeUpdate("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            try (Transaction tx = connection.startTransaction()) {
                tx.executeUpdate("insert into test (id, name) values (1, 'test')");
                final List<Row> resultSet = tx.query("select count(*) as result from test").toList();
                assertEquals(1L, resultSet.get(0).get(0).getValue(Long.class));
                tx.rollback();
            }
            final List<Row> resultSet = connection.query("select count(*) as result from test").toList();
            assertEquals(0L, resultSet.get(0).get(0).getValue(Long.class));
        }
    }

    @Test
    void implicitRollback() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeUpdate("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            try (Transaction tx = connection.startTransaction()) {
                tx.executeUpdate("insert into test (id, name) values (1, 'test')");
                final List<Row> resultSet = tx.query("select count(*) as result from test").toList();
                assertEquals(1L, resultSet.get(0).get(0).getValue(Long.class));
            }
            final List<Row> resultSet = connection.query("select count(*) as result from test").toList();
            assertEquals(0L, resultSet.get(0).get(0).getValue(Long.class));
        }
    }

    @Test
    void executeStatementWithParamSetter() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeUpdate("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            try (Transaction tx = connection.startTransaction()) {
                tx.executeUpdate("insert into test (id, name) values (?, ?)", stmt -> {
                    stmt.setInt(1, 1);
                    stmt.setString(2, "a");
                });
                final List<Row> resultSet = tx.query("select count(*) as result from test where id=1 and name='a'")
                        .toList();
                assertEquals(1L, resultSet.get(0).get(0).getValue(Long.class));
            }
        }
    }

    @Test
    void executeScript() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            try (Transaction tx = connection.startTransaction()) {
                tx.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
                final List<Row> resultSet = tx.query("select count(*) as result from test")
                        .toList();
                assertEquals(0L, resultSet.get(0).get(0).getValue(Long.class));
            }
        }
    }

    @Test
    void queryWithRowMapper() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeUpdate("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            try (Transaction tx = connection.startTransaction()) {
                tx.executeUpdate("insert into test (id, name) values (1, 'test')");
                final List<String> resultSet = tx.query("select * from test",
                        (final ResultSet rs, final int rowNum) -> rs.getString("name")).toList();
                assertEquals(List.of("test"), resultSet);
            }
        }
    }

    @Test
    void queryWithParamSetter() {
        try (final SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeUpdate("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            try (final Transaction tx = connection.startTransaction()) {
                tx.executeUpdate("insert into test (id, name) values (1, 'test')");
                final List<String> resultSet = tx.query("select * from test where id = ?",
                        stmt -> stmt.setInt(1, 1),
                        (final ResultSet rs, final int rowNum) -> rs.getString("name")).toList();
                assertEquals(List.of("test"), resultSet);
            }
        }
    }

    @Test
    void batchInsert() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            try (Transaction tx = connection.startTransaction()) {
                tx.batchInsert(String.class).into("TEST", List.of("ID", "NAME"))
                        .mapping(row -> new Object[] { row.length(), row })
                        .rows(Stream.of("a", "ab", "abc")).start();
                final List<Row> resultSet = tx.query("select count(*) as result from test")
                        .toList();
                assertEquals(3L, resultSet.get(0).get(0).getValue(Long.class));
            }
        }
    }
}
