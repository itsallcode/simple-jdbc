package org.itsallcode.jdbc.resultset;

import java.util.List;

/**
 * Represents a generic row from a result set.
 */
public class Row {
    private final int rowIndex;
    private final List<ColumnValue> fields;

    Row(final int rowIndex, final List<ColumnValue> fields) {
        this.rowIndex = rowIndex;
        this.fields = fields;
    }

    /**
     * Row index (zero based).
     * 
     * @return row index
     */
    public int getRowIndex() {
        return rowIndex;
    }

    /**
     * Values for each column.
     * 
     * @return column values
     */
    public List<ColumnValue> getColumnValues() {
        return fields;
    }

    /**
     * Get the value at a given column index (zero based).
     * 
     * @param columnIndex column index
     * @return column value
     */
    public ColumnValue getColumnValue(final int columnIndex) {
        return fields.get(columnIndex);
    }

    @Override
    public String toString() {
        return "ResultSetRow [rowIndex=" + rowIndex + ", fields=" + fields + "]";
    }
}
