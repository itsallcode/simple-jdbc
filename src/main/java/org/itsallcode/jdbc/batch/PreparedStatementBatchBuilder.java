package org.itsallcode.jdbc.batch;

import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Logger;

import org.itsallcode.jdbc.SimpleConnection;
import org.itsallcode.jdbc.SimplePreparedStatement;
import org.itsallcode.jdbc.identifier.Identifier;

/**
 * Builder for {@link PreparedStatementBatch}. Create a new builder instance
 * using {@link SimpleConnection#preparedStatementBatch()}.
 */
public class PreparedStatementBatchBuilder {
    private static final Logger LOG = Logger.getLogger(PreparedStatementBatchBuilder.class.getName());
    /** Default maximum batch size. */
    public static final int DEFAULT_MAX_BATCH_SIZE = 200_000;
    private final Function<String, SimplePreparedStatement> statementFactory;
    private String sql;
    private int maxBatchSize = DEFAULT_MAX_BATCH_SIZE;

    /**
     * Create a new instance.
     * 
     * @param statementFactory factory for creating {@link SimplePreparedStatement}.
     */
    public PreparedStatementBatchBuilder(final Function<String, SimplePreparedStatement> statementFactory) {
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
    public PreparedStatementBatchBuilder into(final Identifier tableName, final List<Identifier> columnNames) {
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
    public PreparedStatementBatchBuilder into(final String tableName, final List<String> columnNames) {
        return into(Identifier.simple(tableName), columnNames.stream().map(Identifier::simple).toList());
    }

    /**
     * Define maximum batch size, using {@link #DEFAULT_MAX_BATCH_SIZE} as default.
     * 
     * @param maxBatchSize maximum batch size
     * @return {@code this} for fluent programming
     */
    public PreparedStatementBatchBuilder maxBatchSize(final int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
        return this;
    }

    private static String createInsertStatement(final Identifier table, final List<Identifier> columnNames) {
        final String columns = columnNames.stream().map(Identifier::quote).collect(joining(","));
        final String placeholders = columnNames.stream().map(n -> "?").collect(joining(","));
        return "insert into " + table.quote() + " (" + columns + ") values (" + placeholders + ")";
    }

    /**
     * Build the batch inserter.
     * 
     * @return the batch inserter
     */
    public PreparedStatementBatch build() {
        Objects.requireNonNull(this.sql, "sql");
        LOG.finest(() -> "Running insert statement '" + sql + "'...");
        final SimplePreparedStatement statement = statementFactory.apply(sql);
        return new PreparedStatementBatch(statement, this.maxBatchSize);
    }
}
