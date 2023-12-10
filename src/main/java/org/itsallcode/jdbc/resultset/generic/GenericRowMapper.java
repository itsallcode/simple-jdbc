package org.itsallcode.jdbc.resultset.generic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.itsallcode.jdbc.Context;
import org.itsallcode.jdbc.UncheckedSQLException;
import org.itsallcode.jdbc.resultset.RowMapper;
import org.itsallcode.jdbc.resultset.generic.SimpleMetaData.ColumnMetaData;

/**
 * This {@link RowMapper} converts a row to the generic {@link Row} type.
 * 
 * @param <T> generic row type
 */
public class GenericRowMapper<T> implements RowMapper<T> {
    private ResultSetRowBuilder rowBuilder;
    private final ColumnValuesConverter<T> converter;

    /**
     * Create a new instance.
     * 
     * @param converter optionally converts each generic {@link Row} to a different
     *                  type.
     */
    public GenericRowMapper(final ColumnValuesConverter<T> converter) {
        this.converter = converter;
    }

    @Override
    public T mapRow(final Context context, final ResultSet resultSet, final int rowNum) throws SQLException {
        if (rowBuilder == null) {
            rowBuilder = new ResultSetRowBuilder(SimpleMetaData.create(resultSet.getMetaData(), context));
        }
        final Row row = rowBuilder.buildRow(resultSet, rowNum);
        return converter.mapRow(row);
    }

    private class ResultSetRowBuilder {
        private final SimpleMetaData metadata;

        private ResultSetRowBuilder(final SimpleMetaData metaData) {
            this.metadata = metaData;
        }

        private Row buildRow(final ResultSet resultSet, final int rowIndex) {
            final List<ColumnMetaData> columns = metadata.getColumns();
            final List<ColumnValue> fields = new ArrayList<>(columns.size());
            for (final ColumnMetaData column : columns) {
                final ColumnValue field = getField(resultSet, column, rowIndex);
                fields.add(field);
            }
            return Row.create(rowIndex, columns, fields);
        }

        private ColumnValue getField(final ResultSet resultSet, final ColumnMetaData column, final int rowIndex) {
            try {
                return column.getValueExtractor().extractValue(resultSet, column.getColumnIndex());
            } catch (final SQLException e) {
                throw new UncheckedSQLException(
                        "Error extracting value for row " + rowIndex + " / column " + column + ": " + e.getMessage(),
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
