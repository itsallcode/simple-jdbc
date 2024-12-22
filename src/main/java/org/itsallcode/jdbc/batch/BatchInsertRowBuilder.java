package org.itsallcode.jdbc.batch;

import static java.util.stream.Collectors.joining;

import java.sql.PreparedStatement;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.itsallcode.jdbc.*;
import org.itsallcode.jdbc.identifier.Identifier;

/**
 * Builder for batch inserts.
 * 
 * @param <T> row type
 */
public class BatchInsertRowBuilder<T> {
    private static final Logger LOG = Logger.getLogger(BatchInsertRowBuilder.class.getName());
    private static final int DEFAULT_MAX_BATCH_SIZE = 200_000;
    private final Function<String, SimplePreparedStatement> statementFactory;
    private String sql;
    private RowPreparedStatementSetter<T> mapper;
    private Iterator<T> rows;
    private int maxBatchSize = DEFAULT_MAX_BATCH_SIZE;

    public BatchInsertRowBuilder(final Function<String, SimplePreparedStatement> statementFactory) {
        this.statementFactory = statementFactory;
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
    public BatchInsertRowBuilder<T> into(final Identifier tableName, final List<Identifier> columnNames) {
        this.sql = createInsertStatement(tableName, columnNames);
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
    public BatchInsertRowBuilder<T> into(final String tableName, final List<String> columnNames) {
        return into(Identifier.simple(tableName), columnNames.stream().map(Identifier::simple).toList());
    }

    /**
     * Define {@link Stream} of rows to insert.
     * 
     * @param rows rows to insert
     * @return {@code this} for fluent programming
     */
    public BatchInsertRowBuilder<T> rows(final Stream<T> rows) {
        return rows(rows.iterator());
    }

    /**
     * Define {@link Iterator} of rows to insert.
     * 
     * @param rows rows to insert
     * @return {@code this} for fluent programming
     */
    public BatchInsertRowBuilder<T> rows(final Iterator<T> rows) {
        this.rows = rows;
        return this;
    }

    /**
     * Define mapping how rows are converted to {@code Object[]} for inserting.
     * 
     * @param rowMapper row mapper
     * @return {@code this} for fluent programming
     */
    public BatchInsertRowBuilder<T> mapping(final ParamConverter<T> rowMapper) {
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
    public BatchInsertRowBuilder<T> mapping(final RowPreparedStatementSetter<T> preparedStatementSetter) {
        this.mapper = preparedStatementSetter;
        return this;
    }

    /**
     * Define maximum batch size, using {@link #DEFAULT_MAX_BATCH_SIZE} as default.
     * 
     * @param maxBatchSize maximum batch size
     * @return {@code this} for fluent programming
     */
    public BatchInsertRowBuilder<T> maxBatchSize(final int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
        return this;
    }

    private static String createInsertStatement(final Identifier table, final List<Identifier> columnNames) {
        final String columns = columnNames.stream().map(Identifier::quote).collect(joining(","));
        final String placeholders = columnNames.stream().map(n -> "?").collect(joining(","));
        return "insert into " + table.quote() + " (" + columns + ") values (" + placeholders + ")";
    }

    /**
     * Start the batch insert process using the given rows.
     */
    public void start() {
        Objects.requireNonNull(this.sql, "sql");
        Objects.requireNonNull(this.mapper, "mapper");
        Objects.requireNonNull(this.rows, "rows");
        LOG.finest(() -> "Running insert statement '" + sql + "'...");
        final SimplePreparedStatement statement = statementFactory.apply(sql);
        try (BatchInsertRow<T> batch = new BatchInsertRow<>(statement, this.mapper, this.maxBatchSize)) {
            while (rows.hasNext()) {
                batch.add(rows.next());
            }
        }
    }
}
