package org.itsallcode.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SimpleBatch
{
    private final PreparedStatement statement;

    public SimpleBatch(PreparedStatement statement, Context context)
    {
        this.statement = statement;
    }

    public SimpleBatch add(Object... parameters)
    {
        int parameterIndex = 1;
        for (final Object object : parameters)
        {
            setParameter(parameterIndex, object);
            parameterIndex++;
        }
        addBatch();
        return this;
    }

    public SimpleBatch executeBatch()
    {
        doExecuteBatch();
        return this;
    }

    private void doExecuteBatch()
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

    private void addBatch()
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

    private void setParameter(int parameterIndex, final Object object)
    {
        try
        {
            statement.setObject(parameterIndex, object);
        }
        catch (final SQLException e)
        {
            throw new UncheckedSQLException("Error setting parameter " + parameterIndex + " value " + object, e);
        }
    }
}
