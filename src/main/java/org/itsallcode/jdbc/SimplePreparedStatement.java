package org.itsallcode.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.itsallcode.jdbc.resultset.SimpleResultSet;

public class SimplePreparedStatement implements AutoCloseable
{
    private final PreparedStatement statement;
    private final Context context;

    public SimplePreparedStatement(PreparedStatement statement, Context context)
    {
        this.statement = statement;
        this.context = context;
    }

    public SimpleResultSet executeQuery()
    {
        return new SimpleResultSet(execute(), context);
    }

    private ResultSet execute()
    {
        try
        {
            return statement.executeQuery();
        }
        catch (final SQLException e)
        {
            throw new UncheckedSQLException("Error executing query", e);
        }
    }

    @Override
    public void close()
    {
        try
        {
            statement.close();
        }
        catch (final SQLException e)
        {
            throw new UncheckedSQLException("Error closing statement", e);
        }
    }
}
