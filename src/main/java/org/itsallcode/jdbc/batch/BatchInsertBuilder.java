package org.itsallcode.jdbc.batch;

import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Logger;

import org.itsallcode.jdbc.SimplePreparedStatement;
import org.itsallcode.jdbc.identifier.Identifier;

public class BatchInsertBuilder {
    private static final Logger LOG = Logger.getLogger(BatchInsertBuilder.class.getName());
    private static final int DEFAULT_MAX_BATCH_SIZE = 200_000;
    private final Function<String, SimplePreparedStatement> statementFactory;
    private String sql;
    private int maxBatchSize = DEFAULT_MAX_BATCH_SIZE;

    public BatchInsertBuilder(final Function<String, SimplePreparedStatement> statementFactory) {
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
    public BatchInsertBuilder into(final Identifier tableName, final List<Identifier> columnNames) {
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
    public BatchInsertBuilder into(final String tableName, final List<String> columnNames) {
        return into(Identifier.simple(tableName), columnNames.stream().map(Identifier::simple).toList());
    }

    /**
     * Define maximum batch size, using {@link #DEFAULT_MAX_BATCH_SIZE} as default.
     * 
     * @param maxBatchSize maximum batch size
     * @return {@code this} for fluent programming
     */
    public BatchInsertBuilder maxBatchSize(final int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
        return this;
    }

    private static String createInsertStatement(final Identifier table, final List<Identifier> columnNames) {
        final String columns = columnNames.stream().map(Identifier::quote).collect(joining(","));
        final String placeholders = columnNames.stream().map(n -> "?").collect(joining(","));
        return "insert into " + table.quote() + " (" + columns + ") values (" + placeholders + ")";
    }

    /**
     * Built the batch inserter.
     * 
     * @return
     */
    public BatchInsert build() {
        Objects.requireNonNull(this.sql, "sql");
        LOG.finest(() -> "Running insert statement '" + sql + "'...");
        final SimplePreparedStatement statement = statementFactory.apply(sql);
        return new BatchInsert(statement, this.maxBatchSize);
    }
}
