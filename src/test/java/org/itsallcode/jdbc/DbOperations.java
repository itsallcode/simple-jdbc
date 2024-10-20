package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.not;

import java.util.Arrays;

import org.itsallcode.jdbc.resultset.RowMapper;
import org.itsallcode.jdbc.resultset.SimpleResultSet;
import org.itsallcode.jdbc.resultset.generic.Row;

public interface DbOperations extends AutoCloseable {
    /**
     * Execute all commands in a SQL script, separated with {@code ;}.
     * 
     * @param sqlScript the script to execute.
     */
    default void executeScript(final String sqlScript) {
        Arrays.stream(sqlScript.split(";"))
                .map(String::trim)
                .filter(not(String::isEmpty))
                .forEach(this::executeStatement);
    }

    /**
     * Execute a single SQL statement.
     * 
     * @param sql the statement
     */
    void executeStatement(final String sql);

    /**
     * Execute a SQL query and return a {@link SimpleResultSet result set} with
     * generic {@link Row}s.
     * 
     * @param sql the query
     * @return the result set
     */
    SimpleResultSet<Row> query(final String sql);

    /**
     * Execute a SQL query and return a {@link SimpleResultSet result set} with rows
     * converted to a custom type {@link T}.
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
     * Create a batch insert builder
     * 
     * @param rowType row type
     * @param <T>     row type
     * @return batch insert builder
     */
    <T> BatchInsertBuilder<T> batchInsert(final Class<T> rowType);
}
