package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.itsallcode.jdbc.dialect.H2Dialect;
import org.itsallcode.jdbc.resultset.RowMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimpleConnectionTest {

    private static final String SQL_STATEMENT = "query";
    @Mock
    Connection connectionMock;
    @Mock
    static PreparedStatementSetter preparedStatementSetterMock;
    @Mock
    static RowMapper<?> rowMapperMock;

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
    void closeClosesWrappedConnection() throws SQLException {
        final SimpleConnection testee = SimpleConnection.wrap(connectionMock, new H2Dialect());
        testee.close();
        verify(connectionMock).close();
    }

    static Stream<Arguments> operations() {
        return Stream.of(
                operation(con -> con.startTransaction()),
                operation(con -> con.executeScript("script")),
                operation(con -> con.executeStatement("sql")),
                operation(con -> con.executeStatement("sql", preparedStatementSetterMock)),
                operation(con -> con.executeStatement("sql", List.of())),
                operation(con -> con.query("sql")),
                operation(con -> con.query("sql", rowMapperMock)),
                operation(con -> con.query("sql", preparedStatementSetterMock, rowMapperMock)),
                operation(con -> con.query("sql", List.of(), rowMapperMock)),
                operation(con -> con.batchInsert()),
                operation(con -> con.batchInsert(null)));
    }

    static Arguments operation(final Consumer<SimpleConnection> operation) {
        return Arguments.of(operation);
    }

    @ParameterizedTest
    @MethodSource("operations")
    void operationSucceedsForOpenConnection(final Consumer<SimpleConnection> operation) throws SQLException {
        final SimpleConnection testee = testee();
        lenient().when(connectionMock.prepareStatement(anyString()))
                .thenReturn(mock(PreparedStatement.class, RETURNS_DEEP_STUBS));
        assertDoesNotThrow(() -> operation.accept(testee));
    }

    @ParameterizedTest
    @MethodSource("operations")
    void operationFailsAfterClose(final Consumer<SimpleConnection> operation) throws SQLException {
        final SimpleConnection testee = testee();
        when(connectionMock.isClosed()).thenReturn(true);
        assertThatThrownBy(() -> operation.accept(testee))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Operation not allowed on closed connection");
    }

    @ParameterizedTest
    @MethodSource("operations")
    void operationFailsWhenTransactionIsActive(final Consumer<SimpleConnection> operation) {
        final SimpleConnection testee = testee();
        testee.startTransaction();
        assertThatThrownBy(() -> operation.accept(testee))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Operation not allowed on connection when transaction is active");
    }

    @ParameterizedTest
    @MethodSource("operations")
    void operationSucceedsAfterTransactionIsClosed(final Consumer<SimpleConnection> operation) throws SQLException {
        final SimpleConnection testee = testee();
        lenient().when(connectionMock.prepareStatement(anyString()))
                .thenReturn(mock(PreparedStatement.class, RETURNS_DEEP_STUBS));
        testee.startTransaction().close();
        assertDoesNotThrow(() -> operation.accept(testee));
    }

    @ParameterizedTest
    @MethodSource("operations")
    void operationSucceedsAfterTransactionIsCommitted(final Consumer<SimpleConnection> operation) throws SQLException {
        final SimpleConnection testee = testee();
        lenient().when(connectionMock.prepareStatement(anyString()))
                .thenReturn(mock(PreparedStatement.class, RETURNS_DEEP_STUBS));
        testee.startTransaction().commit();
        assertDoesNotThrow(() -> operation.accept(testee));
    }

    @ParameterizedTest
    @MethodSource("operations")
    void operationSucceedsAfterTransactionIsRolledBack(final Consumer<SimpleConnection> operation) throws SQLException {
        final SimpleConnection testee = testee();
        lenient().when(connectionMock.prepareStatement(anyString()))
                .thenReturn(mock(PreparedStatement.class, RETURNS_DEEP_STUBS));
        testee.startTransaction().rollback();
        assertDoesNotThrow(() -> operation.accept(testee));
    }

    SimpleConnection testee() {
        return new SimpleConnection(connectionMock, Context.builder().build(), new H2Dialect());
    }
}
