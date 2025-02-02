package org.itsallcode.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.itsallcode.jdbc.batch.*;
import org.itsallcode.jdbc.resultset.RowMapper;
import org.itsallcode.jdbc.resultset.SimpleResultSet;
import org.itsallcode.jdbc.resultset.generic.Row;

/**
 * Interface containing various DB operations. Use one of the implementations
 * {@link SimpleConnection} or {@link Transaction}.
 */
public interface DbOperations extends AutoCloseable {

    /**
     * Execute all commands in a SQL script, separated with {@code ;}.
     * 
     * @param sqlScript script to execute.
     */
    void executeScript(final String sqlScript);

    /**
     * Execute a single SQL statement.
     * 
     * @param sql SQL statement
     * @return either the row count for SQL Data Manipulation Language (DML)
     *         statements or 0 for SQL statements that return nothing
     */
    int executeUpdate(final String sql);

    /**
     * Execute a single SQL statement as a prepared statement with placeholders.
     * <p>
     * This will use {@link PreparedStatement#setObject(int, Object)} for setting
     * parameters. If you need more control, use
     * {@link #executeUpdate(String, PreparedStatementSetter)}.
     * 
     * @param sql        SQL statement
     * @param parameters parameters to set in the prepared statement
     * @return either the row count for SQL Data Manipulation Language (DML)
     *         statements or 0 for SQL statements that return nothing
     */
    default int executeUpdate(final String sql, final List<Object> parameters) {
        return this.executeUpdate(sql, new GenericParameterSetter(parameters));
    }

    /**
     * Execute a single SQL statement as a prepared statement with placeholders.
     * 
     * @param sql                     SQL statement
     * @param preparedStatementSetter prepared statement setter
     * @return either the row count for SQL Data Manipulation Language (DML)
     *         statements or 0 for SQL statements that return nothing
     */
    int executeUpdate(final String sql, PreparedStatementSetter preparedStatementSetter);

    /**
     * Execute a SQL query and return a {@link SimpleResultSet result set} with
     * generic {@link Row}s.
     * 
     * @param sql SQL query
     * @return result set
     */
    SimpleResultSet<Row> query(final String sql);

    /**
     * Execute a SQL query and return a {@link SimpleResultSet result set} with rows
     * converted to a custom type {@link T} using the given {@link RowMapper}.
     * 
     * @param <T>       generic row type
     * @param sql       SQL query
     * @param rowMapper row mapper
     * @return the result set
     */
    default <T> SimpleResultSet<T> query(final String sql, final RowMapper<T> rowMapper) {
        return query(sql, ps -> {
        }, rowMapper);
    }

    /**
     * Execute a SQL query, set parameters and return a {@link SimpleResultSet
     * result set} with rows converted to a custom type {@link T}.
     * <p>
     * This will use {@link PreparedStatement#setObject(int, Object)} for setting
     * parameters. If you need more control, use
     * {@link #executeUpdate(String, PreparedStatementSetter)}.
     * 
     * @param <T>        generic row type
     * @param sql        SQL query
     * @param parameters parameters to set in the prepared statement
     * @param rowMapper  row mapper
     * @return the result set
     */
    default <T> SimpleResultSet<T> query(final String sql, final List<Object> parameters,
            final RowMapper<T> rowMapper) {
        return this.query(sql, new GenericParameterSetter(parameters), rowMapper);
    }

    /**
     * Execute a SQL query, set parameters and return a {@link SimpleResultSet
     * result set} with rows converted to a custom type {@link T}.
     * 
     * @param <T>                     generic row type
     * @param sql                     SQL query
     * @param preparedStatementSetter the prepared statement setter
     * @param rowMapper               row mapper
     * @return the result set
     */
    <T> SimpleResultSet<T> query(final String sql, final PreparedStatementSetter preparedStatementSetter,
            final RowMapper<T> rowMapper);

    /**
     * Create a batch statement builder for executing multiple statements in a
     * batch.
     * 
     * @return batch statement builder
     */
    StatementBatchBuilder statementBatch();

    /**
     * Create a prepared statement batch builder for inserting or updating rows by
     * directly setting values of a {@link PreparedStatement}.
     * <p>
     * If you want to insert rows from an {@link Iterator} or a {@link Stream}, use
     * {@link #preparedStatementBatch(Class)}.
     * 
     * @return batch insert builder
     */
    PreparedStatementBatchBuilder preparedStatementBatch();

    /**
     * Create a row-based prepared statement batch builder for inserting or updating
     * rows from an {@link Iterator} or a {@link Stream}.
     * <p>
     * If you want to insert rows by directly setting values of a
     * {@link PreparedStatement}, use {@link #preparedStatementBatch()}.
     * 
     * @param rowType row type
     * @param <T>     row type
     * @return row-based batch insert builder
     */
    <T> RowPreparedStatementBatchBuilder<T> preparedStatementBatch(final Class<T> rowType);

    /**
     * Get the original wrapped connection.
     * <p>
     * Use this in case of missing features in {@link DbOperations}.
     * 
     * @return original wrapped connection
     */
    Connection getOriginalConnection();

    @Override
    void close();
}
