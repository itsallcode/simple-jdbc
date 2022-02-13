package org.itsallcode.jdbc;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.itsallcode.jdbc.resultset.ResultSetRow;
import org.itsallcode.jdbc.resultset.SimpleResultSet;
import org.junit.jupiter.api.Test;

class SimpleConnectionITest
{
    @Test
    void executeStatement()
    {
        try (SimpleConnection connection = H2TestFixture.createMemConnection())
        {
            connection.executeStatement("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            assertDoesNotThrow(() -> connection.executeStatement("select count(*) from test"));
        }
    }

    @Test
    void executeStatementFails()
    {
        try (SimpleConnection connection = H2TestFixture.createMemConnection())
        {
            assertThatThrownBy(() -> connection.executeStatement("select count(*) from missingtable"))
                    .isInstanceOf(UncheckedSQLException.class)
                    .hasMessage("Error executing 'select count(*) from missingtable'")
                    .hasCauseInstanceOf(SQLException.class);
        }
    }

    @Test
    void executeScript()
    {
        try (SimpleConnection connection = H2TestFixture.createMemConnection())
        {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255));"
                    + "insert into test (id, name) values (1, 'test');");
            assertDoesNotThrow(() -> connection.executeStatement("select count(*) from test"));
        }
    }

    @Test
    void executeQuery()
    {
        try (SimpleConnection connection = H2TestFixture.createMemConnection())
        {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255));"
                    + "insert into test (id, name) values (1, 'test');");
            try (SimpleResultSet<ResultSetRow> resultSet = connection.query("select count(*) from test"))
            {
                final List<ResultSetRow> rows = resultSet.stream().collect(toList());
                assertThat(rows).hasSize(1);
                assertThat(rows.get(0).getRowIndex()).isZero();
                assertThat(rows.get(0).getColumnValues()).hasSize(1);
                assertThat(rows.get(0).getColumnValue(0).getValue()).isEqualTo(1L);
            }
        }
    }

    @Test
    void executeQueryEmptyResult()
    {
        try (SimpleConnection connection = H2TestFixture.createMemConnection())
        {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            try (SimpleResultSet<ResultSetRow> resultSet = connection.query("select * from test"))
            {
                final Iterator<ResultSetRow> iterator = resultSet.iterator();
                assertThat(iterator.hasNext()).isFalse();
                assertThatThrownBy(() -> iterator.next()).isInstanceOf(NoSuchElementException.class);
            }
        }
    }

    @Test
    void executeQuerySingleRow()
    {
        try (final SimpleConnection connection = H2TestFixture.createMemConnection())
        {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255));"
                    + "insert into test (id, name) values (1, 'test');");
            try (final SimpleResultSet<ResultSetRow> resultSet = connection.query("select * from test"))
            {
                final Iterator<ResultSetRow> iterator = resultSet.iterator();
                assertThat(iterator.hasNext()).isTrue();
                final ResultSetRow firstRow = iterator.next();

                assertAll(
                        () -> assertThat(firstRow.getRowIndex()).isZero(),
                        () -> assertThat(firstRow.getColumnValues()).hasSize(2),
                        () -> assertThat(firstRow.getColumnValues().get(0).getValue()).isEqualTo(1),
                        () -> assertThat(firstRow.getColumnValues().get(0).getType().getJdbcType())
                                .isEqualTo(Types.INTEGER),
                        () -> assertThat(firstRow.getColumnValues().get(0).getType().getTypeName())
                                .isEqualTo("INTEGER"),
                        () -> assertThat(firstRow.getColumnValues().get(0).getType().getClassName())
                                .isEqualTo(Integer.class.getName()),
                        () -> assertThat(firstRow.getColumnValues().get(0).getType().getDisplaySize()).isEqualTo(11),
                        () -> assertThat(firstRow.getColumnValues().get(0).getType().getPrecision()).isEqualTo(32),
                        () -> assertThat(firstRow.getColumnValues().get(0).getType().getScale()).isZero(),

                        () -> assertThat(firstRow.getColumnValues().get(1).getValue()).isEqualTo("test"),
                        () -> assertThat(firstRow.getColumnValues().get(1).getType().getJdbcType())
                                .isEqualTo(Types.VARCHAR),
                        () -> assertThat(firstRow.getColumnValues().get(1).getType().getTypeName())
                                .isEqualTo("CHARACTER VARYING"),
                        () -> assertThat(firstRow.getColumnValues().get(1).getType().getClassName())
                                .isEqualTo(String.class.getName()),
                        () -> assertThat(firstRow.getColumnValues().get(1).getType().getDisplaySize()).isEqualTo(255),
                        () -> assertThat(firstRow.getColumnValues().get(1).getType().getPrecision()).isEqualTo(255),
                        () -> assertThat(firstRow.getColumnValues().get(1).getType().getScale()).isZero(),

                        () -> assertThat(iterator.hasNext()).isFalse(),
                        () -> assertThatThrownBy(() -> iterator.next()).isInstanceOf(NoSuchElementException.class));
            }
        }
    }

    @Test
    void executeQueryOnlyOneIteratorAllowed()
    {
        try (SimpleConnection connection = H2TestFixture.createMemConnection())
        {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            try (SimpleResultSet<ResultSetRow> resultSet = connection.query("select * from test"))
            {
                final Iterator<ResultSetRow> iterator = resultSet.iterator();
                assertThat(iterator).isNotNull();
                assertThatThrownBy(() -> resultSet.iterator()).isInstanceOf(IllegalStateException.class)
                        .hasMessage("Only one iterator allowed per ResultSet");
            }
        }
    }

    @Test
    void batchInsertEmptyInput()
    {
        try (SimpleConnection connection = H2TestFixture.createMemConnection())
        {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            connection.insert("insert into test (id, name) values (?, ?)", ParamConverter.identity(),
                    Stream.empty());

            final List<ResultSetRow> result = connection.query("select * from test")
                    .stream().toList();
            assertThat(result).isEmpty();
        }
    }

    @Test
    void batchInsert()
    {
        try (SimpleConnection connection = H2TestFixture.createMemConnection())
        {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255))");
            connection.insert("insert into test (id, name) values (?, ?)", ParamConverter.identity(),
                    Stream.of(new Object[]
                    { 1, "a" }, new Object[] { 2, "b" }, new Object[] { 3, "c" }));

            final List<ResultSetRow> result = connection.query("select count(*) from test")
                    .stream().toList();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getColumnValues()).hasSize(1);
            assertThat(result.get(0).getColumnValue(0).getValue()).isEqualTo(3L);
        }
    }
}
