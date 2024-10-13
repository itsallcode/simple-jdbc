package org.itsallcode.jdbc;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.itsallcode.jdbc.dialect.DbDialect;
import org.itsallcode.jdbc.identifier.Identifier;
import org.itsallcode.jdbc.resultset.*;
import org.itsallcode.jdbc.resultset.generic.Row;

/**
 * A simplified version of a JDBC {@link Connection}. Create new connections
 * with {@link ConnectionFactory#create(String, String, String)}.
 */
public class SimpleConnection implements AutoCloseable {
    private static final Logger LOG = Logger.getLogger(SimpleConnection.class.getName());

    private final Connection connection;
    private final Context context;
    private final DbDialect dialect;

    SimpleConnection(final Connection connection, final Context context, final DbDialect dialect) {
        this.connection = Objects.requireNonNull(connection, "connection");
        this.context = Objects.requireNonNull(context, "context");
        this.dialect = Objects.requireNonNull(dialect, "dialect");
    }

    /**
     * Execute all commands in a SQL script, separated with {@code ;}.
     * 
     * @param sqlScript the script to execute.
     */
    public void executeScript(final String sqlScript) {
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
    public void executeStatement(final String sql) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error executing '" + sql + "'", e);
        }
    }

    /**
     * Execute a SQL query and return a {@link SimpleResultSet result set} with
     * generic {@link Row}s.
     * 
     * @param sql the query
     * @return the result set
     */
    public SimpleResultSet<Row> query(final String sql) {
        return query(sql, ContextRowMapper.generic(dialect));
    }

    /**
     * Execute a SQL query and return a {@link SimpleResultSet result set} with rows
     * converted to a custom type {@link T}.
     * 
     * @param <T>       generic row type
     * @param sql       SQL query
     * @param rowMapper row mapper
     * @return the result set
     */
    public <T> SimpleResultSet<T> query(final String sql, final RowMapper<T> rowMapper) {
        return query(sql, ps -> {
        }, rowMapper);
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
    public <T> SimpleResultSet<T> query(final String sql, final PreparedStatementSetter preparedStatementSetter,
            final RowMapper<T> rowMapper) {
        LOG.finest(() -> "Executing query '" + sql + "'...");
        final SimplePreparedStatement statement = prepareStatement(sql);
        statement.setValues(preparedStatementSetter);
        return statement.executeQuery(ContextRowMapper.create(rowMapper));
    }

    private SimplePreparedStatement prepareStatement(final String sql) {
        return new SimplePreparedStatement(context, dialect, prepare(sql), sql);
    }

    /**
     * Insert rows into a table using batch operation.
     * 
     * @param <T>         generic row type
     * @param table       table name
     * @param columnNames column names
     * @param rowMapper   a mapper to convert each column
     * @param rows        a stream of rows to insert
     */
    public <T> void insert(final String table, final List<String> columnNames, final ParamConverter<T> rowMapper,
            final Stream<T> rows) {
        final Stream<Object[]> objectArrayRows = rows.map(row -> rowMapper.map(row));
        insert(table, columnNames, new ArgumentPreparedStatementSetter(context.getParameterMapper()),
                objectArrayRows);
    }

    public <T> void insert(final String table, final List<String> columnNames,
            final RowPreparedStatementSetter<T> preparedStatementSetter,
            final Stream<T> rows) {
        insert(Identifier.simple(table), columnNames.stream().map(Identifier::simple).toList(), preparedStatementSetter,
                rows);
    }

    /**
     * Insert rows into a table using batch operation.
     * 
     * @param <T>         generic row type
     * @param table       table name
     * @param columnNames column names
     * @param rowMapper   a mapper to convert each column
     * @param rows        a stream of rows to insert
     */
    public <T> void insert(final Identifier table, final List<Identifier> columnNames,
            final RowPreparedStatementSetter<T> preparedStatementSetter, final Stream<T> rows) {
        insert(createInsertStatement(table, columnNames), preparedStatementSetter, rows);
    }

    private static String createInsertStatement(final Identifier table, final List<Identifier> columnNames) {
        final String columns = columnNames.stream().map(Identifier::quote).collect(joining(","));
        final String placeholders = columnNames.stream().map(n -> "?").collect(joining(","));
        return "insert into " + table.quote() + " (" + columns + ") values (" + placeholders + ")";
    }

    <T> void insert(final String sql, final RowPreparedStatementSetter<T> preparedStatementSetter,
            final Stream<T> rows) {
        LOG.finest(() -> "Running insert statement '" + sql + "'...");
        try (SimpleBatch<T> batch = new SimpleBatch<>(prepareStatement(sql), preparedStatementSetter)) {
            rows.forEach(batch::add);
        } finally {
            rows.close();
        }
    }

    private PreparedStatement prepare(final String sql) {
        try {
            return connection.prepareStatement(sql);
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error preparing statement '" + sql + "'", e);
        }
    }

    /**
     * Database dialect of this connection.
     * 
     * @return dialect
     */
    public DbDialect getDialect() {
        return dialect;
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
