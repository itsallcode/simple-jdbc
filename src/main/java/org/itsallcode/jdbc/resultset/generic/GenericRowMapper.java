package org.itsallcode.jdbc.resultset.generic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.itsallcode.jdbc.UncheckedSQLException;
import org.itsallcode.jdbc.dialect.DbDialect;
import org.itsallcode.jdbc.resultset.*;

/**
 * This {@link ContextRowMapper} converts a row to the generic {@link Row} type.
 * 
 * @param <T> generic row type
 */
public class GenericRowMapper<T> implements RowMapper<T> {
    private ResultSetRowBuilder rowBuilder;
    private final ColumnValuesConverter<T> converter;
    private final DbDialect dialect;

    /**
     * Create a new instance.
     * 
     * @param dialect   database dialect
     * @param converter optionally converts each generic {@link Row} to a different
     *                  type.
     */
    public GenericRowMapper(final DbDialect dialect, final ColumnValuesConverter<T> converter) {
        this.dialect = Objects.requireNonNull(dialect, "dialect");
        this.converter = Objects.requireNonNull(converter, "converter");
    }

    @Override
    public T mapRow(final ResultSet resultSet, final int rowNum) throws SQLException {
        if (rowBuilder == null) {
            rowBuilder = new ResultSetRowBuilder(SimpleMetaData.create(resultSet));
        }
        final Row row = rowBuilder.buildRow(ConvertingResultSet.create(dialect, resultSet), rowNum);
        return converter.mapRow(row);
    }

    private static final class ResultSetRowBuilder {
        private final SimpleMetaData metadata;

        private ResultSetRowBuilder(final SimpleMetaData metaData) {
            this.metadata = metaData;
        }

        private Row buildRow(final ResultSet resultSet, final int rowIndex) {
            final List<ColumnMetaData> columns = metadata.columns();
            final List<ColumnValue> fields = new ArrayList<>(columns.size());
            for (final ColumnMetaData column : columns) {
                final ColumnValue field = getField(resultSet, column, rowIndex);
                fields.add(field);
            }
            return new Row(rowIndex, columns, fields);
        }

        private static ColumnValue getField(final ResultSet resultSet, final ColumnMetaData column,
                final int rowIndex) {
            final Object value = getValue(resultSet, column, rowIndex);
            return new ColumnValue(column.type(), value);
        }

        private static Object getValue(final ResultSet resultSet, final ColumnMetaData column, final int rowIndex) {
            try {
                return resultSet.getObject(column.columnIndex());
            } catch (final SQLException e) {
                throw new UncheckedSQLException("Error extracting value for row " + rowIndex + " / column " + column,
                        e);
            }
        }
    }

    /**
     * A simplified row mapper that gets a list of column values as input.
     * 
     * @param <T> generic row type
     */
    @FunctionalInterface
    @SuppressWarnings("java:S1711") // Explicit interface instead of generic Function<>
    public interface ColumnValuesConverter<T> {
        /**
         * Convert a single row.
         * 
         * @param row column values
         * @return converted object
         */
        T mapRow(Row row);
    }
}
