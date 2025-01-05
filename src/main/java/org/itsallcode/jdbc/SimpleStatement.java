package org.itsallcode.jdbc;

import java.sql.*;
import java.util.Objects;

import org.itsallcode.jdbc.dialect.DbDialect;
import org.itsallcode.jdbc.resultset.*;

/**
 * Simple wrapper for a JDBC {@link Statement}.
 */
public class SimpleStatement implements AutoCloseable {
    private final Context context;
    private final DbDialect dialect;
    private final Statement statement;

    SimpleStatement(final Context context, final DbDialect dialect, final Statement statement) {
        this.context = Objects.requireNonNull(context, "context");
        this.dialect = Objects.requireNonNull(dialect, "dialect");
        this.statement = Objects.requireNonNull(statement, "statement");
    }

    <T> SimpleResultSet<T> executeQuery(final String sql, final ContextRowMapper<T> rowMapper) {
        final ResultSet resultSet = doExecuteQuery(sql);
        final ResultSet convertingResultSet = ConvertingResultSet.create(dialect, resultSet);
        return new SimpleResultSet<>(context, convertingResultSet, rowMapper);
    }

    private ResultSet doExecuteQuery(final String sql) {
        try {
            return statement.executeQuery(sql);
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error executing query '" + sql + "'", e);
        }
    }

    boolean execute(final String sql) {
        try {
            return statement.execute(sql);
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error executing statement '" + sql + "'", e);
        }
    }

    int getUpdateCount() {
        try {
            return statement.getUpdateCount();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error getting update count", e);
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
     * Execute the batch statement.
     * 
     * @return array of update counts
     * @see Statement#executeBatch()
     */
    public int[] executeBatch() {
        try {
            return statement.executeBatch();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error executing batch", e);
        }
    }

    /**
     * Add the SQL statement to the batch.
     * 
     * @param sql SQL statement
     * @see Statement#addBatch(String)
     */
    public void addBatch(final String sql) {
        try {
            this.statement.addBatch(sql);
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error adding batch", e);
        }
    }

    /**
     * Get the underlying {@link Statement}.
     * 
     * @return the underlying {@link Statement}
     */
    public Statement getStatement() {
        return statement;
    }
}
