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
    private Statement statementMock;

    @Test
    void unwrap() throws SQLException {
        when(getStatementMock().unwrap(String.class)).thenReturn("value");
        assertEquals("value", testee().unwrap(String.class));
    }

    @Test
    void isWrapperFor() throws SQLException {
        when(getStatementMock().isWrapperFor(String.class)).thenReturn(true);
        assertTrue(testee().isWrapperFor(String.class));
    }

    @Test
    void executeQuery() throws SQLException {
        testee().executeQuery("sql");
        verify(getStatementMock()).executeQuery("sql");
    }

    @Test
    void executeUpdate() throws SQLException {
        when(getStatementMock().executeUpdate("sql")).thenReturn(1);
        assertEquals(1, testee().executeUpdate("sql"));
    }

    @Test
    void close() throws SQLException {
        testee().close();
        verify(getStatementMock()).close();
    }

    @Test
    void getMaxFieldSize() throws SQLException {
        when(getStatementMock().getMaxFieldSize()).thenReturn(1);
        assertEquals(1, testee().getMaxFieldSize());
    }

    @Test
    void setMaxFieldSize() throws SQLException {
        testee().setMaxFieldSize(1);
        verify(getStatementMock()).setMaxFieldSize(1);
    }

    @Test
    void getMaxRows() throws SQLException {
        when(getStatementMock().getMaxRows()).thenReturn(1);
        assertEquals(1, testee().getMaxRows());
    }

    @Test
    void setMaxRows() throws SQLException {
        testee().setMaxRows(1);
        verify(getStatementMock()).setMaxRows(1);
    }

    @Test
    void setEscapeProcessing() throws SQLException {
        testee().setEscapeProcessing(true);
        verify(getStatementMock()).setEscapeProcessing(true);
    }

    @Test
    void getQueryTimeout() throws SQLException {
        when(getStatementMock().getQueryTimeout()).thenReturn(1);
        assertEquals(1, testee().getQueryTimeout());
    }

    @Test
    void setQueryTimeout() throws SQLException {
        testee().setQueryTimeout(1);
        verify(getStatementMock()).setQueryTimeout(1);
    }

    @Test
    void cancel() throws SQLException {
        testee().cancel();
        verify(getStatementMock()).cancel();
    }

    @Test
    void getWarnings(@Mock final SQLWarning warning) throws SQLException {
        when(getStatementMock().getWarnings()).thenReturn(warning);
        assertSame(warning, testee().getWarnings());
    }

    @Test
    void clearWarnings() throws SQLException {
        testee().clearWarnings();
        verify(getStatementMock()).clearWarnings();
    }

    @Test
    void setCursorName() throws SQLException {
        testee().setCursorName("name");
        verify(getStatementMock()).setCursorName("name");
    }

    @Test
    void execute() throws SQLException {
        when(getStatementMock().execute("sql")).thenReturn(true);
        assertTrue(testee().execute("sql"));
    }

    @Test
    void getResultSet(@Mock final ResultSet resultSet) throws SQLException {
        when(getStatementMock().getResultSet()).thenReturn(resultSet);
        assertSame(resultSet, testee().getResultSet());
    }

    @Test
    void getUpdateCount() throws SQLException {
        when(getStatementMock().getUpdateCount()).thenReturn(1);
        assertEquals(1, testee().getUpdateCount());
    }

    @Test
    void getMoreResults() throws SQLException {
        when(getStatementMock().getMoreResults()).thenReturn(true);
        assertTrue(testee().getMoreResults());
    }

    @Test
    void setFetchDirection() throws SQLException {
        testee().setFetchDirection(1);
        verify(getStatementMock()).setFetchDirection(1);
    }

    @Test
    void getFetchDirection() throws SQLException {
        when(getStatementMock().getFetchDirection()).thenReturn(1);
        assertEquals(1, testee().getFetchDirection());
    }

    @Test
    void setFetchSize() throws SQLException {
        testee().setFetchSize(1);
        verify(getStatementMock()).setFetchSize(1);
    }

    @Test
    void getFetchSize() throws SQLException {
        when(getStatementMock().getFetchSize()).thenReturn(1);
        assertEquals(1, testee().getFetchSize());
    }

    @Test
    void getResultSetConcurrency() throws SQLException {
        when(getStatementMock().getResultSetConcurrency()).thenReturn(1);
        assertEquals(1, testee().getResultSetConcurrency());
    }

    @Test
    void getResultSetType() throws SQLException {
        when(getStatementMock().getResultSetType()).thenReturn(1);
        assertEquals(1, testee().getResultSetType());
    }

    @Test
    void addBatch() throws SQLException {
        testee().addBatch("sql");
        verify(getStatementMock()).addBatch("sql");
    }

    @Test
    void clearBatch() throws SQLException {
        testee().clearBatch();
        verify(getStatementMock()).clearBatch();
    }

    @Test
    void executeBatch() throws SQLException {
        final var result = new int[] { 1 };
        when(getStatementMock().executeBatch()).thenReturn(result);
        assertSame(result, testee().executeBatch());
    }

    @Test
    void getConnection(@Mock final Connection connection) throws SQLException {
        when(getStatementMock().getConnection()).thenReturn(connection);
        assertSame(connection, testee().getConnection());
    }

    @Test
    void getMoreResultsInt() throws SQLException {
        when(getStatementMock().getMoreResults(1)).thenReturn(true);
        assertTrue(testee().getMoreResults(1));
    }

    @Test
    void getGeneratedKeys(@Mock final ResultSet resultSet) throws SQLException {
        when(getStatementMock().getGeneratedKeys()).thenReturn(resultSet);
        assertSame(resultSet, testee().getGeneratedKeys());
    }

    @Test
    void executeUpdateAutoGeneratedKeys() throws SQLException {
        when(getStatementMock().executeUpdate("sql", 2)).thenReturn(1);
        assertEquals(1, testee().executeUpdate("sql", 2));
    }

    @Test
    void executeUpdateColumnIndices() throws SQLException {
        when(getStatementMock().executeUpdate("sql", new int[] { 1 })).thenReturn(1);
        assertEquals(1, testee().executeUpdate("sql", new int[] { 1 }));
    }

    @Test
    void executeUpdateColumnNames() throws SQLException {
        when(getStatementMock().executeUpdate("sql", new String[] { "col" })).thenReturn(1);
        assertEquals(1, testee().executeUpdate("sql", new String[] { "col" }));
    }

    @Test
    void executeAutoGeneratedKeys() throws SQLException {
        when(getStatementMock().execute("sql", 2)).thenReturn(true);
        assertTrue(testee().execute("sql", 2));
    }

    @Test
    void executeColumnIndexes() throws SQLException {
        when(getStatementMock().execute("sql", new int[] { 1 })).thenReturn(true);
        assertTrue(testee().execute("sql", new int[] { 1 }));
    }

    @Test
    void executeColumnNames() throws SQLException {
        when(getStatementMock().execute("sql", new String[] { "col" })).thenReturn(true);
        assertTrue(testee().execute("sql", new String[] { "col" }));
    }

    @Test
    void getResultSetHoldability() throws SQLException {
        when(getStatementMock().getResultSetHoldability()).thenReturn(1);
        assertEquals(1, testee().getResultSetHoldability());
    }

    @Test
    void isClosed() throws SQLException {
        when(getStatementMock().isClosed()).thenReturn(true);
        assertTrue(testee().isClosed());
    }

    @Test
    void setPoolable() throws SQLException {
        testee().setPoolable(true);
        verify(getStatementMock()).setPoolable(true);
    }

    @Test
    void isPoolable() throws SQLException {
        when(getStatementMock().isPoolable()).thenReturn(true);
        assertTrue(testee().isPoolable());
    }

    @Test
    void closeOnCompletion() throws SQLException {
        testee().closeOnCompletion();
        verify(getStatementMock()).closeOnCompletion();
    }

    @Test
    void isCloseOnCompletion() throws SQLException {
        when(getStatementMock().isCloseOnCompletion()).thenReturn(true);
        assertTrue(testee().isCloseOnCompletion());
    }

    protected Statement getStatementMock() {
        return statementMock;
    }

    protected DelegatingStatement testee() {
        return new DelegatingStatement(getStatementMock());
    }
}
