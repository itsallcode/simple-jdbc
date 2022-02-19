package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.itsallcode.jdbc.UncheckedSQLException;
import org.itsallcode.jdbc.resultset.SimpleMetaData.ColumnMetaData;

class ResultSetRowBuilder {
    private final SimpleMetaData metadata;

    ResultSetRowBuilder(SimpleMetaData metaData) {
        this.metadata = metaData;
    }

    Row buildRow(ResultSet resultSet, int rowIndex) {
        final List<ColumnMetaData> columns = metadata.getColumns();
        final List<ColumnValue> fields = new ArrayList<>(columns.size());
        for (final ColumnMetaData column : columns) {
            final ColumnValue field = getField(resultSet, column, rowIndex);
            fields.add(field);
        }
        return new Row(rowIndex, fields);
    }

    private ColumnValue getField(ResultSet resultSet, final ColumnMetaData column, int rowIndex) {
        ColumnValue field;
        try {
            field = column.getValueExtractor().extractValue(resultSet, column.getColumnIndex());
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error extracting value for row " + rowIndex + " / column " + column, e);
        }
        return field;
    }
}
