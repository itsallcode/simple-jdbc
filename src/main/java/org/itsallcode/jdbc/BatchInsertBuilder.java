package org.itsallcode.jdbc;

import static java.util.stream.Collectors.joining;

import java.sql.PreparedStatement;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.itsallcode.jdbc.identifier.Identifier;

/**
 * Builder for batch inserts.
 */
public class BatchInsertBuilder<T> {
    private static final Logger LOG = Logger.getLogger(BatchInsertBuilder.class.getName());
    private static final int DEFAULT_MAX_BATCH_SIZE = 200_000;
    private final SimpleConnection connection;
    private final Context context;
    private String sql;
    private RowPreparedStatementSetter<T> mapper;
    private Iterator<T> rows;
    private int maxBatchSize = DEFAULT_MAX_BATCH_SIZE;

    BatchInsertBuilder(final SimpleConnection connection, final Context context) {
        this.connection = connection;
        this.context = context;
    }

    @SuppressWarnings("java:S3242") // Using List instead of Collection to preserve column order
    public BatchInsertBuilder<T> into(final String tableName, final List<String> columnNames) {
        this.sql = createInsertStatement(Identifier.simple(tableName),
                columnNames.stream().map(Identifier::simple).toList());
        return this;
    }

    public BatchInsertBuilder<T> rows(final Stream<T> rows) {
        final Iterator<T> iterator = rows.iterator();
        return rows(iterator);
    }

    public BatchInsertBuilder<T> rows(final Iterator<T> rows) {
        this.rows = rows;
        return this;
    }

    public BatchInsertBuilder<T> mapping(final ParamConverter<T> rowMapper) {
        final RowPreparedStatementSetter<Object[]> setter = new ObjectArrayPreparedStatementSetter(
                context.getParameterMapper());
        return mapping(
                (final T row, final PreparedStatement preparedStatement) -> setter.setValues(rowMapper.map(row),
                        preparedStatement));
    }

    public BatchInsertBuilder<T> mapping(final RowPreparedStatementSetter<T> preparedStatementSetter) {
        this.mapper = preparedStatementSetter;
        return this;
    }

    public BatchInsertBuilder<T> maxBatchSize(final int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
        return this;
    }

    private static String createInsertStatement(final Identifier table, final List<Identifier> columnNames) {
        final String columns = columnNames.stream().map(Identifier::quote).collect(joining(","));
        final String placeholders = columnNames.stream().map(n -> "?").collect(joining(","));
        return "insert into " + table.quote() + " (" + columns + ") values (" + placeholders + ")";
    }

    public void start() {
        Objects.requireNonNull(this.sql, "sql");
        Objects.requireNonNull(this.mapper, "mapper");
        Objects.requireNonNull(this.rows, "rows");
        LOG.finest(() -> "Running insert statement '" + sql + "'...");
        final SimplePreparedStatement statement = connection.prepareStatement(sql);
        try (BatchInsert<T> batch = new BatchInsert<>(statement, this.mapper, this.maxBatchSize)) {
            while (rows.hasNext()) {
                batch.add(rows.next());
            }
        }
    }
}
