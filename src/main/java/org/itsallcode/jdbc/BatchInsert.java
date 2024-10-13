package org.itsallcode.jdbc;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.logging.Logger;

class BatchInsert<T> implements AutoCloseable {
    private static final Logger LOG = Logger.getLogger(BatchInsert.class.getName());

    private final int maxBatchSize;
    private final SimplePreparedStatement statement;
    private final RowPreparedStatementSetter<T> preparedStatementSetter;
    private final Instant start;

    private int rows;
    private int currentBatchSize;

    BatchInsert(final SimplePreparedStatement statement, final RowPreparedStatementSetter<T> preparedStatementSetter,
            final int maxBatchSize) {
        this.preparedStatementSetter = preparedStatementSetter;
        this.statement = Objects.requireNonNull(statement, "statement");
        this.maxBatchSize = maxBatchSize;
        this.start = Instant.now();
    }

    void add(final T row) {
        statement.setValues(stmt -> this.preparedStatementSetter.setValues(row, stmt));
        statement.addBatch();
        currentBatchSize++;
        rows++;
        if (rows % maxBatchSize == 0) {
            executeBatch();
        }
    }

    @Override
    public void close() {
        executeBatch();
        LOG.fine(() -> "Batch insert of %d rows with batch size %d completed in %s".formatted(rows, maxBatchSize,
                Duration.between(start, Instant.now())));
        statement.close();
    }

    private void executeBatch() {
        if (currentBatchSize == 0) {
            LOG.finest("No rows added to batch, skip");
            return;
        }
        final Instant batchStart = Instant.now();
        statement.executeBatch();
        final Duration duration = Duration.between(batchStart, Instant.now());
        LOG.finest(() -> "Execute batch of " + currentBatchSize + " after " + rows + " took " + duration.toMillis()
                + " ms");
        currentBatchSize = 0;
    }
}
