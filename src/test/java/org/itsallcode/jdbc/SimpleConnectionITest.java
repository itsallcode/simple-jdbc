package org.itsallcode.jdbc;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

import org.itsallcode.jdbc.identifier.Identifier;
import org.itsallcode.jdbc.resultset.RowMapper;
import org.itsallcode.jdbc.resultset.SimpleResultSet;
import org.itsallcode.jdbc.resultset.generic.JdbcType;
import org.itsallcode.jdbc.resultset.generic.Row;
import org.junit.jupiter.api.Test;

class SimpleConnectionITest {
    @Test
    void executeStatement() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeStatement("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            assertDoesNotThrow(() -> connection.executeStatement("select count(*) from test"));
        }
    }

    @Test
    void executeStatementFails() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            assertThatThrownBy(() -> connection.executeStatement("select count(*) from missingtable"))
                    .isInstanceOf(UncheckedSQLException.class)
                    .hasMessage("Error executing 'select count(*) from missingtable'")
                    .hasCauseInstanceOf(SQLException.class);
        }
    }

    @Test
    void executeScript() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255));"
                    + "insert into test (id, name) values (1, 'test');");
            assertDoesNotThrow(() -> connection.executeStatement("select count(*) from test"));
        }
    }

    @Test
    void executeQueryWithGenericRowMapper() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255));"
                    + "insert into test (id, name) values (1, 'test');");
            try (SimpleResultSet<Row> resultSet = connection.query("select count(*) from test")) {
                final List<Row> rows = resultSet.stream().collect(toList());
                assertThat(rows).hasSize(1);
                assertThat(rows.get(0).rowIndex()).isZero();
                assertThat(rows.get(0).columnValues()).hasSize(1);
                assertThat(rows.get(0).get(0).value()).isEqualTo(1L);
            }
        }
    }

    @Test
    void executeQueryWithListRowMapper() {
        final ConnectionFactory factory = ConnectionFactory.create(Context.builder().build());
        try (SimpleConnection connection = factory.create("jdbc:h2:mem:")) {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255));"
                    + "insert into test (id, name) values (1, 'test');");
            try (SimpleResultSet<List<Object>> resultSet = connection.query("select * from test",
                    RowMapper.columnValueList(connection.getDialect()))) {
                final List<List<Object>> rows = resultSet.toList();
                assertThat(rows).hasSize(1);
                assertThat(rows.get(0)).containsExactly(1, "test");
            }
        }
    }

    @Test
    void executeQueryEmptyResult() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            try (SimpleResultSet<Row> resultSet = connection.query("select * from test")) {
                final Iterator<Row> iterator = resultSet.iterator();
                assertThat(iterator.hasNext()).isFalse();
                assertThatThrownBy(() -> iterator.next()).isInstanceOf(NoSuchElementException.class);
            }
        }
    }

    @Test
    void executeQuerySingleRow() {
        try (final SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255));"
                    + "insert into test (id, name) values (1, 'test');");
            try (final SimpleResultSet<Row> resultSet = connection.query("select * from test")) {
                final Iterator<Row> iterator = resultSet.iterator();
                assertThat(iterator.hasNext()).isTrue();
                final Row firstRow = iterator.next();

                assertAll(() -> assertThat(firstRow.rowIndex()).isZero(),
                        () -> assertThat(firstRow.columnValues()).hasSize(2),
                        () -> assertThat(firstRow.columnValues().get(0).value()).isEqualTo(1),
                        () -> assertThat(firstRow.columnValues().get(0).type().jdbcType())
                                .isEqualTo(JdbcType.INTEGER),
                        () -> assertThat(firstRow.columnValues().get(0).type().typeName())
                                .isEqualTo("INTEGER"),
                        () -> assertThat(firstRow.columnValues().get(0).type().className())
                                .isEqualTo(Integer.class.getName()),
                        () -> assertThat(firstRow.columnValues().get(0).type().displaySize()).isEqualTo(11),
                        () -> assertThat(firstRow.columnValues().get(0).type().precision()).isEqualTo(32),
                        () -> assertThat(firstRow.columnValues().get(0).type().scale()).isZero(),

                        () -> assertThat(firstRow.columnValues().get(1).value()).isEqualTo("test"),
                        () -> assertThat(firstRow.columnValues().get(1).type().jdbcType())
                                .isEqualTo(JdbcType.VARCHAR),
                        () -> assertThat(firstRow.columnValues().get(1).type().typeName())
                                .isEqualTo("CHARACTER VARYING"),
                        () -> assertThat(firstRow.columnValues().get(1).type().className())
                                .isEqualTo(String.class.getName()),
                        () -> assertThat(firstRow.columnValues().get(1).type().displaySize()).isEqualTo(255),
                        () -> assertThat(firstRow.columnValues().get(1).type().precision()).isEqualTo(255),
                        () -> assertThat(firstRow.columnValues().get(1).type().scale()).isZero(),

                        () -> assertThat(iterator.hasNext()).isFalse(),
                        () -> assertThatThrownBy(() -> iterator.next()).isInstanceOf(NoSuchElementException.class));
            }
        }
    }

    @Test
    void executeQueryOnlyOneIteratorAllowed() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            try (SimpleResultSet<Row> resultSet = connection.query("select * from test")) {
                final Iterator<Row> iterator = resultSet.iterator();
                assertThat(iterator).isNotNull();
                assertThatThrownBy(() -> resultSet.iterator()).isInstanceOf(IllegalStateException.class)
                        .hasMessage("Only one iterator allowed per ResultSet");
            }
        }
    }

    @Test
    void batchInsertEmptyInput() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            connection.insert("insert into test (id, name) values (?, ?)", ParamConverter.identity(), Stream.empty());

            final List<Row> result = connection.query("select * from test").stream().toList();
            assertThat(result).isEmpty();
        }
    }

    @Test
    void batchInsert() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            connection.insert("insert into test (id, name) values (?, ?)", ParamConverter.identity(),
                    Stream.of(new Object[] { 1, "a" }, new Object[] { 2, "b" }, new Object[] { 3, "c" }));

            final List<Row> result = connection.query("select count(*) from test").stream().toList();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).columnValues()).hasSize(1);
            assertThat(result.get(0).get(0).value()).isEqualTo(3L);
        }
    }

    @Test
    void insert() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            connection.insert(Identifier.simple("TEST"), List.of(Identifier.simple("ID"), Identifier.simple("NAME")),
                    ParamConverter.identity(),
                    Stream.of(new Object[] { 1, "a" }, new Object[] { 2, "b" }, new Object[] { 3, "c" }));

            final List<List<Object>> result = connection.query("select * from test").stream()
                    .map(row -> row.columnValues().stream().map(value -> value.value()).toList()).toList();
            assertThat(result).hasSize(3);
            assertThat(result).isEqualTo(List.of(List.of(1, "a"), List.of(2, "b"), List.of(3, "c")));
        }
    }
}
