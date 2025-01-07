package org.itsallcode.jdbc.batch;

import org.itsallcode.jdbc.RowPreparedStatementSetter;
import org.itsallcode.jdbc.SimplePreparedStatement;

class RowPreparedStatementBatch<T> implements AutoCloseable {

    private final PreparedStatementBatch batchInsert;
    private final RowPreparedStatementSetter<T> preparedStatementSetter;

    RowPreparedStatementBatch(final SimplePreparedStatement statement,
            final RowPreparedStatementSetter<T> preparedStatementSetter,
            final int maxBatchSize) {
        this(new PreparedStatementBatch(statement, maxBatchSize), preparedStatementSetter);
    }

    RowPreparedStatementBatch(final PreparedStatementBatch batchInsert,
            final RowPreparedStatementSetter<T> preparedStatementSetter) {
        this.batchInsert = batchInsert;
        this.preparedStatementSetter = preparedStatementSetter;
    }

    void add(final T row) {
        batchInsert.add(stmt -> this.preparedStatementSetter.setValues(row, stmt));
    }

    @Override
    public void close() {
        this.batchInsert.close();
    }
}
