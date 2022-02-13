package org.itsallcode.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.itsallcode.jdbc.resultset.RowMapper;
import org.itsallcode.jdbc.resultset.SimpleResultSet;

class SimplePreparedStatement implements AutoCloseable
{
    private final PreparedStatement statement;
    private final String sql;

    SimplePreparedStatement(PreparedStatement statement, String sql)
    {
        this.statement = statement;
        this.sql = sql;
    }

    <T> SimpleResultSet<T> executeQuery(RowMapper<T> rowMapper)
    {
        final ResultSet resultSet = doExecute();
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
            throw new UncheckedSQLException("Error executing query '" + sql + "'", e);
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

    void setValues(PreparedStatementSetter preparedStatementSetter)
    {
        try
        {
            preparedStatementSetter.setValues(statement);
        }
        catch (final SQLException e)
        {
            throw new UncheckedSQLException("Error setting values for prepared statement", e);
        }

    }

    void executeBatch()
    {
        try
        {
            statement.executeBatch();
        }
        catch (final SQLException e)
        {
            throw new UncheckedSQLException("Error executing batch sql '" + sql + "'", e);
        }
    }

    void addBatch()
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

    SimpleParameterMetaData getParameterMetadata()
    {
        try
        {
            return new SimpleParameterMetaData(statement.getParameterMetaData());
        }
        catch (final SQLException e)
        {
            throw new UncheckedSQLException("Error getting parameter metadata", e);
        }
    }
}
