package org.itsallcode.jdbc.update;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;

import org.itsallcode.jdbc.Context;
import org.itsallcode.jdbc.UncheckedSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleBatch implements AutoCloseable
{
    private static final Logger LOG = LoggerFactory.getLogger(SimpleBatch.class);

    private static final int BATCH_SIZE = 200000;
    private final PreparedStatement statement;
    private final Context context;

    private int rows = 0;
    private int currentBatchSize = 0;

    public SimpleBatch(PreparedStatement statement, Context context)
    {
        this.statement = statement;
        this.context = context;
    }

    public SimpleBatch add(Object... parameters)
    {
        setParameters(parameters);
        addBatch();
        currentBatchSize++;
        if (++rows % BATCH_SIZE == 0)
        {
            executeBatch();
        }
        return this;
    }

    private void setParameters(Object... parameters)
    {
        final ParameterMapper mapper = context.getParameterMapper();
        int parameterIndex = 1;
        for (final Object object : parameters)
        {
            setParameter(parameterIndex, mapper.map(object));
            parameterIndex++;
        }
    }

    @Override
    public void close()
    {
        executeBatch();
    }

    private void executeBatch()
    {
        final Instant start = Instant.now();
        doExecuteBatch();
        final Duration duration = Duration.between(start, Instant.now());
        LOG.debug("Execute batch of {} after {} took {} ms", currentBatchSize, rows, duration.toMillis());
        currentBatchSize = 0;
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
            throw new UncheckedSQLException("Error setting parameter " + parameterIndex + " value " + object
                    + " of type " + object.getClass().getName() + ": " + e.getMessage(), e);
        }
    }
}
