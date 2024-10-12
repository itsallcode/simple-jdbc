package org.itsallcode.jdbc;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;

import org.itsallcode.jdbc.SimpleParameterMetaData.Parameter;

class SimpleBatch implements AutoCloseable {
    private static final Logger LOG = Logger.getLogger(SimpleBatch.class.getName());
    private static final int BATCH_SIZE = 200_000;

    private final SimplePreparedStatement statement;
    private final Context context;
    private final List<Parameter> parameterMetadata;

    private int rows;
    private int currentBatchSize;

    SimpleBatch(final SimplePreparedStatement statement, final Context context) {
        this.statement = Objects.requireNonNull(statement, "statement");
        this.context = Objects.requireNonNull(context, "context");
        this.parameterMetadata = statement.getParameterMetadata().parameters();
    }

    @SuppressWarnings("java:S923") // Varargs required
    SimpleBatch add(final Object... args) {
        validateParameters(args);
        return add(new ArgumentPreparedStatementSetter(context.getParameterMapper(), args));
    }

    @SuppressWarnings("java:S923") // Varargs required
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
        rows++;
        if (rows % BATCH_SIZE == 0) {
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
        LOG.finest(() -> "Execute batch of " + currentBatchSize + " after " + rows + " took " + duration.toMillis()
                + " ms");
        currentBatchSize = 0;
    }
}
