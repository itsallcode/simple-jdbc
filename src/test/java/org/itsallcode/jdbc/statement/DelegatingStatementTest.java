package org.itsallcode.jdbc.statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DelegatingStatementTest {
    @Mock
    Statement statementMock;

    @Test
    void unwrap() throws SQLException {
        when(statementMock.unwrap(String.class)).thenReturn("value");
        assertEquals("value", testee().unwrap(String.class));
    }

    @Test
    void isWrapperFor() throws SQLException {
        when(statementMock.isWrapperFor(String.class)).thenReturn(true);
        assertTrue(testee().isWrapperFor(String.class));
    }

    @Test
    void executeQuery() throws SQLException {
        testee().executeQuery("sql");
        verify(statementMock).executeQuery("sql");
    }

    @Test
    void executeUpdate() throws SQLException {
        when(statementMock.executeUpdate("sql")).thenReturn(1);
        assertEquals(1, testee().executeUpdate("sql"));
    }

    @Test
    void close() throws SQLException {
        testee().close();
        verify(statementMock).close();
    }

    @Test
    void getMaxFieldSize() throws SQLException {
        when(statementMock.getMaxFieldSize()).thenReturn(1);
        assertEquals(1, testee().getMaxFieldSize());
    }

    @Test
    void setMaxFieldSize() throws SQLException {
        testee().setMaxFieldSize(1);
        verify(statementMock).setMaxFieldSize(1);
    }

    @Test
    void getMaxRows() throws SQLException {
        when(statementMock.getMaxRows()).thenReturn(1);
        assertEquals(1, testee().getMaxRows());
    }

    @Test
    void setMaxRows() throws SQLException {
        testee().setMaxRows(1);
        verify(statementMock).setMaxRows(1);
    }

    @Test
    void setEscapeProcessing() throws SQLException {
        testee().setEscapeProcessing(true);
        verify(statementMock).setEscapeProcessing(true);
    }

    @Test
    void getQueryTimeout() throws SQLException {
        when(statementMock.getQueryTimeout()).thenReturn(1);
        assertEquals(1, testee().getQueryTimeout());
    }

    @Test
    void setQueryTimeout() throws SQLException {
        testee().setQueryTimeout(1);
        verify(statementMock).setQueryTimeout(1);
    }

    @Test
    void cancel() throws SQLException {
        testee().cancel();
        verify(statementMock).cancel();
    }

    @Test
    void getWarnings(@Mock final SQLWarning warning) throws SQLException {
        when(statementMock.getWarnings()).thenReturn(warning);
        assertSame(warning, testee().getWarnings());
    }

    @Test
    void clearWarnings() throws SQLException {
        testee().clearWarnings();
        verify(statementMock).clearWarnings();
    }

    @Test
    void setCursorName() throws SQLException {
        testee().setCursorName("name");
        verify(statementMock).setCursorName("name");
    }

    @Test
    void execute() throws SQLException {
        when(statementMock.execute("sql")).thenReturn(true);
        assertTrue(testee().execute("sql"));
    }

    @Test
    void getResultSet(@Mock final ResultSet resultSet) throws SQLException {
        when(statementMock.getResultSet()).thenReturn(resultSet);
        assertSame(resultSet, testee().getResultSet());
    }

    @Test
    void getUpdateCount() throws SQLException {
        when(statementMock.getUpdateCount()).thenReturn(1);
        assertEquals(1, testee().getUpdateCount());
    }

    @Test
    void getMoreResults() throws SQLException {
        when(statementMock.getMoreResults()).thenReturn(true);
        assertTrue(testee().getMoreResults());
    }

    @Test
    void setFetchDirection() throws SQLException {
        testee().setFetchDirection(1);
        verify(statementMock).setFetchDirection(1);
    }

    @Test
    void getFetchDirection() throws SQLException {
        when(statementMock.getFetchDirection()).thenReturn(1);
        assertEquals(1, testee().getFetchDirection());
    }

    @Test
    void setFetchSize() throws SQLException {
        testee().setFetchSize(1);
        verify(statementMock).setFetchSize(1);
    }

    @Test
    void getFetchSize() throws SQLException {
        when(statementMock.getFetchSize()).thenReturn(1);
        assertEquals(1, testee().getFetchSize());
    }

    @Test
    void getResultSetConcurrency() throws SQLException {
        when(statementMock.getResultSetConcurrency()).thenReturn(1);
        assertEquals(1, testee().getResultSetConcurrency());
    }

    @Test
    void getResultSetType() throws SQLException {
        when(statementMock.getResultSetType()).thenReturn(1);
        assertEquals(1, testee().getResultSetType());
    }

    @Test
    void addBatch() throws SQLException {
        testee().addBatch("sql");
        verify(statementMock).addBatch("sql");
    }

    @Test
    void clearBatch() throws SQLException {
        testee().clearBatch();
        verify(statementMock).clearBatch();
    }

    @Test
    void executeBatch() throws SQLException {
        final var result = new int[] { 1 };
        when(statementMock.executeBatch()).thenReturn(result);
        assertSame(result, testee().executeBatch());
    }

    @Test
    void getConnection(@Mock final Connection connection) throws SQLException {
        when(statementMock.getConnection()).thenReturn(connection);
        assertSame(connection, testee().getConnection());
    }

    @Test
    void getMoreResultsInt() throws SQLException {
        when(statementMock.getMoreResults(1)).thenReturn(true);
        assertTrue(testee().getMoreResults(1));
    }

    @Test
    void getGeneratedKeys(@Mock final ResultSet resultSet) throws SQLException {
        when(statementMock.getGeneratedKeys()).thenReturn(resultSet);
        assertSame(resultSet, testee().getGeneratedKeys());
    }

    @Test
    void executeUpdateAutoGeneratedKeys() throws SQLException {
        when(statementMock.executeUpdate("sql", 2)).thenReturn(1);
        assertEquals(1, testee().executeUpdate("sql", 2));
    }

    @Test
    void executeUpdateColumnIndices() throws SQLException {
        when(statementMock.executeUpdate("sql", new int[] { 1 })).thenReturn(1);
        assertEquals(1, testee().executeUpdate("sql", new int[] { 1 }));
    }

    @Test
    void executeUpdateColumnNames() throws SQLException {
        when(statementMock.executeUpdate("sql", new String[] { "col" })).thenReturn(1);
        assertEquals(1, testee().executeUpdate("sql", new String[] { "col" }));
    }

    @Test
    void executeAutoGeneratedKeys() throws SQLException {
        when(statementMock.execute("sql", 2)).thenReturn(true);
        assertTrue(testee().execute("sql", 2));
    }

    @Test
    void executeColumnIndexes() throws SQLException {
        when(statementMock.execute("sql", new int[] { 1 })).thenReturn(true);
        assertTrue(testee().execute("sql", new int[] { 1 }));
    }

    @Test
    void executeColumnNames() throws SQLException {
        when(statementMock.execute("sql", new String[] { "col" })).thenReturn(true);
        assertTrue(testee().execute("sql", new String[] { "col" }));
    }

    @Test
    void getResultSetHoldability() throws SQLException {
        when(statementMock.getResultSetHoldability()).thenReturn(1);
        assertEquals(1, testee().getResultSetHoldability());
    }

    @Test
    void isClosed() throws SQLException {
        when(statementMock.isClosed()).thenReturn(true);
        assertTrue(testee().isClosed());
    }

    @Test
    void setPoolable() throws SQLException {
        testee().setPoolable(true);
        verify(statementMock).setPoolable(true);
    }

    @Test
    void isPoolable() throws SQLException {
        when(statementMock.isPoolable()).thenReturn(true);
        assertTrue(testee().isPoolable());
    }

    @Test
    void closeOnCompletion() throws SQLException {
        testee().closeOnCompletion();
        verify(statementMock).closeOnCompletion();
    }

    @Test
    void isCloseOnCompletion() throws SQLException {
        when(statementMock.isCloseOnCompletion()).thenReturn(true);
        assertTrue(testee().isCloseOnCompletion());
    }

    DelegatingStatement testee() {
        return new DelegatingStatement(statementMock);
    }
}
