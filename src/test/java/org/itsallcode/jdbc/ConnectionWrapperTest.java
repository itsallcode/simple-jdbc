package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.sql.*;
import java.util.List;
import java.util.stream.Stream;

import org.itsallcode.jdbc.batch.PreparedStatementBatch;
import org.itsallcode.jdbc.dialect.GenericDialect;
import org.itsallcode.jdbc.resultset.ContextRowMapper;
import org.itsallcode.jdbc.resultset.SimpleResultSet;
import org.itsallcode.jdbc.resultset.generic.Row;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConnectionWrapperTest {

    @Mock
    Connection connectionMock;
    @Mock
    Statement statementMock;
    @Mock
    PreparedStatement preparedStatementMock;

    ConnectionWrapper testee() {
        return new ConnectionWrapper(connectionMock, Context.builder().build(), GenericDialect.INSTANCE);
    }

    @Test
    void executeStatementPrepareFails() throws SQLException {
        when(connectionMock.createStatement()).thenThrow(new SQLException("expected"));
        final ConnectionWrapper testee = testee();
        assertThatThrownBy(() -> testee.executeUpdate("sql"))
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error creating statement: expected");
    }

    @Test
    void executeStatementExecuteFails() throws SQLException {
        when(connectionMock.createStatement()).thenReturn(statementMock);
        when(statementMock.executeUpdate("sql")).thenThrow(new SQLException("expected"));
        final ConnectionWrapper testee = testee();
        assertThatThrownBy(() -> testee.executeUpdate("sql"))
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error executing statement 'sql': expected");
    }

    @Test
    void executeStatementCloseFails() throws SQLException {
        when(connectionMock.createStatement()).thenReturn(statementMock);
        doThrow(new SQLException("expected")).when(statementMock).close();
        final ConnectionWrapper testee = testee();
        assertThatThrownBy(() -> testee.executeUpdate("sql"))
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error closing statement: expected");
    }

    @Test
    void executeStatement() throws SQLException {
        when(connectionMock.createStatement()).thenReturn(statementMock);
        testee().executeUpdate("sql");
        final InOrder inOrder = inOrder(connectionMock, statementMock);
        inOrder.verify(connectionMock).createStatement();
        inOrder.verify(statementMock).executeUpdate("sql");
        inOrder.verify(statementMock).close();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void executeStatementWithPreparedStatementSetter() throws SQLException {
        when(connectionMock.prepareStatement("sql")).thenReturn(preparedStatementMock);
        testee().executeUpdate("sql", ps -> {
            ps.setString(1, "one");
        });
        final InOrder inOrder = inOrder(connectionMock, preparedStatementMock);
        inOrder.verify(connectionMock).prepareStatement("sql");
        inOrder.verify(preparedStatementMock).setString(1, "one");
        inOrder.verify(preparedStatementMock).executeUpdate();
        inOrder.verify(preparedStatementMock).close();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void queryWithPreparedStatementSetterAndRowMapper() throws SQLException {
        when(connectionMock.prepareStatement("sql")).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(mock(ResultSet.class, RETURNS_DEEP_STUBS));
        final SimpleResultSet<Row> result = testee().query("sql", ps -> {
            ps.setString(1, "one");
        }, ContextRowMapper.generic(GenericDialect.INSTANCE));
        assertThat(result).isNotNull();
        final InOrder inOrder = inOrder(connectionMock, preparedStatementMock);
        inOrder.verify(connectionMock).prepareStatement("sql");
        inOrder.verify(preparedStatementMock).setString(1, "one");
        inOrder.verify(preparedStatementMock).executeQuery();
        inOrder.verify(preparedStatementMock, never()).close();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void preparedStatementFails() throws SQLException {
        when(connectionMock.prepareStatement("sql")).thenThrow(new SQLException("expected"));
        final ConnectionWrapper testee = testee();
        assertThatThrownBy(() -> testee.executeUpdate("sql", ps -> {
            ps.setString(1, "one");
        })).isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error preparing statement 'sql': expected");
    }

    @Test
    void query() throws SQLException {
        when(connectionMock.createStatement()).thenReturn(statementMock);
        when(statementMock.executeQuery("sql")).thenReturn(mock(ResultSet.class, RETURNS_DEEP_STUBS));
        final SimpleResultSet<Row> result = testee().query("sql");
        assertThat(result).isNotNull();
    }

    @Test
    void batchInsert() throws SQLException {
        when(connectionMock.prepareStatement("insert into \"tab\" (\"c1\",\"c2\") values (?,?)"))
                .thenReturn(preparedStatementMock);
        final PreparedStatementBatch batch = testee().preparedStatementBatch().into("tab", List.of("c1", "c2"))
                .maxBatchSize(4)
                .build();
        batch.add(ps -> {
            ps.setString(1, "one");
        });
        batch.close();
        final InOrder inOrder = inOrder(connectionMock, preparedStatementMock);
        inOrder.verify(connectionMock).prepareStatement("insert into \"tab\" (\"c1\",\"c2\") values (?,?)");
        inOrder.verify(preparedStatementMock).setString(1, "one");
        inOrder.verify(preparedStatementMock).addBatch();
        inOrder.verify(preparedStatementMock).executeBatch();
        inOrder.verify(preparedStatementMock).close();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void rowBatchInsert() throws SQLException {
        when(connectionMock.prepareStatement("insert into \"tab\" (\"c1\",\"c2\") values (?,?)"))
                .thenReturn(preparedStatementMock);
        testee().rowPreparedStatementBatch().into("tab", List.of("c1", "c2")).mapping(row -> new Object[] { row })
                .rows(Stream.of("one")).maxBatchSize(4).start();
        final InOrder inOrder = inOrder(connectionMock, preparedStatementMock);
        inOrder.verify(connectionMock).prepareStatement("insert into \"tab\" (\"c1\",\"c2\") values (?,?)");
        inOrder.verify(preparedStatementMock).setObject(1, "one");
        inOrder.verify(preparedStatementMock).addBatch();
        inOrder.verify(preparedStatementMock).executeBatch();
        inOrder.verify(preparedStatementMock, times(2)).close(); // TODO: why twice?
        inOrder.verifyNoMoreInteractions();
    }

    @ParameterizedTest
    @ValueSource(strings = { "", " ", "\n", "\t", " \n\t", ";" })
    void executeScriptEmptyString(final String script) {
        testee().executeScript(script);
        verifyNoInteractions(connectionMock);
    }

    @ParameterizedTest
    @ValueSource(strings = { "sql script", "sql script;", "sql script\n", "sql script\t", " sql script", "\nsql script",
            "sql script;", "sql script;\n", "sql script\n;", "sql script;;", "sql script;;", ";sql script",
            " ; ; sql script" })
    void executeScriptWithoutTrailingSemicolon(final String script) throws SQLException {
        when(connectionMock.createStatement()).thenReturn(statementMock);
        testee().executeScript(script);
        verify(statementMock).addBatch("sql script");
        verify(statementMock).executeBatch();
        verifyNoMoreInteractions(connectionMock);
    }

    @Test
    void executeScriptRunsMultipleCommands() throws SQLException {
        when(connectionMock.createStatement()).thenReturn(statementMock);
        testee().executeScript("script 1; script 2; script 3");
        final InOrder inOrder = inOrder(connectionMock, statementMock);
        inOrder.verify(statementMock).addBatch("script 1");
        inOrder.verify(statementMock).addBatch("script 2");
        inOrder.verify(statementMock).addBatch("script 3");
        inOrder.verify(statementMock).executeBatch();
        inOrder.verify(statementMock).close();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void setAutoCommit() throws SQLException {
        testee().setAutoCommit(true);
        verify(connectionMock).setAutoCommit(true);
    }

    @Test
    void setAutoCommitFails() throws SQLException {
        doThrow(new SQLException("expected")).when(connectionMock).setAutoCommit(true);
        final ConnectionWrapper testee = testee();
        assertThatThrownBy(() -> testee.setAutoCommit(true))
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Failed to set autoCommit to true: expected");
    }

    @Test
    void isAutoCommitEnabled() throws SQLException {
        when(connectionMock.getAutoCommit()).thenReturn(true);
        assertThat(testee().isAutoCommitEnabled()).isTrue();
    }

    @Test
    void isAutoCommitEnabledFails() throws SQLException {
        doThrow(new SQLException("expected")).when(connectionMock).getAutoCommit();
        final ConnectionWrapper testee = testee();
        assertThatThrownBy(testee::isAutoCommitEnabled)
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Failed to get autoCommit: expected");
    }

    @Test
    void commit() throws SQLException {
        testee().commit();
        verify(connectionMock).commit();
    }

    @Test
    void commitFails() throws SQLException {
        doThrow(new SQLException("expected")).when(connectionMock).commit();
        final ConnectionWrapper testee = testee();
        assertThatThrownBy(testee::commit)
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Failed to commit transaction: expected");
    }

    @Test
    void rollback() throws SQLException {
        testee().rollback();
        verify(connectionMock).rollback();
    }

    @Test
    void rollbackFails() throws SQLException {
        doThrow(new SQLException("expected")).when(connectionMock).rollback();
        final ConnectionWrapper testee = testee();
        assertThatThrownBy(testee::rollback)
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Failed to rollback transaction: expected");
    }

    @Test
    void close() throws SQLException {
        testee().close();
        verify(connectionMock).close();
    }

    @Test
    void closeFails() throws SQLException {
        doThrow(new SQLException("expected")).when(connectionMock).close();
        final ConnectionWrapper testee = testee();
        assertThatThrownBy(testee::close)
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error closing connection: expected");
    }

    @Test
    void isClosed() throws SQLException {
        when(connectionMock.isClosed()).thenReturn(true);
        assertThat(testee().isClosed()).isTrue();
    }

    @Test
    void isClosedFails() throws SQLException {
        doThrow(new SQLException("expected")).when(connectionMock).isClosed();
        final ConnectionWrapper testee = testee();
        assertThatThrownBy(testee::isClosed)
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Failed to get closed state: expected");
    }

    @Test
    void getMetaData() {
        assertThat(testee().getMetaData()).isNotNull();
    }

    @Test
    void getMetaDataFails() throws SQLException {
        when(connectionMock.getMetaData()).thenThrow(new SQLException("expected"));
        final ConnectionWrapper testee = testee();
        assertThatThrownBy(testee::getMetaData)
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Failed to get metadata: expected");
    }

    @Test
    void getOriginalConnection() {
        assertThat(testee().getOriginalConnection()).isSameAs(connectionMock);
    }
}
