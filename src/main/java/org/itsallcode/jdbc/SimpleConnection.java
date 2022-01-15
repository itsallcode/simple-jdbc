package org.itsallcode.jdbc;

import static java.util.function.Predicate.not;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public class SimpleConnection implements AutoCloseable
{
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
