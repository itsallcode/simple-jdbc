package org.itsallcode.jdbc.resultset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.itsallcode.jdbc.UncheckedSQLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("resource")
class SimpleResultSetTest {

    @Mock
    private ResultSet resultSetMock;
    @Mock
    private ContextRowMapper<TestingRowType> rowMapper;

    SimpleResultSet<TestingRowType> testee() {
        return new SimpleResultSet<>(null, resultSetMock, rowMapper);
    }

    @Test
    void mapRow() throws SQLException {
        simulateSingleResult();
        simulateRowMapper();
        assertThat(testee().toList()).containsExactly(new TestingRowType(0));
    }

    @Test
    void mapRowFailsWithRuntimeException() throws SQLException {
        simulateSingleResult();
        when(rowMapper.mapRow(eq(null), same(resultSetMock), anyInt())).thenThrow(new RuntimeException("expected"));
        assertThatThrownBy(testee()::toList).isInstanceOf(IllegalStateException.class)
                .hasMessage("Error mapping row 0: expected");
    }

    @Test
    void mapRowFailsWithSQLException() throws SQLException {
        simulateSingleResult();
        when(rowMapper.mapRow(eq(null), same(resultSetMock), anyInt())).thenThrow(new SQLException("expected"));
        assertThatThrownBy(testee()::toList).isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error mapping row 0: expected");
    }

    @Test
    void nextFails() throws SQLException {
        when(resultSetMock.next()).thenThrow(new SQLException("expected"));
        assertThatThrownBy(testee()::toList).isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error getting next row: expected");
    }

    @Test
    void closeFails() throws SQLException {
        doThrow(new SQLException("expected")).when(resultSetMock).close();
        assertThatThrownBy(testee()::close).isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error closing resultset: expected");
    }

    @Test
    void closeSucceeds() throws SQLException {
        testee().close();
        verify(resultSetMock).close();
    }

    private void simulateRowMapper() throws SQLException {
        when(rowMapper.mapRow(eq(null), same(resultSetMock), anyInt()))
                .thenAnswer(invocation -> new TestingRowType(invocation.getArgument(2, Integer.class)));
    }

    private void simulateSingleResult() throws SQLException {
        when(resultSetMock.next()).thenReturn(true).thenReturn(false);
    }

    record TestingRowType(int row) {
    }
}
