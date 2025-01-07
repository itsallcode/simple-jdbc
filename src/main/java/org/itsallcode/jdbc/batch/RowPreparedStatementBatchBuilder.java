package org.itsallcode.jdbc.batch;

import java.sql.PreparedStatement;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import org.itsallcode.jdbc.*;
import org.itsallcode.jdbc.identifier.Identifier;

/**
 * Builder for {@link PreparedStatement} batch jobs for a {@link Stream} or
 * {@link Iterable} of row objects using e.g. {@code INSERT} or {@code UPDATE}
 * statements.
 * <p>
 * Create a new instance using
 * {@link DbOperations#preparedStatementBatch(Class)}.
 * 
 * @param <T> row type
 */
public class RowPreparedStatementBatchBuilder<T> {
    private final PreparedStatementBatchBuilder baseBuilder;
    private RowPreparedStatementSetter<T> mapper;
    private Iterator<T> rows;

    /**
     * Create a new instance.
     * 
     * @param statementFactory factory for creating {@link SimplePreparedStatement}
     */
    public RowPreparedStatementBatchBuilder(final Function<String, SimplePreparedStatement> statementFactory) {
        this(new PreparedStatementBatchBuilder(statementFactory));
    }

    RowPreparedStatementBatchBuilder(final PreparedStatementBatchBuilder baseBuilder) {
        this.baseBuilder = baseBuilder;
    }

    /**
     * Define the SQL statement to be used for the batch job, e.g. {@code INSERT} or
     * {@code UPDATE}.
     * 
     * @param sql SQL statement
     * @return {@code this} for fluent programming
     */
    public RowPreparedStatementBatchBuilder<T> sql(final String sql) {
        this.baseBuilder.sql(sql);
        return this;
    }

    /**
     * Define table and column names used for generating the {@code INSERT}
     * statement.
     * 
     * @param tableName   table name
     * @param columnNames column names
     * @return {@code this} for fluent programming
     */
    @SuppressWarnings("java:S3242") // Using List instead of Collection to preserve column order
    public RowPreparedStatementBatchBuilder<T> into(final Identifier tableName, final List<Identifier> columnNames) {
        this.baseBuilder.into(tableName, columnNames);
        return this;
    }

    /**
     * Define table and column names used for generating the {@code INSERT}
     * statement.
     * 
     * @param tableName   table name
     * @param columnNames column names
     * @return {@code this} for fluent programming
     */
    @SuppressWarnings("java:S3242") // Using List instead of Collection to preserve column order
    public RowPreparedStatementBatchBuilder<T> into(final String tableName, final List<String> columnNames) {
        return into(Identifier.simple(tableName), columnNames.stream().map(Identifier::simple).toList());
    }

    /**
     * Define {@link Stream} of rows to insert.
     * 
     * @param rows rows to insert
     * @return {@code this} for fluent programming
     */
    public RowPreparedStatementBatchBuilder<T> rows(final Stream<T> rows) {
        return rows(rows.iterator());
    }

    /**
     * Define {@link Iterator} of rows to insert.
     * 
     * @param rows rows to insert
     * @return {@code this} for fluent programming
     */
    public RowPreparedStatementBatchBuilder<T> rows(final Iterator<T> rows) {
        this.rows = rows;
        return this;
    }

    /**
     * Define mapping how rows are converted to {@code Object[]} for inserting.
     * 
     * @param rowMapper row mapper
     * @return {@code this} for fluent programming
     */
    public RowPreparedStatementBatchBuilder<T> mapping(final ParamConverter<T> rowMapper) {
        final RowPreparedStatementSetter<Object[]> setter = new ObjectArrayPreparedStatementSetter();
        return mapping(
                (final T row, final PreparedStatement preparedStatement) -> setter.setValues(rowMapper.map(row),
                        preparedStatement));
    }

    /**
     * Define {@link RowPreparedStatementSetter} that sets values of a
     * {@link PreparedStatement} for each row.
     * 
     * @param preparedStatementSetter prepared statement setter
     * @return {@code this} for fluent programming
     */
    public RowPreparedStatementBatchBuilder<T> mapping(final RowPreparedStatementSetter<T> preparedStatementSetter) {
        this.mapper = preparedStatementSetter;
        return this;
    }

    /**
     * Define maximum batch size, using
     * {@link PreparedStatementBatchBuilder#DEFAULT_MAX_BATCH_SIZE} as default.
     * 
     * @param maxBatchSize maximum batch size
     * @return {@code this} for fluent programming
     */
    public RowPreparedStatementBatchBuilder<T> maxBatchSize(final int maxBatchSize) {
        this.baseBuilder.maxBatchSize(maxBatchSize);
        return this;
    }

    /**
     * Start the batch insert process using the given rows.
     */
    public void start() {
        Objects.requireNonNull(this.mapper, "mapper");
        Objects.requireNonNull(this.rows, "rows");
        try (PreparedStatementBatch batchInsert = baseBuilder.build();
                RowPreparedStatementBatch<T> rowBatchInsert = new RowPreparedStatementBatch<>(batchInsert,
                        this.mapper)) {
            while (rows.hasNext()) {
                rowBatchInsert.add(rows.next());
            }
        }
    }
}
