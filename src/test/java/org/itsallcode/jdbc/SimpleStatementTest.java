package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.sql.*;

import org.itsallcode.jdbc.dialect.GenericDialect;
import org.itsallcode.jdbc.resultset.ContextRowMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimpleStatementTest {
    @Mock
    Statement stmtMock;
    @Mock
    ResultSet resultSetMock;
    @Mock
    ResultSetMetaData metaDataMock;
    @Mock
    ContextRowMapper<?> rowMapperMock;

    SimpleStatement testee() {
        return new SimpleStatement(Context.builder().build(), GenericDialect.INSTANCE, stmtMock);
    }

    @Test
    void executeQuery() throws SQLException {
        when(stmtMock.executeQuery("sql")).thenReturn(resultSetMock);
        when(resultSetMock.getMetaData()).thenReturn(metaDataMock);
        assertThat(testee().executeQuery("sql", rowMapperMock)).isNotNull();
        verify(stmtMock).executeQuery("sql");
    }

    @Test
    void executeQueryFails() throws SQLException {
        when(stmtMock.executeQuery("sql")).thenThrow(new SQLException("expected"));
        final SimpleStatement testee = testee();
        assertThatThrownBy(() -> testee.executeQuery("sql", rowMapperMock))
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error executing query 'sql': expected");
    }

    @Test
    void executeUpdate() throws SQLException {
        when(stmtMock.executeUpdate("sql")).thenReturn(2);
        assertThat(testee().executeUpdate("sql")).isEqualTo(2);
    }

    @Test
    void executeUpdateFails() throws SQLException {
        when(stmtMock.executeUpdate("sql")).thenThrow(new SQLException("expected"));
        final SimpleStatement testee = testee();
        assertThatThrownBy(() -> testee.executeUpdate("sql"))
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error executing statement 'sql': expected");
    }

    @Test
    void addBatch() throws SQLException {
        testee().addBatch("sql");
        verify(stmtMock).addBatch("sql");
    }

    @Test
    void addBatchFails() throws SQLException {
        doThrow(new SQLException("expected")).when(stmtMock).addBatch("sql");
        final SimpleStatement testee = testee();
        assertThatThrownBy(() -> testee.addBatch("sql"))
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error adding batch: expected");
    }

    @Test
    void executeBatch() throws SQLException {
        when(stmtMock.executeBatch()).thenReturn(new int[] { 2 });
        assertThat(testee().executeBatch()).isEqualTo(new int[] { 2 });
    }

    @Test
    void executeBatchFails() throws SQLException {
        when(stmtMock.executeBatch()).thenThrow(new SQLException("expected"));
        final SimpleStatement testee = testee();
        assertThatThrownBy(testee::executeBatch)
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error executing batch: expected");
    }

    @Test
    void getStatement() {
        assertThat(testee().getStatement()).isSameAs(stmtMock);
    }

    @Test
    void close() throws SQLException {
        testee().close();
        verify(stmtMock).close();
    }

    @Test
    void closeFails() throws SQLException {
        doThrow(new SQLException("expected")).when(stmtMock).close();
        final SimpleStatement testee = testee();
        assertThatThrownBy(testee::close).isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error closing statement: expected");
    }
}
