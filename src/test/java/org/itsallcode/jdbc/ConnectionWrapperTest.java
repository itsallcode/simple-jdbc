package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.*;

import org.itsallcode.jdbc.dialect.H2Dialect;
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
    PreparedStatement preparedStatementMock;

    ConnectionWrapper testee() {
        return new ConnectionWrapper(connectionMock, Context.builder().build(), new H2Dialect());
    }

    @Test
    void executeStatementPrepareFails() throws SQLException {
        when(connectionMock.prepareStatement("sql")).thenThrow(new SQLException("expected"));
        final ConnectionWrapper testee = testee();
        assertThatThrownBy(() -> testee.executeStatement("sql"))
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error preparing statement 'sql': expected");
    }

    @Test
    void executeStatementExecuteFails() throws SQLException {
        when(connectionMock.prepareStatement("sql")).thenReturn(preparedStatementMock);
        when(preparedStatementMock.execute()).thenThrow(new SQLException("expected"));
        final ConnectionWrapper testee = testee();
        assertThatThrownBy(() -> testee.executeStatement("sql"))
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error executing statement 'sql': expected");
    }

    @Test
    void executeStatementCloseFails() throws SQLException {
        when(connectionMock.prepareStatement("sql")).thenReturn(preparedStatementMock);
        doThrow(new SQLException("expected")).when(preparedStatementMock).close();
        final ConnectionWrapper testee = testee();
        assertThatThrownBy(() -> testee.executeStatement("sql"))
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error closing statement: expected");
    }

    @Test
    void executeStatement() throws SQLException {
        when(connectionMock.prepareStatement("sql")).thenReturn(preparedStatementMock);
        testee().executeStatement("sql");
        final InOrder inOrder = inOrder(connectionMock, preparedStatementMock);
        inOrder.verify(connectionMock).prepareStatement("sql");
        inOrder.verify(preparedStatementMock).execute();
        inOrder.verify(preparedStatementMock).close();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void executeStatementWithPreparedStatementSetter() throws SQLException {
        when(connectionMock.prepareStatement("sql")).thenReturn(preparedStatementMock);
        testee().executeStatement("sql", ps -> {
            ps.setString(1, "one");
        });
        final InOrder inOrder = inOrder(connectionMock, preparedStatementMock);
        inOrder.verify(connectionMock).prepareStatement("sql");
        inOrder.verify(preparedStatementMock).setString(1, "one");
        inOrder.verify(preparedStatementMock).execute();
        inOrder.verify(preparedStatementMock).close();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void executeScriptEmptyString() throws SQLException {
        testee().executeScript("");
        verifyNoInteractions(connectionMock);
    }

    @ParameterizedTest
    @ValueSource(strings = { "sql script", "sql script;", "sql script\n", "sql script\t", " sql script", "\nsql script",
            "sql script;", "sql script;\n", "sql script\n;", "sql script;;", "sql script;;", ";sql script",
            " ; ; sql script" })
    void executeScriptWithoutTrailingSemicolon(final String script) throws SQLException {
        when(connectionMock.prepareStatement("sql script")).thenReturn(preparedStatementMock);
        testee().executeScript(script);
        verify(connectionMock).prepareStatement("sql script");
        verifyNoMoreInteractions(connectionMock);
    }

    @Test
    void executeScriptRunsMultipleCommands() throws SQLException {
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        testee().executeScript("script 1; script 2; script 3");
        verify(connectionMock).prepareStatement("script 1");
        verify(connectionMock).prepareStatement("script 2");
        verify(connectionMock).prepareStatement("script 3");
        verifyNoMoreInteractions(connectionMock);
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
}
