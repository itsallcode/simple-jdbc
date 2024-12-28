package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

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
    ConnectionWrapper connectionMock;
    @Mock
    Consumer<Transaction> transactionFinishedCallbackMock;
    @Mock
    static PreparedStatementSetter preparedStatementSetterMock;
    @Mock
    static RowMapper<?> rowMapperMock;

    @Test
    void startDisablesAutoCommitWhenEnabledBefore() throws SQLException {
        when(connectionMock.isAutoCommitEnabled()).thenReturn(true);
        startTransaction();
        verify(connectionMock).setAutoCommit(false);
    }

    @Test
    void startDoesNotDisableAutoCommitWhenAlreadyDisabled() throws SQLException {
        when(connectionMock.isAutoCommitEnabled()).thenReturn(false);
        startTransaction();
        verify(connectionMock, never()).setAutoCommit(anyBoolean());
    }

    @Test
    void closeDoesNotRestoreAutoCommitWhenAlreadyDisabled() throws SQLException {
        when(connectionMock.isAutoCommitEnabled()).thenReturn(false);
        startTransaction().close();
        verify(connectionMock, never()).setAutoCommit(anyBoolean());
    }

    @Test
    void closeRestoresAutoCommit() throws SQLException {
        when(connectionMock.isAutoCommitEnabled()).thenReturn(true);
        final InOrder inOrder = inOrder(connectionMock, connectionMock);
        startTransaction().close();
        inOrder.verify(connectionMock).setAutoCommit(false);
        inOrder.verify(connectionMock).setAutoCommit(true);
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
        final Transaction testee = startTransaction();
        assertDoesNotThrow(() -> operation.accept(testee));
    }

    @ParameterizedTest
    @MethodSource("operations")
    void operationFailsAfterClose(final Consumer<Transaction> operation) {
        final Transaction testee = startTransaction();
        testee.close();
        assertThatThrownBy(() -> operation.accept(testee))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Operation not allowed on closed transaction");
    }

    @ParameterizedTest
    @MethodSource("operations")
    void operationFailsAfterCommit(final Consumer<Transaction> operation) {
        final Transaction testee = startTransaction();
        testee.commit();
        assertThatThrownBy(() -> operation.accept(testee))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Operation not allowed on committed transaction");
    }

    @ParameterizedTest
    @MethodSource("operations")
    void operationFailsAfterRollback(final Consumer<Transaction> operation) {
        final Transaction testee = startTransaction();
        testee.rollback();
        assertThatThrownBy(() -> operation.accept(testee))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Operation not allowed on rolled back transaction");
    }

    @Test
    void closingRollsBack() throws SQLException {
        final Transaction testee = startTransaction();
        testee.close();
        verify(connectionMock).rollback();
    }

    @Test
    void closingCallsTransactionFinishedCallback() throws SQLException {
        final Transaction testee = startTransaction();
        testee.close();
        verify(transactionFinishedCallbackMock).accept(same(testee));
    }

    @Test
    void closingDoesNotCloseConnection() throws SQLException {
        final Transaction testee = startTransaction();
        testee.close();
        verify(connectionMock, never()).close();
        verify(connectionMock, never()).close();
    }

    @Test
    void closingAlreadyClosedTransactionSucceeds() {
        final Transaction testee = startTransaction();
        testee.close();
        assertDoesNotThrow(testee::close);
    }

    @Test
    void closingAlreadyClosedTransactionRollsBackOnlyOnce() throws SQLException {
        final Transaction testee = startTransaction();
        testee.close();
        testee.close();
        verify(connectionMock, times(1)).rollback();
    }

    @Test
    void closingRolledBackTransactionDoesNotRollback() throws SQLException {
        final Transaction testee = startTransaction();
        testee.rollback();
        testee.close();
        verify(connectionMock, times(1)).rollback();
    }

    @Test
    void closingCommittedTransactionDoesNotRollback() throws SQLException {
        final Transaction testee = startTransaction();
        testee.commit();
        testee.close();
        verify(connectionMock, never()).rollback();
    }

    @Test
    void commit() throws SQLException {
        startTransaction().commit();
        verify(connectionMock).commit();
    }

    @Test
    void commitCallsTransactionFinishedCallback() throws SQLException {
        final Transaction testee = startTransaction();
        testee.commit();
        verify(transactionFinishedCallbackMock).accept(same(testee));
    }

    @Test
    void rollback() throws SQLException {
        startTransaction().rollback();
        verify(connectionMock).rollback();
    }

    @Test
    void rollbackCallsTransactionFinishedCallback() throws SQLException {
        final Transaction testee = startTransaction();
        testee.rollback();
        verify(transactionFinishedCallbackMock).accept(same(testee));
    }

    private Transaction startTransaction() {
        return Transaction.start(connectionMock, transactionFinishedCallbackMock);
    }
}
