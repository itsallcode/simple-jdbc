package org.itsallcode.jdbc.resultset;

import java.util.List;

public class ResultSetRow
{
    private final int rowIndex;
    private final List<ResultSetValue> fields;

    ResultSetRow(int rowIndex, List<ResultSetValue> fields)
    {
        this.rowIndex = rowIndex;
        this.fields = fields;
    }

    public int getRowIndex()
    {
        return rowIndex;
    }

    public List<ResultSetValue> getColumnValues()
    {
        return fields;
    }

    public ResultSetValue getColumnValue(int columnIndex)
    {
        return fields.get(columnIndex);
    }
}
