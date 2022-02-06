package org.itsallcode.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.itsallcode.jdbc.resultset.RowMapper;
import org.itsallcode.jdbc.resultset.SimpleResultSet;

public class SimplePreparedStatement implements AutoCloseable
{
    private final PreparedStatement statement;

    SimplePreparedStatement(PreparedStatement statement)
    {
        this.statement = statement;
    }

    <T> SimpleResultSet<T> executeQuery(RowMapper<T> rowMapper)
    {
        ResultSet resultSet = doExecute();
        return new SimpleResultSet<>(resultSet, rowMapper);
    }

    private ResultSet doExecute()
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

    public void setValues(PreparedStatementSetter preparedStatementSetter)
    {
        try
        {
            preparedStatementSetter.setValues(statement);
        }
        catch (SQLException e)
        {
            throw new UncheckedSQLException("Error setting values for prepared statement", e);
        }

    }

    public void executeBatch()
    {
        try
        {
            statement.executeBatch();
        }
        catch (final SQLException e)
        {
            throw new UncheckedSQLException("Error executing batch", e);
        }
    }

    public void addBatch()
    {
        try
        {
            this.statement.addBatch();
        }
        catch (final SQLException e)
        {
            throw new UncheckedSQLException("Error adding batch", e);
        }
    }
}
