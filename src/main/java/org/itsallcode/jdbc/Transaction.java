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

    private Transaction(final SimpleConnection connection) {
        this.connection = connection;
    }

    static Transaction start(final SimpleConnection connection) {
        connection.setAutoCommit(false);
        return new Transaction(connection);
    }

    /**
     * Commit the transaction.
     */
    public void commit() {
        connection.commit();
    }

    /**
     * Rollback the transaction.
     */
    public void rollback() {
        connection.rollback();
    }

    @Override
    public void executeStatement(final String sql) {
        connection.executeStatement(sql);
    }

    @Override
    public void executeStatement(final String sql, final PreparedStatementSetter preparedStatementSetter) {
        connection.executeStatement(sql, preparedStatementSetter);
    }

    @Override
    public SimpleResultSet<Row> query(final String sql) {
        return connection.query(sql);
    }

    @Override
    public void executeScript(final String sqlScript) {
        connection.executeScript(sqlScript);
    }

    @Override
    public <T> SimpleResultSet<T> query(final String sql, final RowMapper<T> rowMapper) {
        return connection.query(sql, rowMapper);
    }

    @Override
    public <T> SimpleResultSet<T> query(final String sql, final PreparedStatementSetter preparedStatementSetter,
            final RowMapper<T> rowMapper) {
        return connection.query(sql, preparedStatementSetter, rowMapper);
    }

    @Override
    public <T> BatchInsertBuilder<T> batchInsert(final Class<T> rowType) {
        return connection.batchInsert(rowType);
    }

    /**
     * Rollback transaction and enable auto commit.
     * <p>
     * Explicitly run {@link #commit()} before to commit your transaction.
     */
    @Override
    public void close() {
        this.rollback();
        connection.setAutoCommit(true);
    }
}
