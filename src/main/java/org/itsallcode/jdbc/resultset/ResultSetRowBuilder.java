package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.itsallcode.jdbc.UncheckedSQLException;
import org.itsallcode.jdbc.resultset.SimpleMetaData.ColumnMetaData;

class ResultSetRowBuilder
{
    private final SimpleMetaData metadata;

    ResultSetRowBuilder(SimpleMetaData metaData)
    {
        this.metadata = metaData;
    }

    ResultSetRow buildRow(ResultSet resultSet, int rowIndex)
    {
        final List<ColumnMetaData> columns = metadata.getColumns();
        final List<ResultSetValue> fields = new ArrayList<>(columns.size());
        for (final ColumnMetaData column : columns)
        {
            final ResultSetValue field = getField(resultSet, column, rowIndex);
            fields.add(field);
        }
        return new ResultSetRow(rowIndex, fields);
    }

    private ResultSetValue getField(ResultSet resultSet, final ColumnMetaData column, int rowIndex)
    {
        ResultSetValue field;
        try
        {
            field = column.getValueExtractor().extractValue(resultSet, column.getColumnIndex());
        }
        catch (final SQLException e)
        {
            throw new UncheckedSQLException("Error extracting value for row " + rowIndex + " / column " + column,
                    e);
        }
        return field;
    }
}
