package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.itsallcode.jdbc.dialect.H2Dialect;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimpleConnectionTest {

    private static final String SQL_STATEMENT = "query";
    @Mock
    Connection connectionMock;

    @Test
    void queryPrepareStatementFails() throws SQLException {
        when(connectionMock.prepareStatement(SQL_STATEMENT)).thenThrow(new SQLException("expected"));
        final SimpleConnection testee = testee();
        assertThatThrownBy(() -> testee.query(SQL_STATEMENT)).isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error preparing statement 'query': expected");
    }

    @Test
    void close() throws SQLException {
        testee().close();
        verify(connectionMock).close();
    }

    @Test
    void closeFails() throws SQLException {
        doThrow(new SQLException("expected")).when(connectionMock).close();
        assertThatThrownBy(testee()::close).isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error closing connection: expected");
    }

    @Test
    void setAutoCommit() throws SQLException {
        testee().setAutoCommit(false);
        verify(connectionMock).setAutoCommit(false);
    }

    @Test
    void setAutoCommitFails() throws SQLException {
        doThrow(new SQLException("expected")).when(connectionMock).setAutoCommit(false);
        final SimpleConnection testee = testee();
        assertThatThrownBy(() -> testee.setAutoCommit(false)).isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Failed to set autoCommit to false: expected");
    }

    @Test
    void commit() throws SQLException {
        testee().commit();
        verify(connectionMock).commit();
    }

    @Test
    void commitFails() throws SQLException {
        doThrow(new SQLException("expected")).when(connectionMock).commit();
        assertThatThrownBy(testee()::commit).isInstanceOf(UncheckedSQLException.class)
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
        assertThatThrownBy(testee()::rollback).isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Failed to rollback transaction: expected");
    }

    SimpleConnection testee() {
        return new SimpleConnection(connectionMock, Context.builder().build(), new H2Dialect());
    }
}
