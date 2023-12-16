package org.itsallcode.jdbc.resultset.generic;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Represents the metadata of a single column.
 * 
 * @param columnIndex column index (1 based)
 * @param name        column name
 * @param label       column label
 * @param type        SQL type
 */
public record ColumnMetaData(int columnIndex, String name, String label, ColumnType type) {

    static ColumnMetaData create(final ResultSetMetaData metaData, final int columnIndex)
            throws SQLException {
        final String label = metaData.getColumnLabel(columnIndex);
        final String name = metaData.getColumnName(columnIndex);
        final ColumnType columnType = ColumnType.create(metaData, columnIndex);
        return new ColumnMetaData(columnIndex, name, label, columnType);
    }
}