package org.itsallcode.jdbc.batch;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.logging.Logger;

import org.itsallcode.jdbc.*;

/**
 * Batch insert of rows. Create a new instance using
 * {@link SimpleConnection#batchInsert(Class)}.
 */
public class BatchInsert implements AutoCloseable {
    private static final Logger LOG = Logger.getLogger(BatchInsert.class.getName());

    private final int maxBatchSize;
    private final SimplePreparedStatement statement;
    private final Instant start;

    private int rows;
    private int currentBatchSize;

    BatchInsert(final SimplePreparedStatement statement, final int maxBatchSize) {
        this.statement = Objects.requireNonNull(statement, "statement");
        this.maxBatchSize = maxBatchSize;
        this.start = Instant.now();
    }

    public void add(final PreparedStatementSetter preparedStatementSetter) {
        statement.setValues(preparedStatementSetter);
        statement.addBatch();
        currentBatchSize++;
        rows++;
        if (rows % maxBatchSize == 0) {
            executeBatch();
        }
    }

    private void executeBatch() {
        if (currentBatchSize == 0) {
            LOG.finest("No rows added to batch, skip");
            return;
        }
        final Instant batchStart = Instant.now();
        final int[] result = statement.executeBatch();
        final Duration duration = Duration.between(batchStart, Instant.now());
        LOG.finest(() -> "Execute batch of %d after %d took %d ms, result length: %s".formatted(currentBatchSize, rows,
                duration.toMillis(), result.length));
        currentBatchSize = 0;
    }

    @Override
    public void close() {
        executeBatch();
        LOG.fine(() -> "Batch insert of %d rows with batch size %d completed in %s".formatted(rows, maxBatchSize,
                Duration.between(start, Instant.now())));
        statement.close();
    }
}
