package org.itsallcode.jdbc.batch;

import java.sql.PreparedStatement;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.logging.Logger;

import org.itsallcode.jdbc.*;

/**
 * Direct batch insert using {@link PreparedStatement}. Create a new instance
 * using {@link SimpleConnection#batchInsert()}.
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

    /**
     * Add a new row to the batch.
     * <p>
     * <em>Important:</em> This method automatically calls
     * {@link PreparedStatement#addBatch()}. No need to call it separately.
     * 
     * @param preparedStatementSetter prepared statement setter that is used for
     *                                setting row values of the
     *                                {@link PreparedStatement}.
     */
    public void add(final PreparedStatementSetter preparedStatementSetter) {
        statement.setValues(preparedStatementSetter);
        addBatch();
    }

    /**
     * Get the {@link PreparedStatement} that is used for the batch insert. Use this
     * to set values on the {@link PreparedStatement} before calling
     * {@link #addBatch()}.
     * <p>
     * Use this method if you want to set values on the {@link PreparedStatement}
     * directly and you need more control. Prefer using
     * {@link #add(PreparedStatementSetter)} if possible.
     * 
     * @return the {@link PreparedStatement} used for the batch insert
     */
    public PreparedStatement getStatement() {
        return statement.getStatement();
    }

    /**
     * Add a new row to the batch. Only call this method if you have set all values
     * on the {@link PreparedStatement} retrieved from {@link #statement}.
     * <p>
     * Don't call this if you use {@link #add(PreparedStatementSetter)}.
     */
    public void addBatch() {
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
