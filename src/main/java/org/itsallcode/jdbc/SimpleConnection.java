package org.itsallcode.jdbc;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.itsallcode.jdbc.resultset.GenericRowMapper;
import org.itsallcode.jdbc.resultset.ResultSetRow;
import org.itsallcode.jdbc.resultset.RowMapper;
import org.itsallcode.jdbc.resultset.SimpleResultSet;
import org.itsallcode.jdbc.update.ParamConverter;
import org.itsallcode.jdbc.update.SimpleBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleConnection implements AutoCloseable
{
    private static final Logger LOG = LoggerFactory.getLogger(SimpleConnection.class);

    private final Connection connection;
    private final Context context;

    SimpleConnection(Connection connection, Context context)
    {
        this.connection = connection;
        this.context = context;
    }

    public void executeScript(String sqlScript)
    {
        Arrays.stream(sqlScript.split(";"))
                .map(String::trim)
                .filter(not(String::isEmpty))
                .forEach(this::executeStatement);
    }

    public void executeStatement(String sql)
    {
        try (Statement statement = connection.createStatement())
        {
            statement.execute(sql);
        }
        catch (final SQLException e)
        {
            throw new UncheckedSQLException("Error executing '" + sql + "'", e);
        }
    }

    public SimpleResultSet<ResultSetRow> query(String sql)
    {
        return query(sql, new GenericRowMapper(context));
    }

    public <T> SimpleResultSet<T> query(String sql, RowMapper<T> rowMapper)
    {
        return query(sql, ps -> {}, rowMapper);
    }

    public <T> SimpleResultSet<T> query(String sql, PreparedStatementSetter preparedStatementSetter,
            RowMapper<T> rowMapper)
    {
        final SimplePreparedStatement statement = prepareStatement(sql);
        statement.setValues(preparedStatementSetter);
        return statement.executeQuery(rowMapper);
    }

    public SimpleBatch batch(String sql)
    {
        return new SimpleBatch(prepareStatement(sql), context);
    }

    private SimplePreparedStatement prepareStatement(String sql)
    {
        return new SimplePreparedStatement(prepare(sql));
    }

    public <T> void insert(String table, List<String> columnNames, ParamConverter<T> rowMapper, Stream<T> rows)
    {
        insert(createInsertStatement(table, columnNames), rowMapper, rows);
    }

    private String createInsertStatement(String table, List<String> columnNames)
    {
        final String columns = columnNames.stream().map(n -> "\"" + n + "\"").collect(joining(","));
        final String placeholders = columnNames.stream().map(n -> "?").collect(joining(","));
        return "insert into \"" + table + "\" (" + columns + ") values (" + placeholders + ")";
    }

    public <T> void insert(String sql, ParamConverter<T> rowMapper, Stream<T> rows)
    {
        LOG.debug("Running insert statement '{}'...", sql);
        batch(sql).add();
        try (SimpleBatch batch = batch(sql))
        {
            rows.map(rowMapper::map).forEach(batch::add);
        }
        finally
        {
            rows.close();
        }
    }

    private PreparedStatement prepare(String sql)
    {
        try
        {
            return connection.prepareStatement(sql);
        }
        catch (final SQLException e)
        {
            throw new UncheckedSQLException("Error preparing statement '" + sql + "'", e);
        }
    }

    @Override
    public void close()
    {
        try
        {
            connection.close();
        }
        catch (final SQLException e)
        {
            throw new UncheckedSQLException("Error closing connection", e);
        }
    }
}
