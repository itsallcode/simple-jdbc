package org.itsallcode.jdbc;

import static java.util.function.Predicate.not;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Stream;

import org.itsallcode.jdbc.update.RowMapper;
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
        try
        {
            connection.createStatement().execute(sql);
        }
        catch (final SQLException e)
        {
            throw new UncheckedSQLException("Error executing '" + sql + "'", e);
        }
    }

    public SimplePreparedStatement prepareStatement(String sql)
    {
        return new SimplePreparedStatement(prepare(sql), context);
    }

    public <T> void insert(String sql, RowMapper<T> rowMapper, Stream<T> rows)
    {
        LOG.debug("Running insert statement '{}'...", sql);
        try (SimpleBatch batch = prepareStatement(sql).startBatch())
        {
            rows.map(rowMapper::map).forEach(batch::add);
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
