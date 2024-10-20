package org.itsallcode.jdbc;

import java.sql.*;
import java.util.Objects;
import java.util.logging.Logger;

import org.itsallcode.jdbc.dialect.DbDialect;
import org.itsallcode.jdbc.resultset.*;
import org.itsallcode.jdbc.resultset.generic.Row;
import org.itsallcode.jdbc.statement.ConvertingPreparedStatement;
import org.itsallcode.jdbc.statement.ParamSetterProvider;

/**
 * A simplified version of a JDBC {@link Connection}. Create new connections
 * with {@link ConnectionFactory#create(String, String, String)}.
 */
public class SimpleConnection implements DbOperations {
    private static final Logger LOG = Logger.getLogger(SimpleConnection.class.getName());

    private final Connection connection;
    private final Context context;
    private final DbDialect dialect;
    private final ParamSetterProvider paramSetterProvider;

    SimpleConnection(final Connection connection, final Context context, final DbDialect dialect) {
        this.connection = Objects.requireNonNull(connection, "connection");
        this.context = Objects.requireNonNull(context, "context");
        this.dialect = Objects.requireNonNull(dialect, "dialect");
        this.paramSetterProvider = new ParamSetterProvider(dialect);
    }

    public Transaction startTransaction() {
        return Transaction.start(this);
    }

    @Override
    public void executeStatement(final String sql) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error executing '" + sql + "'", e);
        }
    }

    @Override
    public SimpleResultSet<Row> query(final String sql) {
        return query(sql, ContextRowMapper.generic(dialect));
    }

    @Override
    public <T> SimpleResultSet<T> query(final String sql, final RowMapper<T> rowMapper) {
        return query(sql, ps -> {
        }, rowMapper);
    }

    @Override
    public <T> SimpleResultSet<T> query(final String sql, final PreparedStatementSetter preparedStatementSetter,
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

    @Override
    public <T> BatchInsertBuilder<T> batchInsert(final Class<T> rowType) {
        return new BatchInsertBuilder<>(this::prepareStatement);
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
            this.connection.setAutoCommit(autoCommit);
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Failed to set autoCommit to " + autoCommit, e);
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

    @Override
    public void close() {
        try {
            connection.close();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error closing connection", e);
        }
    }
}
