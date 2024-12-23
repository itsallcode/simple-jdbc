package org.itsallcode.jdbc;

import static java.util.function.Predicate.not;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

import org.itsallcode.jdbc.batch.BatchInsertBuilder;
import org.itsallcode.jdbc.batch.RowBatchInsertBuilder;
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
    default void executeScript(final String sqlScript) {
        Arrays.stream(sqlScript.split(";"))
                .map(String::trim)
                .filter(not(String::isEmpty))
                .forEach(this::executeStatement);
    }

    /**
     * Execute a single SQL statement as a prepared statement with placeholders.
     * 
     * @param sql                     SQL statement
     * @param preparedStatementSetter prepared statement setter
     */
    void executeStatement(final String sql, PreparedStatementSetter preparedStatementSetter);

    /**
     * Execute a single SQL statement.
     * 
     * @param sql SQL statement
     */
    void executeStatement(final String sql);

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
    <T> SimpleResultSet<T> query(final String sql, final RowMapper<T> rowMapper);

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
     * Create a batch insert builder for inserting rows by directly setting values
     * of a {@link PreparedStatement}.
     * <p>
     * If you want to insert rows from an {@link Iterator} or a {@link Stream}, use
     * {@link #batchInsert(Class)}.
     * 
     * @return batch insert builder
     */
    BatchInsertBuilder batchInsert();

    /**
     * Create a row-based batch insert builder for inserting rows from an
     * {@link Iterator} or a {@link Stream}.
     * <p>
     * If you want to insert rows by directly setting values of a
     * {@link PreparedStatement}, use {@link #batchInsert()}.
     * 
     * @param rowType row type
     * @param <T>     row type
     * @return row-based batch insert builder
     */
    <T> RowBatchInsertBuilder<T> batchInsert(final Class<T> rowType);

    @Override
    void close();
}
