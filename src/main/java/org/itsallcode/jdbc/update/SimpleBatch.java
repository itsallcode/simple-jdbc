package org.itsallcode.jdbc.update;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.itsallcode.jdbc.ArgumentPreparedStatementSetter;
import org.itsallcode.jdbc.Context;
import org.itsallcode.jdbc.PreparedStatementSetter;
import org.itsallcode.jdbc.SimpleParameterMetaData.Parameter;
import org.itsallcode.jdbc.SimplePreparedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleBatch implements AutoCloseable
{
    private static final Logger LOG = LoggerFactory.getLogger(SimpleBatch.class);

    private static final int BATCH_SIZE = 200000;
    private final SimplePreparedStatement statement;
    private final Context context;
    private final List<Parameter> parameterMetadata;

    private int rows = 0;
    private int currentBatchSize = 0;

    public SimpleBatch(SimplePreparedStatement statement, Context context)
    {
        this.statement = statement;
        this.context = context;
        this.parameterMetadata = statement.getParameterMetadata().getParameters();
    }

    public SimpleBatch add(Object... args)
    {
        validateParameters(args);
        return add(new ArgumentPreparedStatementSetter(context.getParameterMapper(), args));
    }

    private void validateParameters(Object... args)
    {
        if (args.length != this.parameterMetadata.size())
        {
            throw new IllegalStateException(
                    "Expected " + this.parameterMetadata.size() + " arguments but got " + args.length + ": "
                            + Arrays.toString(args) + ", " + parameterMetadata);
        }
    }

    private SimpleBatch add(PreparedStatementSetter preparedStatementSetter)
    {
        statement.setValues(preparedStatementSetter);
        statement.addBatch();
        currentBatchSize++;
        if (++rows % BATCH_SIZE == 0)
        {
            executeBatch();
        }
        return this;
    }

    @Override
    public void close()
    {
        executeBatch();
        statement.close();
    }

    private void executeBatch()
    {
        final Instant start = Instant.now();
        statement.executeBatch();
        final Duration duration = Duration.between(start, Instant.now());
        LOG.debug("Execute batch of {} after {} took {} ms", currentBatchSize, rows, duration.toMillis());
        currentBatchSize = 0;
    }
}
