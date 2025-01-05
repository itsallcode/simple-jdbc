package org.itsallcode.jdbc.batch;

import org.itsallcode.jdbc.SimpleStatement;

public class StatementBatch implements AutoCloseable {

    private final Batch batch;
    private final SimpleStatement statement;

    public StatementBatch(final SimpleStatement statement, final int maxBatchSize) {
        this.statement = statement;
        this.batch = new Batch(maxBatchSize, statement, statement::executeBatch);
    }

    public void addBatch(final String sql) {
        statement.addBatch(sql);
        this.batch.addBatch();
    }

    @Override
    public void close() {
        this.batch.close();
    }
}
