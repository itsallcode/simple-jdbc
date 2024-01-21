package org.itsallcode.jdbc.resultset.generic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.itsallcode.jdbc.UncheckedSQLException;

/**
 * Represents the metadata of a single column.
 * 
 * @param columnIndex column index (1 based)
 * @param name        column name
 * @param label       column label
 * @param type        SQL type
 */
public record ColumnMetaData(int columnIndex, String name, String label, ColumnType type) {

    static List<ColumnMetaData> create(final ResultSet resultSet) {
        try {
            return create(resultSet.getMetaData());
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error extracting meta data", e);
        }
    }

    private static List<ColumnMetaData> create(final ResultSetMetaData metaData)
            throws SQLException {
        final int columnCount = metaData.getColumnCount();
        final List<ColumnMetaData> columns = new ArrayList<>(columnCount);
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            columns.add(ColumnMetaData.create(metaData, columnIndex));
        }
        return columns;
    }

    private static ColumnMetaData create(final ResultSetMetaData metaData, final int columnIndex)
            throws SQLException {
        final String label = metaData.getColumnLabel(columnIndex);
        final String name = metaData.getColumnName(columnIndex);
        final ColumnType columnType = ColumnType.create(metaData, columnIndex);
        return new ColumnMetaData(columnIndex, name, label, columnType);
    }
}