package org.itsallcode.jdbc.resultset.generic;

import java.util.List;

import org.itsallcode.jdbc.resultset.generic.SimpleMetaData.ColumnMetaData;

/**
 * Represents a generic row from a result set.
 */
public class Row {
    private final int rowIndex;
    private final List<ColumnValue> fields;

    private Row(final int rowIndex, final List<ColumnValue> fields) {
        this.rowIndex = rowIndex;
        this.fields = fields;
    }

    static Row create(final int rowIndex, final List<ColumnMetaData> columns, final List<ColumnValue> fields) {
        return new Row(rowIndex, fields);
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
     * @return column valuespublic
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
    public ColumnValue get(final int columnIndex) {
        return fields.get(columnIndex);
    }

    /**
     * Get the value at a given column index (zero based) converted to the given
     * type.
     * 
     * @param columnIndex column index
     * @param type        expected type
     * @param <T>         expected type
     * @return column value
     */
    public <T> T get(final int columnIndex, final Class<T> type) {
        return get(columnIndex).getValue(type);
    }

    @Override
    public String toString() {
        return "ResultSetRow [rowIndex=" + rowIndex + ", fields=" + fields + "]";
    }
}
