package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.sql.*;
import java.util.*;
import java.util.stream.Stream;

import org.itsallcode.jdbc.batch.StatementBatch;
import org.itsallcode.jdbc.dialect.H2Dialect;
import org.itsallcode.jdbc.resultset.RowMapper;
import org.itsallcode.jdbc.resultset.SimpleResultSet;
import org.itsallcode.jdbc.resultset.generic.ColumnValue;
import org.itsallcode.jdbc.resultset.generic.Row;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SimpleConnectionITest {

    @Test
    void wrap() throws SQLException {
        try (Connection existingConnection = DriverManager.getConnection(H2TestFixture.H2_MEM_JDBC_URL);
                SimpleConnection connection = SimpleConnection.wrap(existingConnection, new H2Dialect())) {
            assertThat(connection.executeUpdate("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))")).isZero();
            assertDoesNotThrow(() -> connection.query("select count(*) from test"));
        }
    }

    @Test
    void executeStatement() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            assertThat(connection.executeUpdate("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))")).isZero();
            assertDoesNotThrow(() -> connection.query("select count(*) from test"));
        }
    }

    @Test
    void executeStatementWithParameter() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeUpdate("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            assertThat(connection.executeUpdate("INSERT INTO TEST VALUES (?,?), (?,?)", List.of(1, "a", 2, "b")))
                    .isEqualTo(2);
            assertThat(connection.query("select count(*) from test").toList().get(0).get(0).getValue()).isEqualTo(2L);
        }
    }

    @Test
    void executeStatementFails() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            assertThatThrownBy(() -> connection.executeUpdate("select count(*) from missing_table"))
                    .isInstanceOf(UncheckedSQLException.class)
                    .hasMessage(
                            "Error preparing statement 'select count(*) from missing_table': Table \"MISSING_TABLE\" not found (this database is empty); SQL statement:\n"
                                    + "select count(*) from missing_table [42104-232]")
                    .hasCauseInstanceOf(SQLException.class);
        }
    }

    @Test
    void executeScript() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255));"
                    + "insert into test (id, name) values (1, 'test');");
            assertDoesNotThrow(() -> connection.query("select count(*) from test"));
        }
    }

    @Test
    void executeQueryFails() {
        try (final SimpleConnection connection = H2TestFixture.createMemConnection()) {
            assertThatThrownBy(
                    () -> connection.query("select count(*) from missing_table"))
                    .isInstanceOf(UncheckedSQLException.class).hasMessage(
                            "Error preparing statement 'select count(*) from missing_table': Table \"MISSING_TABLE\" not found (this database is empty); SQL statement:\n"
                                    + "select count(*) from missing_table [42104-232]");
        }
    }

    @Test
    void executeQueryWithGenericRowMapper() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255));"
                    + "insert into test (id, name) values (1, 'test');");
            try (SimpleResultSet<Row> resultSet = connection.query("select count(*) as result from test")) {
                final List<Row> rows = resultSet.stream().toList();
                assertAll(
                        () -> assertThat(rows).hasSize(1),
                        () -> assertThat(rows.get(0).rowIndex()).isZero(),
                        () -> assertThat(rows.get(0).columnValues()).hasSize(1),
                        () -> assertThat(rows.get(0).get(0).value()).isEqualTo(1L),
                        () -> assertThat(rows.get(0).get(0, Long.class)).isEqualTo(1L));
            }
        }
    }

    static Stream<Arguments> rowMappers() {
        return Stream.of(
                mapper("type+label-lowercase", (rs, rowNum) -> rs.getInt("id") + ":" + rs.getString("name")),
                mapper("type+label-uppercase", (rs, rowNum) -> rs.getInt("ID") + ":" + rs.getString("NAME")),
                mapper("type+index", (rs, rowNum) -> rs.getInt(1) + ":" + rs.getString(2)),
                mapper("generic+label-lowercase", (rs, rowNum) -> rs.getObject("id") + ":" + rs.getObject("name")),
                mapper("generic+label-uppercase", (rs, rowNum) -> rs.getObject("ID") + ":" + rs.getObject("NAME")),
                mapper("generic+index", (rs, rowNum) -> rs.getObject(1) + ":" + rs.getObject(2)),
                mapper("specific type+label-lowercase",
                        (rs, rowNum) -> rs.getObject("id", Integer.class) + ":" + rs.getObject("name", String.class)),
                mapper("specific type+label-uppercase",
                        (rs, rowNum) -> rs.getObject("ID", Integer.class) + ":" + rs.getObject("NAME", String.class)),
                mapper("specific type+index",
                        (rs, rowNum) -> rs.getObject(1, Integer.class) + ":" + rs.getObject(2, String.class)));
    }

    static Arguments mapper(final String testName, final RowMapper<String> mapper) {
        return Arguments.of(testName, mapper);
    }

    @ParameterizedTest
    @MethodSource("rowMappers")
    void executeQueryWithCustomRowMapper(final String testName, final RowMapper<String> mapper) {
        final ConnectionFactory factory = ConnectionFactory.create(Context.builder().build());
        try (SimpleConnection connection = factory.create(H2TestFixture.H2_MEM_JDBC_URL)) {
            try (SimpleResultSet<String> resultSet = connection.query(
                    "select t.* from (values (1, 'a'), (2, 'b'), (3, 'c')) as t(id, name)", mapper)) {
                final List<String> rows = resultSet.toList();
                assertThat(rows).containsExactly("1:a", "2:b", "3:c");
            }
        }
    }

    @Test
    void executeQueryEmptyResult() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            try (SimpleResultSet<Row> resultSet = connection.query("select * from test")) {
                final Iterator<Row> iterator = resultSet.iterator();
                assertAll(
                        () -> assertThat(iterator.hasNext()).isFalse(),
                        () -> assertThatThrownBy(iterator::next).isInstanceOf(NoSuchElementException.class));
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
                                .isEqualTo(JDBCType.INTEGER),
                        () -> assertThat(firstRow.columnValues().get(0).type().typeName())
                                .isEqualTo("INTEGER"),
                        () -> assertThat(firstRow.columnValues().get(0).type().className())
                                .isEqualTo(Integer.class.getName()),
                        () -> assertThat(firstRow.columnValues().get(0).type().displaySize()).isEqualTo(11),
                        () -> assertThat(firstRow.columnValues().get(0).type().precision()).isEqualTo(32),
                        () -> assertThat(firstRow.columnValues().get(0).type().scale()).isZero(),

                        () -> assertThat(firstRow.columnValues().get(1).value()).isEqualTo("test"),
                        () -> assertThat(firstRow.columnValues().get(1).type().jdbcType())
                                .isEqualTo(JDBCType.VARCHAR),
                        () -> assertThat(firstRow.columnValues().get(1).type().typeName())
                                .isEqualTo("CHARACTER VARYING"),
                        () -> assertThat(firstRow.columnValues().get(1).type().className())
                                .isEqualTo(String.class.getName()),
                        () -> assertThat(firstRow.columnValues().get(1).type().displaySize()).isEqualTo(255),
                        () -> assertThat(firstRow.columnValues().get(1).type().precision()).isEqualTo(255),
                        () -> assertThat(firstRow.columnValues().get(1).type().scale()).isZero(),

                        () -> assertThat(iterator.hasNext()).isFalse(),
                        () -> assertThatThrownBy(iterator::next).isInstanceOf(NoSuchElementException.class));
            }
        }
    }

    @Test
    void executeQueryWithParameter() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeUpdate("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            connection.executeUpdate("INSERT INTO TEST VALUES (?,?), (?,?)", List.of(1, "a", 2, "b"));
            final List<String> result = connection.query("select * from test where id=?", List.of(2),
                    (rs, idx) -> idx + "-" + rs.getString(2) + "-" + rs.getInt(1)).toList();
            assertThat(result.get(0)).isEqualTo("0-b-2");
        }
    }

    @Test
    void executeQueryOnlyOneIteratorAllowed() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            try (SimpleResultSet<Row> resultSet = connection.query("select * from test")) {
                final Iterator<Row> iterator = resultSet.iterator();
                assertAll(
                        () -> assertThat(iterator).isNotNull(),
                        () -> assertThatThrownBy(resultSet::iterator).isInstanceOf(IllegalStateException.class)
                                .hasMessage("Only one iterator allowed per ResultSet"));
            }
        }
    }

    @Test
    void batchInsertEmptyInput() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            connection.batchInsert(Object[].class).into("TEST", List.of("ID", "NAME"))
                    .mapping(ParamConverter.identity())
                    .rows(Stream.empty()).start();

            final List<Row> result = connection.query("select * from test").stream().toList();
            assertThat(result).isEmpty();
        }
    }

    @Test
    void batchInsert() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            connection.batchInsert(Object[].class).into("TEST", List.of("ID", "NAME"))
                    .mapping(ParamConverter.identity()).rows(
                            Stream.of(new Object[] { 1, "a" }, new Object[] { 2, "b" }, new Object[] { 3, "c" }))
                    .start();

            final List<Row> result = connection.query("select count(*) from test").stream().toList();
            assertAll(
                    () -> assertThat(result).hasSize(1),
                    () -> assertThat(result.get(0).columnValues()).hasSize(1),
                    () -> assertThat(result.get(0).get(0).value()).isEqualTo(3L));
        }
    }

    @Test
    void insert() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            connection.batchInsert(Object[].class).into("TEST", List.of("ID", "NAME"))
                    .mapping(ParamConverter.identity())
                    .rows(
                            Stream.of(new Object[] { 1, "a" }, new Object[] { 2, "b" }, new Object[] { 3, "c" }))
                    .start();

            final List<List<Object>> result = connection.query("select * from test").stream()
                    .map(row -> row.columnValues().stream().map(ColumnValue::value).toList()).toList();
            assertAll(
                    () -> assertThat(result).hasSize(3),
                    () -> assertThat(result).isEqualTo(List.of(List.of(1, "a"), List.of(2, "b"), List.of(3, "c"))));
        }
    }

    @Test
    void batchStatement() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
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
    void multipleTransactions() {
        try (SimpleConnection connection = H2TestFixture.createMemConnection()) {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            try (Transaction tx = connection.startTransaction()) {
                tx.executeUpdate("INSERT INTO TEST VALUES (?,?), (?,?)", List.of(1, "a", 2, "b"));
                assertThat(getRowCount(tx, "test")).isEqualTo(2);
                tx.commit();
            }
            assertThat(getRowCount(connection, "test")).isEqualTo(2);
            try (Transaction tx = connection.startTransaction()) {
                tx.executeUpdate("DELETE FROM TEST WHERE ID = ?", List.of(1));
                assertThat(getRowCount(tx, "test")).isEqualTo(1);
            }
            assertThat(getRowCount(connection, "test")).isEqualTo(2);
        }
    }

    private long getRowCount(final DbOperations dbOperations, final String tableName) {
        return dbOperations.query("select count(*) from %s".formatted(tableName)).toList().get(0).get(0)
                .getValue(Long.class);
    }
}
