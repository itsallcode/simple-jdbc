package org.itsallcode.jdbc.batch;

import java.util.function.Supplier;

import org.itsallcode.jdbc.SimpleStatement;

/**
 * A builder for {@link StatementBatch}.
 */
public class StatementBatchBuilder {
    private final Supplier<SimpleStatement> statementFactory;
    private int maxBatchSize = PreparedStatementBatchBuilder.DEFAULT_MAX_BATCH_SIZE;

    /**
     * Create a new instance.
     * 
     * @param statementFactory factory for creating {@link SimpleStatement}.
     */
    public StatementBatchBuilder(final Supplier<SimpleStatement> statementFactory) {
        this.statementFactory = statementFactory;
    }

    /**
     * Define maximum batch size, using
     * {@link PreparedStatementBatchBuilder#DEFAULT_MAX_BATCH_SIZE} as default.
     * 
     * @param maxBatchSize maximum batch size
     * @return {@code this} for fluent programming
     */
    public StatementBatchBuilder maxBatchSize(final int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
        return this;
    }

    /**
     * Build the batch inserter.
     * 
     * @return the statement batch
     */
    public StatementBatch build() {
        final SimpleStatement statement = statementFactory.get();
        return new StatementBatch(statement, this.maxBatchSize);
    }
}
