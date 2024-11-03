package org.itsallcode.jdbc;

import java.sql.*;

import org.itsallcode.jdbc.dialect.DbDialect;
import org.itsallcode.jdbc.resultset.*;

class SimplePreparedStatement implements AutoCloseable {
    private final Context context;
    private final DbDialect dialect;
    private final PreparedStatement statement;
    private final String sql;

    SimplePreparedStatement(final Context context, final DbDialect dialect, final PreparedStatement statement,
            final String sql) {
        this.context = context;
        this.dialect = dialect;
        this.statement = statement;
        this.sql = sql;
    }

    <T> SimpleResultSet<T> executeQuery(final ContextRowMapper<T> rowMapper) {
        final ResultSet resultSet = doExecuteQuery();
        final ResultSet convertingResultSet = ConvertingResultSet.create(dialect, resultSet);
        return new SimpleResultSet<>(context, convertingResultSet, rowMapper);
    }

    private ResultSet doExecuteQuery() {
        try {
            return statement.executeQuery();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error executing query '" + sql + "'", e);
        }
    }

    void execute() {
        try {
            statement.execute();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error executing statement '" + sql + "'", e);
        }
    }

    @Override
    public void close() {
        try {
            statement.close();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error closing statement", e);
        }
    }

    void setValues(final PreparedStatementSetter preparedStatementSetter) {
        try {
            preparedStatementSetter.setValues(statement);
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error setting values for prepared statement", e);
        }

    }

    int[] executeBatch() {
        try {
            return statement.executeBatch();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error executing batch sql '" + sql + "'", e);
        }
    }

    void addBatch() {
        try {
            this.statement.addBatch();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error adding batch", e);
        }
    }

    SimpleParameterMetaData getParameterMetadata() {
        try {
            return SimpleParameterMetaData.create(statement.getParameterMetaData());
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error getting parameter metadata", e);
        }
    }
}
