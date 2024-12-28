package org.itsallcode.jdbc.batch;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.itsallcode.jdbc.RowPreparedStatementSetter;
import org.itsallcode.jdbc.SimplePreparedStatement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RowBatchInsertTest {

    @Mock
    SimplePreparedStatement stmtMock;
    @Mock
    RowPreparedStatementSetter<Row> stmtSetterMock;

    @Test
    void addDoesNotFlush() {
        final RowBatchInsert<Row> testee = testee(2);
        testee.add(new Row());

        final InOrder inOrder = inOrder(stmtMock);
        inOrder.verify(stmtMock).setValues(any());
        inOrder.verify(stmtMock).addBatch();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void addFlushesAfterBatchSizeReached() {
        final RowBatchInsert<Row> testee = testee(2);
        when(stmtMock.executeBatch()).thenReturn(new int[0]);

        testee.add(new Row());
        testee.add(new Row());

        final InOrder inOrder = inOrder(stmtMock);
        inOrder.verify(stmtMock).setValues(any());
        inOrder.verify(stmtMock).addBatch();
        inOrder.verify(stmtMock).setValues(any());
        inOrder.verify(stmtMock).addBatch();
        inOrder.verify(stmtMock).executeBatch();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void closeClosesStatement() {
        final RowBatchInsert<Row> testee = testee(2);
        testee.close();
        verify(stmtMock).close();
    }

    @Test
    void closeFlushes() {
        final RowBatchInsert<Row> testee = testee(2);
        when(stmtMock.executeBatch()).thenReturn(new int[0]);

        testee.add(new Row());
        testee.close();

        final InOrder inOrder = inOrder(stmtMock);
        inOrder.verify(stmtMock).addBatch();
        inOrder.verify(stmtMock).executeBatch();
        inOrder.verify(stmtMock).close();
        inOrder.verifyNoMoreInteractions();
    }

    RowBatchInsert<Row> testee(final int maxBatchSize) {
        return new RowBatchInsert<>(stmtMock, stmtSetterMock, maxBatchSize);
    }

    record Row() {

    }
}
