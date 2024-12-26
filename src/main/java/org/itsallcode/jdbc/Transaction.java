package org.itsallcode.jdbc;

import org.itsallcode.jdbc.batch.BatchInsertBuilder;
import org.itsallcode.jdbc.batch.RowBatchInsertBuilder;
import org.itsallcode.jdbc.resultset.RowMapper;
import org.itsallcode.jdbc.resultset.SimpleResultSet;
import org.itsallcode.jdbc.resultset.generic.Row;

/**
 * A running database transaction. The transaction will be rolled back
 * automatically in {@link #close()} if not explicitly committed using
 * {@link #commit()} or rolled back using {@link #rollback()} before.
 * <p>
 * Start a new transaction using {@link SimpleConnection#startTransaction()}.
 * <p>
 * Operations are not allowed on a closed, committed or rolled back transaction.
 * <p>
 * Closing an already closed transaction is a no-op.
 */
public final class Transaction implements DbOperations {

    private final SimpleConnection connection;
    private final boolean restoreAutoCommitRequired;
    private boolean closed;
    private boolean committed;
    private boolean rolledBack;

    private Transaction(final SimpleConnection connection, final boolean restoreAutoCommitRequired) {
        this.connection = connection;
        this.restoreAutoCommitRequired = restoreAutoCommitRequired;
    }

    static Transaction start(final SimpleConnection connection) {
        boolean restoreAutoCommitRequired = false;
        if (connection.getAutoCommit()) {
            connection.setAutoCommit(false);
            restoreAutoCommitRequired = true;
        }
        return new Transaction(connection, restoreAutoCommitRequired);
    }

    /**
     * Commit the transaction.
     * <p>
     * No further operations are allowed on this transaction afterwards.
     */
    public void commit() {
        checkOperationAllowed();
        connection.commit();
        this.committed = true;
    }

    /**
     * Rollback the transaction.
     * <p>
     * No further operations are allowed on this transaction afterwards.
     */
    public void rollback() {
        checkOperationAllowed();
        connection.rollback();
        this.rolledBack = true;
    }

    @Override
    public void executeStatement(final String sql, final PreparedStatementSetter preparedStatementSetter) {
        checkOperationAllowed();
        connection.executeStatement(sql, preparedStatementSetter);
    }

    @Override
    public SimpleResultSet<Row> query(final String sql) {
        checkOperationAllowed();
        return connection.query(sql);
    }

    @Override
    public void executeScript(final String sqlScript) {
        checkOperationAllowed();
        connection.executeScript(sqlScript);
    }

    @Override
    public <T> SimpleResultSet<T> query(final String sql, final PreparedStatementSetter preparedStatementSetter,
            final RowMapper<T> rowMapper) {
        checkOperationAllowed();
        return connection.query(sql, preparedStatementSetter, rowMapper);
    }

    @Override
    public BatchInsertBuilder batchInsert() {
        checkOperationAllowed();
        return connection.batchInsert();
    }

    @Override
    public <T> RowBatchInsertBuilder<T> batchInsert(final Class<T> rowType) {
        checkOperationAllowed();
        return connection.batchInsert(rowType);
    }

    private void checkOperationAllowed() {
        if (this.closed) {
            throw new IllegalStateException("Operation not allowed on closed transaction");
        }
        if (this.rolledBack) {
            throw new IllegalStateException("Operation not allowed on rolled back transaction");
        }
        if (this.committed) {
            throw new IllegalStateException("Operation not allowed on committed transaction");
        }
    }

    /**
     * Rollback transaction if not already committed or rolled back and restore
     * original auto commit setting if necessary.
     * <p>
     * Explicitly run {@link #commit()} before to commit your transaction.
     * <p>
     * No further operations are allowed on this transaction afterwards.
     * <p>
     * This <em>does not</em> close the connection, so you can continue using it.
     */
    @Override
    public void close() {
        if (closed) {
            return;
        }
        if (!rolledBack && !committed) {
            this.rollback();
        }
        if (restoreAutoCommitRequired) {
            connection.setAutoCommit(true);
        }
        this.closed = true;
    }
}
