package org.itsallcode.jdbc.update;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.itsallcode.jdbc.Context;
import org.itsallcode.jdbc.UncheckedSQLException;

public class SimpleBatch
{
    private final PreparedStatement statement;
    private final Context context;

    public SimpleBatch(PreparedStatement statement, Context context)
    {
        this.statement = statement;
        this.context = context;
    }

    public SimpleBatch add(Object... parameters)
    {
        final ParameterMapper mapper = context.getParameterMapper();
        int parameterIndex = 1;
        for (final Object object : parameters)
        {
            setParameter(parameterIndex, mapper.map(object));
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
