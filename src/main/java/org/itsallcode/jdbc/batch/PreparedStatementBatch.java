package org.itsallcode.jdbc.batch;

import java.sql.PreparedStatement;
import java.util.Objects;

import org.itsallcode.jdbc.*;

/**
 * Direct batch insert using {@link PreparedStatement}. Create a new instance
 * using {@link SimpleConnection#batchInsert()}.
 */
public class PreparedStatementBatch implements AutoCloseable {
    private final Batch batch;
    private final SimplePreparedStatement statement;

    PreparedStatementBatch(final SimplePreparedStatement statement, final int maxBatchSize) {
        this.statement = Objects.requireNonNull(statement, "statement");
        this.batch = new Batch(maxBatchSize, statement, statement::executeBatch);
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
        batch.addBatch();
    }

    @Override
    public void close() {
        batch.close();
    }
}
