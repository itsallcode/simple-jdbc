package org.itsallcode.jdbc.batch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

import java.sql.PreparedStatement;

import org.itsallcode.jdbc.PreparedStatementSetter;
import org.itsallcode.jdbc.SimplePreparedStatement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PreparedStatementBatchTest {

    @Mock
    SimplePreparedStatement stmtMock;
    @Mock
    PreparedStatementSetter stmtSetterMock;

    @Test
    void addDoesNotFlush() {
        final PreparedStatementBatch testee = testee(2);
        testee.add(stmtSetterMock);

        final InOrder inOrder = inOrder(stmtMock);
        inOrder.verify(stmtMock).setValues(same(stmtSetterMock));
        inOrder.verify(stmtMock).addBatch();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void addFlushesAfterBatchSizeReached() {
        final PreparedStatementBatch testee = testee(2);
        when(stmtMock.executeBatch()).thenReturn(new int[0]);

        testee.add(stmtSetterMock);
        testee.add(stmtSetterMock);

        final InOrder inOrder = inOrder(stmtMock);
        inOrder.verify(stmtMock).setValues(same(stmtSetterMock));
        inOrder.verify(stmtMock).addBatch();
        inOrder.verify(stmtMock).setValues(same(stmtSetterMock));
        inOrder.verify(stmtMock).addBatch();
        inOrder.verify(stmtMock).executeBatch();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void addBatchDoesNotFlush() {
        final PreparedStatementBatch testee = testee(2);
        testee.addBatch();

        final InOrder inOrder = inOrder(stmtMock);
        inOrder.verify(stmtMock).addBatch();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void addBatchFlushesAfterBatchSizeReached() {
        final PreparedStatementBatch testee = testee(2);
        when(stmtMock.executeBatch()).thenReturn(new int[0]);

        testee.addBatch();
        testee.addBatch();

        final InOrder inOrder = inOrder(stmtMock);
        inOrder.verify(stmtMock, times(2)).addBatch();
        inOrder.verify(stmtMock).executeBatch();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void closeClosesStatement() {
        final PreparedStatementBatch testee = testee(2);
        testee.close();
        verify(stmtMock).close();
    }

    @Test
    void closeFlushes() {
        final PreparedStatementBatch testee = testee(2);
        when(stmtMock.executeBatch()).thenReturn(new int[0]);

        testee.add(stmtSetterMock);
        testee.close();

        final InOrder inOrder = inOrder(stmtMock);
        inOrder.verify(stmtMock).addBatch();
        inOrder.verify(stmtMock).executeBatch();
        inOrder.verify(stmtMock).close();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void getStatement() {
        final PreparedStatementBatch testee = testee(2);
        final PreparedStatement preparedStmtMock = mock(PreparedStatement.class);
        when(stmtMock.getStatement()).thenReturn(preparedStmtMock);
        assertThat(testee.getStatement()).isSameAs(preparedStmtMock);
    }

    PreparedStatementBatch testee(final int maxBatchSize) {
        return new PreparedStatementBatch(stmtMock, maxBatchSize);
    }
}
