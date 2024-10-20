package org.itsallcode.jdbc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BatchInsertTest {

    @Mock
    SimplePreparedStatement stmtMock;
    @Mock
    RowPreparedStatementSetter<Row> stmtSetterMock;

    @Test
    void addDoesNotFlush() {
        final BatchInsert<Row> testee = testee(2);
        testee.add(new Row());

        final InOrder inOrder = inOrder(stmtMock);
        inOrder.verify(stmtMock).setValues(any());
        inOrder.verify(stmtMock).addBatch();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void addDoesNotFlushesAfterBatchSizeReached() {
        final BatchInsert<Row> testee = testee(2);
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

    BatchInsert<Row> testee(final int maxBatchSize) {
        return new BatchInsert<>(stmtMock, stmtSetterMock, maxBatchSize);
    }

    record Row() {

    }
}
