package org.itsallcode.jdbc.batch;

import org.itsallcode.jdbc.RowPreparedStatementSetter;
import org.itsallcode.jdbc.SimplePreparedStatement;

class BatchInsertRow<T> implements AutoCloseable {

    private final BatchInsert batchInsert;
    private final RowPreparedStatementSetter<T> preparedStatementSetter;

    BatchInsertRow(final SimplePreparedStatement statement, final RowPreparedStatementSetter<T> preparedStatementSetter,
            final int maxBatchSize) {
        this(new BatchInsert(statement, maxBatchSize), preparedStatementSetter);
    }

    private BatchInsertRow(final BatchInsert batchInsert, final RowPreparedStatementSetter<T> preparedStatementSetter) {
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
