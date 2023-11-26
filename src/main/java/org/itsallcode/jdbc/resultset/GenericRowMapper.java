package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.itsallcode.jdbc.Context;
import org.itsallcode.jdbc.UncheckedSQLException;
import org.itsallcode.jdbc.resultset.SimpleMetaData.ColumnMetaData;

/**
 * This {@link RowMapper} converts a row to the generic {@link Row} type.
 */
class GenericRowMapper implements RowMapper<Row> {
    private final Context context;
    private ResultSetRowBuilder rowBuilder;

    /**
     * Create a new instance.
     * 
     * @param context context
     */
    GenericRowMapper(final Context context) {
        this.context = context;
    }

    @Override
    public Row mapRow(final ResultSet resultSet, final int rowNum) throws SQLException {
        if (rowBuilder == null) {
            rowBuilder = new ResultSetRowBuilder(SimpleMetaData.create(resultSet.getMetaData(), context));
        }
        return rowBuilder.buildRow(resultSet, rowNum);
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
            return new Row(rowIndex, fields);
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

}
