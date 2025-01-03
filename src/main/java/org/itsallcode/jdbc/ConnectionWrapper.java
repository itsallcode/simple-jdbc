package org.itsallcode.jdbc;

import static java.util.function.Predicate.not;

import java.sql.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

import org.itsallcode.jdbc.batch.BatchInsertBuilder;
import org.itsallcode.jdbc.batch.RowBatchInsertBuilder;
import org.itsallcode.jdbc.dialect.DbDialect;
import org.itsallcode.jdbc.resultset.*;
import org.itsallcode.jdbc.resultset.generic.Row;
import org.itsallcode.jdbc.statement.ConvertingPreparedStatement;
import org.itsallcode.jdbc.statement.ParamSetterProvider;

/**
 * This class wraps a {@link Connection} and provides simplified access to it,
 * converting checked exception {@link SQLException} to unchecked exception
 * {@link UncheckedSQLException}.
 */
class ConnectionWrapper implements AutoCloseable {
    private static final Logger LOG = Logger.getLogger(ConnectionWrapper.class.getName());
    private final Connection connection;
    private final DbDialect dialect;
    private final Context context;
    private final ParamSetterProvider paramSetterProvider;

    ConnectionWrapper(final Connection connection, final Context context, final DbDialect dialect) {
        this.connection = Objects.requireNonNull(connection, "connection");
        this.context = Objects.requireNonNull(context, "context");
        this.dialect = Objects.requireNonNull(dialect, "dialect");
        this.paramSetterProvider = new ParamSetterProvider(dialect);
    }

    void executeStatement(final String sql) {
        this.executeStatement(sql, ps -> {
        });
    }

    void executeStatement(final String sql, final PreparedStatementSetter preparedStatementSetter) {
        try (SimplePreparedStatement statement = prepareStatement(sql)) {
            statement.setValues(preparedStatementSetter);
            statement.execute();
        }
    }

    void executeScript(final String sqlScript) {
        Arrays.stream(sqlScript.split(";"))
                .map(String::trim)
                .filter(not(String::isEmpty))
                .forEach(this::executeStatement);
    }

    SimpleResultSet<Row> query(final String sql) {
        return this.query(sql, ps -> {
        }, ContextRowMapper.generic(dialect));
    }

    <T> SimpleResultSet<T> query(final String sql, final PreparedStatementSetter preparedStatementSetter,
            final RowMapper<T> rowMapper) {
        LOG.finest(() -> "Executing query '" + sql + "'...");
        final SimplePreparedStatement statement = prepareStatement(sql);
        statement.setValues(preparedStatementSetter);
        return statement.executeQuery(ContextRowMapper.create(rowMapper));
    }

    SimplePreparedStatement prepareStatement(final String sql) {
        return new SimplePreparedStatement(context, dialect, wrap(prepare(sql)), sql);
    }

    private PreparedStatement wrap(final PreparedStatement preparedStatement) {
        return new ConvertingPreparedStatement(preparedStatement, paramSetterProvider);
    }

    BatchInsertBuilder batchInsert() {
        return new BatchInsertBuilder(this::prepareStatement);
    }

    <T> RowBatchInsertBuilder<T> rowBatchInsert() {
        return new RowBatchInsertBuilder<>(this::prepareStatement);
    }

    private PreparedStatement prepare(final String sql) {
        try {
            return connection.prepareStatement(sql);
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error preparing statement '" + sql + "'", e);
        }
    }

    void setAutoCommit(final boolean autoCommit) {
        try {
            connection.setAutoCommit(autoCommit);
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Failed to set autoCommit to " + autoCommit, e);
        }
    }

    boolean isAutoCommitEnabled() {
        try {
            return connection.getAutoCommit();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Failed to get autoCommit", e);
        }
    }

    void rollback() {
        try {
            this.connection.rollback();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Failed to rollback transaction", e);
        }
    }

    void commit() {
        try {
            this.connection.commit();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Failed to commit transaction", e);
        }
    }

    boolean isClosed() {
        try {
            return this.connection.isClosed();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Failed to get closed state", e);
        }
    }

    Connection getOriginalConnection() {
        return connection;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error closing connection", e);
        }
    }
}
