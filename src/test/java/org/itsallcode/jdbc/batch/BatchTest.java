package org.itsallcode.jdbc.batch;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BatchTest {

    @Mock
    AutoCloseable resourceMock;
    @Mock
    Runnable batchExecutorMock;

    @Test
    void addBatchDoesNotFlush() {
        final Batch testee = testee(2);
        testee.addBatch();

        final InOrder inOrder = inOrder(batchExecutorMock, resourceMock);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void addBatchFlushesAfterBatchSizeReached() {
        final Batch testee = testee(2);

        testee.addBatch();
        testee.addBatch();

        final InOrder inOrder = inOrder(batchExecutorMock, resourceMock);
        inOrder.verify(batchExecutorMock).run();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void closeClosesStatement() throws Exception {
        final Batch testee = testee(2);
        testee.close();
        verify(resourceMock).close();
    }

    @Test
    void closeFlushes() throws Exception {
        final Batch testee = testee(2);

        testee.addBatch();
        testee.close();

        final InOrder inOrder = inOrder(batchExecutorMock, resourceMock);
        inOrder.verify(batchExecutorMock).run();
        inOrder.verify(resourceMock).close();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void closeResourceFails() throws Exception {
        final Batch testee = testee(2);
        doThrow(new RuntimeException("expected")).when(resourceMock).close();
        assertThatThrownBy(testee::close).isInstanceOf(IllegalStateException.class)
                .hasMessage("Failed to close resource: expected");
    }

    Batch testee(final int maxBatchSize) {
        return new Batch(maxBatchSize, resourceMock, batchExecutorMock);
    }
}
