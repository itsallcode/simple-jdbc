package org.itsallcode.jdbc;

import java.sql.*;
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
 * A simplified version of a JDBC {@link Connection}. Create new connections
 * with
 * <ul>
 * <li>{@link ConnectionFactory#create(String, String, String)}</li>
 * <li>or {@link DataSourceConnectionFactory#getConnection()}</li>
 * <li>or {@link #wrap(Connection, DbDialect)}</li>
 * </ul>
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

    /**
     * Wrap an existing {@link Connection} with a {@link SimpleConnection}.
     * <p>
     * Note: Calling {@link #close()} will close the underlying connection.
     * 
     * @param connection existing connection
     * @param dialect    database dialect
     * @return wrapped connection
     */
    public static SimpleConnection wrap(final Connection connection, final DbDialect dialect) {
        return new SimpleConnection(connection, Context.builder().build(), dialect);
    }

    /**
     * Start a new {@link Transaction} by disabling auto commit if necessary.
     * 
     * @return new transaction
     */
    public Transaction startTransaction() {
        return Transaction.start(this);
    }

    @Override
    public void executeStatement(final String sql) {
        this.executeStatement(sql, stmt -> {
        });
    }

    @Override
    public void executeStatement(final String sql, final PreparedStatementSetter preparedStatementSetter) {
        try (SimplePreparedStatement statement = prepareStatement(sql)) {
            statement.setValues(preparedStatementSetter);
            statement.execute();
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
    public BatchInsertBuilder batchInsert() {
        return new BatchInsertBuilder(this::prepareStatement);
    }

    @Override
    public <T> RowBatchInsertBuilder<T> batchInsert(final Class<T> rowType) {
        return new RowBatchInsertBuilder<>(this::prepareStatement);
    }

    private PreparedStatement prepare(final String sql) {
        try {
            return connection.prepareStatement(sql);
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error preparing statement '" + sql + "'", e);
        }
    }

    /**
     * Set the auto commit state.
     * 
     * @param autoCommit auto commit state
     * @see Connection#setAutoCommit(boolean)
     */
    void setAutoCommit(final boolean autoCommit) {
        try {
            this.connection.setAutoCommit(autoCommit);
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Failed to set autoCommit to " + autoCommit, e);
        }
    }

    /**
     * Get the current auto commit state.
     * 
     * @return auto commit state
     * @see Connection#getAutoCommit()
     */
    boolean getAutoCommit() {
        try {
            return this.connection.getAutoCommit();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Failed to get autoCommit", e);
        }
    }

    /**
     * Rollback the transaction.
     * 
     * @see Connection#rollback()
     */
    void rollback() {
        try {
            this.connection.rollback();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Failed to rollback transaction", e);
        }
    }

    /**
     * Commit the transaction.
     * 
     * @see Connection#commit()
     */
    void commit() {
        try {
            this.connection.commit();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Failed to commit transaction", e);
        }
    }

    /**
     * Close the underlying {@link Connection}.
     * 
     * @see Connection#close()
     */
    @Override
    public void close() {
        try {
            connection.close();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error closing connection", e);
        }
    }
}
