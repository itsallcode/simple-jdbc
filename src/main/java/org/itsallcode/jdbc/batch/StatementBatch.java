package org.itsallcode.jdbc.batch;

import org.itsallcode.jdbc.SimpleStatement;

/**
 * A batch handler for SQL statements.
 */
public class StatementBatch implements AutoCloseable {

    private final Batch batch;
    private final SimpleStatement statement;

    /**
     * Create a new instance.
     * 
     * @param statement    the statement
     * @param maxBatchSize maximum batch size
     */
    public StatementBatch(final SimpleStatement statement, final int maxBatchSize) {
        this.statement = statement;
        this.batch = new Batch(maxBatchSize, statement, statement::executeBatch);
    }

    /**
     * Add a new SQL statement to the batch.
     * 
     * @param sql SQL statement
     */
    public void addBatch(final String sql) {
        statement.addBatch(sql);
        this.batch.addBatch();
    }

    @Override
    public void close() {
        this.batch.close();
    }
}
