package org.itsallcode.jdbc.resultset.generic;

import java.util.List;

/**
 * Represents a generic row from a result set.
 * 
 * @param rowIndex     row index (zero based)
 * @param columns      column metadata
 * @param columnValues values for each column
 */
public record Row(int rowIndex, List<ColumnMetaData> columns, List<ColumnValue> columnValues) {

    /**
     * Get the value at a given column index (zero based).
     * 
     * @param columnIndex column index (zero based)
     * @return column value
     */
    public ColumnValue get(final int columnIndex) {
        return columnValues.get(columnIndex);
    }

    /**
     * Get the value at a given column index (zero based) converted to the given
     * type.
     * 
     * @param columnIndex column index (zero based)
     * @param type        expected type
     * @param <T>         expected type
     * @return column value
     */
    public <T> T get(final int columnIndex, final Class<T> type) {
        return get(columnIndex).getValue(type);
    }
}
