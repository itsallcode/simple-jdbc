package org.itsallcode.jdbc;

import java.sql.Connection;
import java.util.logging.Logger;

import org.itsallcode.jdbc.batch.BatchInsertBuilder;
import org.itsallcode.jdbc.batch.RowBatchInsertBuilder;
import org.itsallcode.jdbc.dialect.DbDialect;
import org.itsallcode.jdbc.resultset.RowMapper;
import org.itsallcode.jdbc.resultset.SimpleResultSet;
import org.itsallcode.jdbc.resultset.generic.Row;

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

    private Transaction transaction;

    private final ConnectionWrapper connection;

    SimpleConnection(final Connection connection, final Context context, final DbDialect dialect) {
        this.connection = new ConnectionWrapper(connection, context, dialect);
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
        checkOperationAllowed();
        transaction = Transaction.start(this.connection);
        return transaction;
    }

    private void checkOperationAllowed() {
        // if (transaction != null) {
        // throw new IllegalStateException("Operation not allowed on connection when
        // transaction is active");
        // }
        if (this.connection.isClosed()) {
            throw new IllegalStateException("Operation not allowed on closed connection");
        }
    }

    @Override
    public void executeScript(final String sqlScript) {
        checkOperationAllowed();
        connection.executeScript(sqlScript);
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
        return connection.rowBatchInsert();
    }

    /**
     * Close the underlying {@link Connection}.
     * 
     * @see Connection#close()
     */
    @Override
    public void close() {
        connection.close();
    }
}
