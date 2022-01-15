package org.itsallcode.jdbc;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.SQLException;
import java.util.List;

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
            connection.executeStatement("select count(*) from test");
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
            connection.executeStatement("select count(*) from test");
        }
    }

    @Test
    void executeQuery()
    {
        try (SimpleConnection connection = H2TestFixture.createMemConnection())
        {
            connection.executeScript("CREATE TABLE TEST(ID INT, NAME VARCHAR(255));"
                    + "insert into test (id, name) values (1, 'test');");
            try (SimpleResultSet resultSet = connection.prepareStatement("select count(*) from test").executeQuery())
            {
                final List<ResultSetRow> rows = resultSet.stream().collect(toList());
                assertThat(rows).hasSize(1);
                assertThat(rows.get(0).getRowIndex()).isEqualTo(0);
                assertThat(rows.get(0).getColumnValues()).hasSize(1);
                assertThat(rows.get(0).getColumnValue(0).getValue()).isEqualTo(1L);
            }
        }
    }
}
