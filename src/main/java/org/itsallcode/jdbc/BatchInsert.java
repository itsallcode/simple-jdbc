package org.itsallcode.jdbc;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.logging.Logger;

class BatchInsert<T> implements AutoCloseable {
    private static final Logger LOG = Logger.getLogger(BatchInsert.class.getName());
    private static final int BATCH_SIZE = 200_000;

    private final SimplePreparedStatement statement;
    private final RowPreparedStatementSetter<T> preparedStatementSetter;

    private int rows;
    private int currentBatchSize;

    BatchInsert(final SimplePreparedStatement statement, final RowPreparedStatementSetter<T> preparedStatementSetter) {
        this.preparedStatementSetter = preparedStatementSetter;
        this.statement = Objects.requireNonNull(statement, "statement");
    }

    void add(final T row) {
        statement.setValues(stmt -> this.preparedStatementSetter.setValues(row, stmt));
        statement.addBatch();
        currentBatchSize++;
        rows++;
        if (rows % BATCH_SIZE == 0) {
            executeBatch();
        }
    }

    @Override
    public void close() {
        executeBatch();
        statement.close();
    }

    private void executeBatch() {
        if (currentBatchSize == 0) {
            LOG.finest("No rows added to batch, skip");
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
