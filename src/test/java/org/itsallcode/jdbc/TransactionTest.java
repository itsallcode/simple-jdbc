package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.itsallcode.jdbc.resultset.RowMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionTest {
    @Mock
    SimpleConnection connectionMock;
    @Mock
    Connection rawConnectionMock;
    @Mock
    static PreparedStatementSetter preparedStatementSetterMock;
    @Mock
    static RowMapper<?> rowMapperMock;

    @Test
    void startDisablesAutoCommitWhenEnabledBefore() throws SQLException {
        when(rawConnectionMock.getAutoCommit()).thenReturn(true);
        Transaction.start(rawConnectionMock, connectionMock);
        verify(rawConnectionMock).setAutoCommit(false);
    }

    @Test
    void startDoesNotDisableAutoCommitWhenAlreadyDisabled() throws SQLException {
        when(rawConnectionMock.getAutoCommit()).thenReturn(false);
        Transaction.start(rawConnectionMock, connectionMock);
        verify(rawConnectionMock, never()).setAutoCommit(anyBoolean());
    }

    @Test
    void closeDoesNotRestoreAutoCommitWhenAlreadyDisabled() throws SQLException {
        when(rawConnectionMock.getAutoCommit()).thenReturn(false);
        Transaction.start(rawConnectionMock, connectionMock).close();
        verify(rawConnectionMock, never()).setAutoCommit(anyBoolean());
    }

    @Test
    void closeRestoresAutoCommit() throws SQLException {
        when(rawConnectionMock.getAutoCommit()).thenReturn(true);
        final InOrder inOrder = inOrder(rawConnectionMock, connectionMock);
        Transaction.start(rawConnectionMock, connectionMock).close();
        inOrder.verify(rawConnectionMock).setAutoCommit(false);
        inOrder.verify(rawConnectionMock).setAutoCommit(true);
        inOrder.verifyNoMoreInteractions();
    }

    static Stream<Arguments> operations() {
        return Stream.of(
                operation(tx -> tx.executeScript("script")),
                operation(tx -> tx.executeStatement("sql")),
                operation(tx -> tx.executeStatement("sql", preparedStatementSetterMock)),
                operation(tx -> tx.executeStatement("sql", List.of())),
                operation(tx -> tx.query("sql")),
                operation(tx -> tx.query("sql", rowMapperMock)),
                operation(tx -> tx.query("sql", preparedStatementSetterMock, rowMapperMock)),
                operation(tx -> tx.query("sql", List.of(), rowMapperMock)),
                operation(tx -> tx.batchInsert()),
                operation(tx -> tx.batchInsert(null)),
                operation(tx -> tx.commit()),
                operation(tx -> tx.rollback()));
    }

    static Arguments operation(final Consumer<Transaction> operation) {
        return Arguments.of(operation);
    }

    @ParameterizedTest
    @MethodSource("operations")
    void operationSucceedsForValidTransaction(final Consumer<Transaction> operation) {
        final Transaction testee = testee();
        assertDoesNotThrow(() -> operation.accept(testee));
    }

    @ParameterizedTest
    @MethodSource("operations")
    void operationFailsAfterClose(final Consumer<Transaction> operation) {
        final Transaction testee = testee();
        testee.close();
        assertThatThrownBy(() -> operation.accept(testee))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Operation not allowed on closed transaction");
    }

    @ParameterizedTest
    @MethodSource("operations")
    void operationFailsAfterCommit(final Consumer<Transaction> operation) {
        final Transaction testee = testee();
        testee.commit();
        assertThatThrownBy(() -> operation.accept(testee))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Operation not allowed on committed transaction");
    }

    @ParameterizedTest
    @MethodSource("operations")
    void operationFailsAfterRollback(final Consumer<Transaction> operation) {
        final Transaction testee = testee();
        testee.rollback();
        assertThatThrownBy(() -> operation.accept(testee))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Operation not allowed on rolled back transaction");
    }

    @Test
    void closingRollsBack() throws SQLException {
        final Transaction testee = testee();
        testee.close();
        verify(rawConnectionMock).rollback();
    }

    @Test
    void closingDoesNotCloseConnection() throws SQLException {
        final Transaction testee = testee();
        testee.close();
        verify(connectionMock, never()).close();
        verify(rawConnectionMock, never()).close();
    }

    @Test
    void closingAlreadyClosedTransactionSucceeds() {
        final Transaction testee = testee();
        testee.close();
        assertDoesNotThrow(testee::close);
    }

    @Test
    void closingAlreadyClosedTransactionRollsBackOnlyOnce() throws SQLException {
        final Transaction testee = testee();
        testee.close();
        testee.close();
        verify(rawConnectionMock, times(1)).rollback();
    }

    @Test
    void closingRolledBackTransactionDoesNotRollback() throws SQLException {
        final Transaction testee = testee();
        testee.rollback();
        testee.close();
        verify(rawConnectionMock, times(1)).rollback();
    }

    @Test
    void closingCommittedTransactionDoesNotRollback() throws SQLException {
        final Transaction testee = testee();
        testee.commit();
        testee.close();
        verify(rawConnectionMock, never()).rollback();
    }

    @Test
    void setAutoCommit() throws SQLException {
        Transaction.setAutoCommit(rawConnectionMock, false);
        verify(rawConnectionMock).setAutoCommit(false);
    }

    @Test
    void setAutoCommitFails() throws SQLException {
        doThrow(new SQLException("expected")).when(rawConnectionMock).setAutoCommit(false);
        assertThatThrownBy(() -> Transaction.setAutoCommit(rawConnectionMock, false))
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Failed to set autoCommit to false: expected");
    }

    @Test
    void getAutoCommit() throws SQLException {
        when(rawConnectionMock.getAutoCommit()).thenReturn(true);
        assertThat(Transaction.getAutoCommit(rawConnectionMock)).isTrue();
    }

    @Test
    void getAutoCommitFails() throws SQLException {
        doThrow(new SQLException("expected")).when(rawConnectionMock).getAutoCommit();
        assertThatThrownBy(() -> Transaction.getAutoCommit(rawConnectionMock)).isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Failed to get autoCommit: expected");
    }

    @Test
    void commit() throws SQLException {
        testee().commit();
        verify(rawConnectionMock).commit();
    }

    @Test
    void commitFails() throws SQLException {
        doThrow(new SQLException("expected")).when(rawConnectionMock).commit();
        assertThatThrownBy(testee()::commit).isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Failed to commit transaction: expected");
    }

    @Test
    void rollback() throws SQLException {
        testee().rollback();
        verify(rawConnectionMock).rollback();
    }

    @Test
    void rollbackFails() throws SQLException {
        doThrow(new SQLException("expected")).when(rawConnectionMock).rollback();
        assertThatThrownBy(testee()::rollback).isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Failed to rollback transaction: expected");
    }

    private Transaction testee() {
        return Transaction.start(rawConnectionMock, connectionMock);
    }
}
