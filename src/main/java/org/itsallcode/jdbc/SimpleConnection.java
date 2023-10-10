package org.itsallcode.jdbc;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.itsallcode.jdbc.identifier.Identifier;
import org.itsallcode.jdbc.identifier.SimpleIdentifier;
import org.itsallcode.jdbc.resultset.*;

/**
 * A simplified version of a JDBC {@link Connection}. Create new connections
 * with {@link ConnectionFactory#create(String, String, String)}.
 */
public class SimpleConnection implements AutoCloseable {
    private static final Logger LOG = Logger.getLogger(SimpleConnection.class.getName());

    private final Connection connection;
    private final Context context;

    SimpleConnection(final Connection connection, final Context context) {
        this.connection = connection;
        this.context = context;
    }

    public void executeScriptFromResource(final String resourceName) {
        executeScript(readResource(resourceName));
    }

    public void executeScript(final String sqlScript) {
        Arrays.stream(sqlScript.split(";"))
                .map(String::trim)
                .filter(not(String::isEmpty))
                .forEach(this::executeStatement);
    }

    public void executeStatement(final String sql) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error executing '" + sql + "'", e);
        }
    }

    public SimpleResultSet<Row> query(final String sql) {
        return query(sql, new GenericRowMapper(context));
    }

    public <T> SimpleResultSet<T> querySqlResource(final String resourceName, final RowMapper<T> rowMapper) {
        return query(readResource(resourceName), rowMapper);
    }

    private String readResource(final String resourceName) {
        final URL resource = getClass().getResource(resourceName);
        if (resource == null) {
            throw new IllegalArgumentException("No resource found for name '" + resourceName + "'");
        }
        try (InputStream stream = resource.openStream()) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (final IOException e) {
            throw new UncheckedIOException("Error reading resource " + resource, e);
        }
    }

    public <T> SimpleResultSet<T> query(final String sql, final RowMapper<T> rowMapper) {
        return query(sql, ps -> {
        }, rowMapper);
    }

    public <T> SimpleResultSet<T> query(final String sql, final PreparedStatementSetter preparedStatementSetter,
            final RowMapper<T> rowMapper) {
        LOG.fine(() -> "Executing query '" + sql + "'...");
        final SimplePreparedStatement statement = prepareStatement(sql);
        statement.setValues(preparedStatementSetter);
        return statement.executeQuery(rowMapper);
    }

    private SimplePreparedStatement prepareStatement(final String sql) {
        return new SimplePreparedStatement(prepare(sql), sql);
    }

    public <T> void insert(final String table, final List<String> columnNames, final ParamConverter<T> rowMapper,
            final Stream<T> rows) {
        insert(SimpleIdentifier.of(table), columnNames.stream().map(SimpleIdentifier::of).toList(), rowMapper, rows);
    }

    public <T> void insert(final Identifier table, final List<Identifier> columnNames,
            final ParamConverter<T> rowMapper,
            final Stream<T> rows) {
        insert(createInsertStatement(table, columnNames), rowMapper, rows);
    }

    private String createInsertStatement(final Identifier table, final List<Identifier> columnNames) {
        final String columns = columnNames.stream().map(Identifier::quote).collect(joining(","));
        final String placeholders = columnNames.stream().map(n -> "?").collect(joining(","));
        return "insert into " + table.quote() + " (" + columns + ") values (" + placeholders + ")";
    }

    public <T> void insert(final String sql, final ParamConverter<T> paramConverter, final Stream<T> rows) {
        LOG.fine(() -> "Running insert statement '" + sql + "'...");
        try (SimpleBatch batch = new SimpleBatch(prepareStatement(sql), context)) {
            rows.map(paramConverter::map).forEach(batch::add);
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

    @Override
    public void close() {
        try {
            connection.close();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error closing connection", e);
        }
    }
}
