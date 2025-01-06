package org.itsallcode.jdbc.batch;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

/**
 * A generic batch handler that takes care of maximum batch size and executing
 * the last batch before closing.
 */
class Batch implements AutoCloseable {
    private static final Logger LOG = Logger.getLogger(Batch.class.getName());

    private final Runnable batchExecutor;
    private final AutoCloseable resource;
    private final int maxBatchSize;
    private final Instant start;

    private int totalCount;
    private int currentBatchSize;

    Batch(final int maxBatchSize, final AutoCloseable resource, final Runnable batchExecutor) {
        this.maxBatchSize = maxBatchSize;
        this.resource = resource;
        this.batchExecutor = batchExecutor;
        this.start = Instant.now();
    }

    void addBatch() {
        this.currentBatchSize++;
        this.totalCount++;
        if (this.currentBatchSize >= this.maxBatchSize) {
            this.executeBatch();
        }
    }

    private void executeBatch() {
        if (currentBatchSize == 0) {
            LOG.finest("No items added to batch, skip");
            return;
        }
        final Instant batchStart = Instant.now();
        batchExecutor.run();
        final Duration duration = Duration.between(batchStart, Instant.now());
        LOG.finest(() -> "Execute batch of %d after %d took %d ms".formatted(currentBatchSize, totalCount,
                duration.toMillis()));
        currentBatchSize = 0;
    }

    @Override
    public void close() {
        executeBatch();
        closeResource();
        LOG.fine(() -> "Batch processing of %d items with batch size %d completed in %s".formatted(totalCount,
                maxBatchSize,
                Duration.between(start, Instant.now())));
    }

    private void closeResource() {
        try {
            resource.close();
        } catch (final Exception e) {
            throw new IllegalStateException("Failed to close resource: " + e.getMessage(), e);
        }
    }
}
