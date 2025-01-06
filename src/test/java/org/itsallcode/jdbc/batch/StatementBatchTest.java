package org.itsallcode.jdbc.batch;

import static org.mockito.Mockito.*;

import org.itsallcode.jdbc.SimpleStatement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StatementBatchTest {

    @Mock
    SimpleStatement stmtMock;

    @Test
    void addBatchDoesNotFlush() {
        final StatementBatch testee = testee(2);
        testee.addBatch("stmt1");

        final InOrder inOrder = inOrder(stmtMock);
        inOrder.verify(stmtMock).addBatch("stmt1");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void addBatchFlushesAfterBatchSizeReached() {
        final StatementBatch testee = testee(2);
        when(stmtMock.executeBatch()).thenReturn(new int[0]);

        testee.addBatch("stmt1");
        testee.addBatch("stmt2");

        final InOrder inOrder = inOrder(stmtMock);
        inOrder.verify(stmtMock).addBatch("stmt1");
        inOrder.verify(stmtMock).addBatch("stmt2");
        inOrder.verify(stmtMock).executeBatch();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void closeClosesStatement() {
        final StatementBatch testee = testee(2);
        testee.close();
        verify(stmtMock).close();
    }

    @Test
    void closeFlushes() {
        final StatementBatch testee = testee(2);
        when(stmtMock.executeBatch()).thenReturn(new int[0]);

        testee.addBatch("stmt1");
        testee.close();

        final InOrder inOrder = inOrder(stmtMock);
        inOrder.verify(stmtMock).addBatch("stmt1");
        inOrder.verify(stmtMock).executeBatch();
        inOrder.verify(stmtMock).close();
        inOrder.verifyNoMoreInteractions();
    }

    StatementBatch testee(final int maxBatchSize) {
        return new StatementBatch(stmtMock, maxBatchSize);
    }
}
