package org.itsallcode.jdbc.batch;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import org.itsallcode.jdbc.RowPreparedStatementSetter;
import org.itsallcode.jdbc.SimplePreparedStatement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BatchInsertRowTest {

    @Mock
    SimplePreparedStatement stmtMock;
    @Mock
    RowPreparedStatementSetter<Row> stmtSetterMock;

    @Test
    void addDoesNotFlush() {
        final BatchInsertRow<Row> testee = testee(2);
        testee.add(new Row());

        final InOrder inOrder = inOrder(stmtMock);
        inOrder.verify(stmtMock).setValues(any());
        inOrder.verify(stmtMock).addBatch();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void addDoesNotFlushesAfterBatchSizeReached() {
        final BatchInsertRow<Row> testee = testee(2);
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

    BatchInsertRow<Row> testee(final int maxBatchSize) {
        return new BatchInsertRow<>(stmtMock, stmtSetterMock, maxBatchSize);
    }

    record Row() {

    }
}
