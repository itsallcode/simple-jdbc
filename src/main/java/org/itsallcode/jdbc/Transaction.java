package org.itsallcode.jdbc;

import org.itsallcode.jdbc.resultset.RowMapper;
import org.itsallcode.jdbc.resultset.SimpleResultSet;
import org.itsallcode.jdbc.resultset.generic.Row;

public final class Transaction implements DbOperations {

    private final SimpleConnection connection;

    private Transaction(final SimpleConnection connection) {
        this.connection = connection;
    }

    public static Transaction start(final SimpleConnection connection) {
        connection.setAutoCommit(false);
        return new Transaction(connection);
    }

    public void commit() {
        connection.commit();
    }

    public void rollback() {
        connection.rollback();
    }

    @Override
    public void close() {
        this.rollback();
        connection.setAutoCommit(true);
    }

    public void executeStatement(final String sql) {
        connection.executeStatement(sql);
    }

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
}
