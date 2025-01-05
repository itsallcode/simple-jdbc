package org.itsallcode.jdbc;

import java.sql.*;
import java.util.Objects;

import org.itsallcode.jdbc.dialect.DbDialect;
import org.itsallcode.jdbc.resultset.*;

/**
 * Simple wrapper for a JDBC {@link PreparedStatement}.
 */
public class SimplePreparedStatement implements AutoCloseable {
    private final Context context;
    private final DbDialect dialect;
    private final PreparedStatement statement;
    private final String sql;

    SimplePreparedStatement(final Context context, final DbDialect dialect, final PreparedStatement statement,
            final String sql) {
        this.context = Objects.requireNonNull(context, "context");
        this.dialect = Objects.requireNonNull(dialect, "dialect");
        this.statement = Objects.requireNonNull(statement, "statement");
        this.sql = Objects.requireNonNull(sql, "sql");
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

    boolean execute() {
        try {
            return statement.execute();
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

    /**
     * Set the values for the prepared statement.
     * 
     * @param preparedStatementSetter prepared statement setter
     */
    public void setValues(final PreparedStatementSetter preparedStatementSetter) {
        try {
            preparedStatementSetter.setValues(statement);
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error setting values for prepared statement", e);
        }
    }

    /**
     * Execute the batch statement.
     * 
     * @return array of update counts
     * @see Statement#executeBatch()
     */
    public int[] executeBatch() {
        try {
            return statement.executeBatch();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error executing batch sql '" + sql + "'", e);
        }
    }

    /**
     * Add the current set of parameters to the batch.
     * 
     * @see PreparedStatement#addBatch()
     */
    public void addBatch() {
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

    /**
     * Get the underlying {@link PreparedStatement}.
     * 
     * @return the underlying {@link PreparedStatement}
     */
    public PreparedStatement getStatement() {
        return statement;
    }
}
