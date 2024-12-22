package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

import java.util.function.Consumer;
import java.util.stream.Stream;

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

    @Test
    void startDisablesAutoCommitWhenEnabledBefore() {
        when(connectionMock.getAutoCommit()).thenReturn(true);
        Transaction.start(connectionMock);
        verify(connectionMock).setAutoCommit(false);
    }

    @Test
    void startDoesNotDisableAutoCommitWhenAlreadyDisabled() {
        when(connectionMock.getAutoCommit()).thenReturn(false);
        Transaction.start(connectionMock);
        verify(connectionMock, never()).setAutoCommit(anyBoolean());
    }

    @Test
    void closeDoesNotRestoreAutoCommitWhenAlreadyDisabled() {
        when(connectionMock.getAutoCommit()).thenReturn(false);
        Transaction.start(connectionMock).close();
        verify(connectionMock, never()).setAutoCommit(anyBoolean());
    }

    @Test
    void closeRestoresAutoCommit() {
        when(connectionMock.getAutoCommit()).thenReturn(true);
        final InOrder inOrder = inOrder(connectionMock);
        Transaction.start(connectionMock).close();
        inOrder.verify(connectionMock).setAutoCommit(false);
        inOrder.verify(connectionMock).setAutoCommit(true);
        inOrder.verifyNoMoreInteractions();
    }

    static Stream<Arguments> operations() {
        return Stream.of(
                operation(tx -> tx.executeScript("script")),
                operation(tx -> tx.executeStatement("sql")),
                operation(tx -> tx.executeStatement("sql", null)),
                operation(tx -> tx.query("sql")),
                operation(tx -> tx.query("sql", null)),
                operation(tx -> tx.query("sql", null, null)),
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
    void commit() {
        testee().commit();
        verify(connectionMock).commit();
    }

    @Test
    void rollback() {
        testee().rollback();
        verify(connectionMock).rollback();
    }

    @Test
    void closingRollsBack() {
        final Transaction testee = testee();
        testee.close();
        verify(connectionMock).rollback();
    }

    @Test
    void closingDoesNotCloseConnection() {
        final Transaction testee = testee();
        testee.close();
        verify(connectionMock, never()).close();
    }

    @Test
    void closingAlreadyClosedTransactionSucceeds() {
        final Transaction testee = testee();
        testee.close();
        assertDoesNotThrow(testee::close);
    }

    @Test
    void closingAlreadyClosedTransactionRollsBackOnlyOnce() {
        final Transaction testee = testee();
        testee.close();
        testee.close();
        verify(connectionMock, times(1)).rollback();
    }

    @Test
    void closingRolledBackTransactionDoesNotRollback() {
        final Transaction testee = testee();
        testee.rollback();
        testee.close();
        verify(connectionMock, times(1)).rollback();
    }

    @Test
    void closingCommittedTransactionDoesNotRollback() {
        final Transaction testee = testee();
        testee.commit();
        testee.close();
        verify(connectionMock, never()).rollback();
    }

    private Transaction testee() {
        return Transaction.start(connectionMock);
    }
}
