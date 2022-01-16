package org.itsallcode.jdbc;

import static java.util.stream.Collectors.toList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.itsallcode.jdbc.resultset.ResultSetRow;
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

    public List<ResultSetRow> executeQueryGetRows()
    {
        try (SimpleResultSet resultSet = this.executeQuery())
        {
            return resultSet.stream().collect(toList());
        }
    }

    public SimpleResultSet executeQuery()
    {
        return new SimpleResultSet(execute(), context);
    }

    public SimpleBatch startBatch()
    {
        return new SimpleBatch(statement, context);
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
