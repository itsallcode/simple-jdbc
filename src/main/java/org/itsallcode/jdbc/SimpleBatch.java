package org.itsallcode.jdbc;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.itsallcode.jdbc.SimpleParameterMetaData.Parameter;

class SimpleBatch implements AutoCloseable {
    private static final Logger LOG = Logger.getLogger(SimpleBatch.class.getName());
    private static final int BATCH_SIZE = 200000;

    private final SimplePreparedStatement statement;
    private final Context context;
    private final List<Parameter> parameterMetadata;

    private int rows = 0;
    private int currentBatchSize = 0;

    SimpleBatch(final SimplePreparedStatement statement, final Context context) {
        this.statement = statement;
        this.context = context;
        this.parameterMetadata = statement.getParameterMetadata().getParameters();
    }

    SimpleBatch add(final Object... args) {
        validateParameters(args);
        return add(new ArgumentPreparedStatementSetter(context.getParameterMapper(), args));
    }

    private void validateParameters(final Object... args) {
        if (args.length != this.parameterMetadata.size()) {
            throw new IllegalStateException(
                    "Expected " + this.parameterMetadata.size() + " arguments but got " + args.length + ": "
                            + Arrays.toString(args) + ", " + parameterMetadata);
        }
    }

    private SimpleBatch add(final PreparedStatementSetter preparedStatementSetter) {
        statement.setValues(preparedStatementSetter);
        statement.addBatch();
        currentBatchSize++;
        if (++rows % BATCH_SIZE == 0) {
            executeBatch();
        }
        return this;
    }

    @Override
    public void close() {
        executeBatch();
        statement.close();
    }

    private void executeBatch() {
        if (currentBatchSize == 0) {
            LOG.fine("No rows added to batch, skip");
            return;
        }
        final Instant start = Instant.now();
        statement.executeBatch();
        final Duration duration = Duration.between(start, Instant.now());
        LOG.fine(() -> "Execute batch of " + currentBatchSize + " after " + rows + " took " + duration.toMillis()
                + " ms");
        currentBatchSize = 0;
    }
}
