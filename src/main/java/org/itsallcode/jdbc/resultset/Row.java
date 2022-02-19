package org.itsallcode.jdbc.resultset;

import java.util.List;

public class Row {
    private final int rowIndex;
    private final List<ColumnValue> fields;

    Row(int rowIndex, List<ColumnValue> fields) {
        this.rowIndex = rowIndex;
        this.fields = fields;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public List<ColumnValue> getColumnValues() {
        return fields;
    }

    public ColumnValue getColumnValue(int columnIndex) {
        return fields.get(columnIndex);
    }

    @Override
    public String toString() {
        return "ResultSetRow [rowIndex=" + rowIndex + ", fields=" + fields + "]";
    }
}
