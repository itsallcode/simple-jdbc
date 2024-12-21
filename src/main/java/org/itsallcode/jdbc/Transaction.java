package org.itsallcode.jdbc;

import org.itsallcode.jdbc.resultset.RowMapper;
import org.itsallcode.jdbc.resultset.SimpleResultSet;
import org.itsallcode.jdbc.resultset.generic.Row;

/**
 * A running database transaction. The transaction will be rolled back
 * automatically in {@link #close()}.
 */
public final class Transaction implements DbOperations {

    private final SimpleConnection connection;
    private boolean closed;
    private boolean committed;
    private boolean rolledBack;

    Transaction(final SimpleConnection connection) {
        this.connection = connection;
    }

    static Transaction start(final SimpleConnection connection) {
        connection.setAutoCommit(false);
        return new Transaction(connection);
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
    public void executeStatement(final String sql) {
        checkOperationAllowed();
        connection.executeStatement(sql);
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
    public <T> SimpleResultSet<T> query(final String sql, final RowMapper<T> rowMapper) {
        checkOperationAllowed();
        return connection.query(sql, rowMapper);
    }

    @Override
    public <T> SimpleResultSet<T> query(final String sql, final PreparedStatementSetter preparedStatementSetter,
            final RowMapper<T> rowMapper) {
        checkOperationAllowed();
        return connection.query(sql, preparedStatementSetter, rowMapper);
    }

    @Override
    public <T> BatchInsertBuilder<T> batchInsert(final Class<T> rowType) {
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
     * Rollback transaction and enable auto commit.
     * <p>
     * Explicitly run {@link #commit()} before to commit your transaction.
     * <p>
     * No further operations are allowed on this transaction afterwards.
     */
    @Override
    public void close() {
        if (closed) {
            return;
        }
        if (!rolledBack && !committed) {
            this.rollback();
        }
        connection.setAutoCommit(true);
        this.closed = true;
    }
}
