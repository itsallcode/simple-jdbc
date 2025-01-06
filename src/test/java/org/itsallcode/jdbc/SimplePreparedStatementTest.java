package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

import java.sql.*;

import org.itsallcode.jdbc.dialect.GenericDialect;
import org.itsallcode.jdbc.resultset.ContextRowMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimplePreparedStatementTest {

    private static final String SQL_QUERY = "query";
    @Mock
    PreparedStatement statementMock;
    @Mock
    ContextRowMapper<RowType> rowMapperMock;
    @Mock
    ResultSet resultSetMock;
    @Mock
    ResultSetMetaData metaDataMock;
    @Mock
    PreparedStatementSetter preparedStatementSetterMock;
    @Mock
    ParameterMetaData parameterMetadataMock;

    @Test
    void executeQuery() throws SQLException {
        when(statementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.getMetaData()).thenReturn(metaDataMock);
        assertThat(testee().executeQuery(rowMapperMock)).isNotNull();
        verify(statementMock).executeQuery();
    }

    @Test
    void closingResultSetClosesStatement() throws SQLException {
        when(statementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.getMetaData()).thenReturn(metaDataMock);
        testee().executeQuery(rowMapperMock).close();

        verify(statementMock).close();
    }

    @Test
    void executeQueryFails() throws SQLException {
        when(statementMock.executeQuery()).thenThrow(new SQLException("expected"));
        final SimplePreparedStatement testee = testee();
        assertThatThrownBy(() -> testee.executeQuery(rowMapperMock)).isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error executing query 'query': expected");
    }

    @Test
    void executeStatement() throws SQLException {
        when(statementMock.executeUpdate()).thenReturn(2);
        assertThat(testee().executeUpdate()).isEqualTo(2);
        verify(statementMock).executeUpdate();
    }

    @Test
    void executeStatementFails() throws SQLException {
        when(statementMock.executeUpdate()).thenThrow(new SQLException("expected"));
        final SimplePreparedStatement testee = testee();
        assertThatThrownBy(testee::executeUpdate).isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error executing statement 'query': expected");
    }

    @Test
    void close() throws SQLException {
        testee().close();
        verify(statementMock).close();
    }

    @Test
    void closeFails() throws SQLException {
        doThrow(new SQLException("expected")).when(statementMock).close();
        assertThatThrownBy(testee()::close).isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error closing statement: expected");
    }

    @Test
    void setValues() throws SQLException {
        testee().setValues(preparedStatementSetterMock);
        verify(preparedStatementSetterMock).setValues(same(statementMock));
    }

    @Test
    void setValuesFails() throws SQLException {
        doThrow(new SQLException("expected")).when(preparedStatementSetterMock)
                .setValues(same(statementMock));
        final SimplePreparedStatement testee = testee();
        assertThatThrownBy(() -> testee.setValues(preparedStatementSetterMock))
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error setting values for prepared statement: expected");
    }

    @Test
    void executeBatch() throws SQLException {
        testee().executeBatch();
        verify(statementMock).executeBatch();
    }

    @Test
    void executeBatchFails() throws SQLException {
        doThrow(new SQLException("expected")).when(statementMock).executeBatch();
        assertThatThrownBy(testee()::executeBatch).isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error executing batch sql 'query': expected");
    }

    @Test
    void addBatch() throws SQLException {
        testee().addBatch();
        verify(statementMock).addBatch();
    }

    @Test
    void addBatchFails() throws SQLException {
        doThrow(new SQLException("expected")).when(statementMock).addBatch();
        assertThatThrownBy(testee()::addBatch).isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error adding batch: expected");
    }

    @Test
    void getParameterMetadata() throws SQLException {
        when(statementMock.getParameterMetaData()).thenReturn(parameterMetadataMock);
        assertThat(testee().getParameterMetadata()).isNotNull();
    }

    @Test
    void getParameterMetadataFails() throws SQLException {
        doThrow(new SQLException("expected")).when(statementMock).getParameterMetaData();
        assertThatThrownBy(testee()::getParameterMetadata).isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error getting parameter metadata: expected");
    }

    @Test
    void getStatement() {
        assertThat(testee().getStatement()).isSameAs(statementMock);
    }

    SimplePreparedStatement testee() {
        return new SimplePreparedStatement(Context.builder().build(), GenericDialect.INSTANCE, statementMock,
                SQL_QUERY);
    }

    record RowType() {

    }
}
