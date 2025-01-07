package org.itsallcode.jdbc;

import static java.util.function.Predicate.not;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

import org.itsallcode.jdbc.batch.*;
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

    int executeUpdate(final String sql) {
        try (SimpleStatement statement = createSimpleStatement()) {
            return statement.executeUpdate(sql);
        }
    }

    int executeUpdate(final String sql, final PreparedStatementSetter preparedStatementSetter) {
        try (SimplePreparedStatement statement = prepareStatement(sql)) {
            statement.setValues(preparedStatementSetter);
            return statement.executeUpdate();
        }
    }

    void executeScript(final String sqlScript) {
        final List<String> statements = Arrays.stream(sqlScript.split(";"))
                .map(String::trim)
                .filter(not(String::isEmpty))
                .toList();
        if (statements.isEmpty()) {
            return;
        }
        try (StatementBatch batch = this.batch().build()) {
            statements.forEach(batch::addBatch);
        }
    }

    SimpleResultSet<Row> query(final String sql) {
        LOG.finest(() -> "Executing query '" + sql + "'...");
        final SimpleStatement statement = createSimpleStatement();
        return statement.executeQuery(sql, ContextRowMapper.create(ContextRowMapper.generic(dialect)));
    }

    <T> SimpleResultSet<T> query(final String sql, final PreparedStatementSetter preparedStatementSetter,
            final RowMapper<T> rowMapper) {
        LOG.finest(() -> "Executing query '" + sql + "'...");
        final SimplePreparedStatement statement = prepareStatement(sql);
        statement.setValues(preparedStatementSetter);
        return statement.executeQuery(ContextRowMapper.create(rowMapper));
    }

    private SimplePreparedStatement prepareStatement(final String sql) {
        return new SimplePreparedStatement(context, dialect,
                new ConvertingPreparedStatement(prepare(sql), paramSetterProvider), sql);
    }

    StatementBatchBuilder batch() {
        return new StatementBatchBuilder(this::createSimpleStatement);
    }

    private SimpleStatement createSimpleStatement() {
        return new SimpleStatement(context, dialect, createStatement());
    }

    PreparedStatementBatchBuilder batchInsert() {
        return new PreparedStatementBatchBuilder(this::prepareStatement);
    }

    <T> RowPreparedStatementBatchBuilder<T> rowBatchInsert() {
        return new RowPreparedStatementBatchBuilder<>(this::prepareStatement);
    }

    private PreparedStatement prepare(final String sql) {
        try {
            return connection.prepareStatement(sql);
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error preparing statement '" + sql + "'", e);
        }
    }

    private Statement createStatement() {
        try {
            return connection.createStatement();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error creating statement", e);
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
