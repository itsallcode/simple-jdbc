package org.itsallcode.jdbc.resultset;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.itsallcode.jdbc.Context;
import org.itsallcode.jdbc.UncheckedSQLException;

public class SimpleMetaData
{
    private final List<ColumnMetaData> columns;

    private SimpleMetaData(List<ColumnMetaData> columns)
    {
        this.columns = columns;
    }

    static SimpleMetaData create(ResultSetMetaData metaData, Context context)
    {
        try
        {
            final int columnCount = metaData.getColumnCount();
            final List<ColumnMetaData> columns = new ArrayList<>(columnCount);
            for (int column = 1; column <= columnCount; column++)
            {
                final ColumnMetaData columnMetaData = ColumnMetaData.create(metaData,
                        context.getValueExtractorFactory(), column);
                columns.add(columnMetaData);
            }
            return new SimpleMetaData(columns);
        }
        catch (final SQLException e)
        {
            throw new UncheckedSQLException("Error extracting meta data", e);
        }
    }

    public List<ColumnMetaData> getColumns()
    {
        return columns;
    }

    public static class ColumnMetaData
    {
        private final int columnIndex;
        private final String name;
        private final String label;
        private final ColumnType type;
        private final ResultSetValueExtractor valueExtractor;

        private ColumnMetaData(int columnIndex, String name, String label, ColumnType type,
                ResultSetValueExtractor valueExtractor)
        {
            this.columnIndex = columnIndex;
            this.name = name;
            this.label = label;
            this.type = type;
            this.valueExtractor = valueExtractor;
        }

        private static ColumnMetaData create(ResultSetMetaData metaData,
                ValueExtractorFactory valueExtractorFactory, int columnIndex) throws SQLException
        {
            final String className = metaData.getColumnClassName(columnIndex);
            final int displaySize = metaData.getColumnDisplaySize(columnIndex);
            final String label = metaData.getColumnLabel(columnIndex);
            final String name = metaData.getColumnName(columnIndex);
            final int jdbcType = metaData.getColumnType(columnIndex);
            final String typeName = metaData.getColumnTypeName(columnIndex);
            final int precision = metaData.getPrecision(columnIndex);
            final int scale = metaData.getScale(columnIndex);
            final ColumnType columnType = new ColumnType(jdbcType, typeName, className, precision, scale, displaySize);
            final ResultSetValueExtractor valueExtractor = valueExtractorFactory.create(columnType);
            return new ColumnMetaData(columnIndex, name, label, columnType, valueExtractor);
        }

        public int getColumnIndex()
        {
            return columnIndex;
        }

        public String getName()
        {
            return name;
        }

        public String getLabel()
        {
            return label;
        }

        public ColumnType getType()
        {
            return type;
        }

        ResultSetValueExtractor getValueExtractor()
        {
            return valueExtractor;
        }

        @Override
        public String toString()
        {
            return "ColumnMetaData [columnIndex=" + columnIndex + ", name=" + name + ", label=" + label + ", type="
                    + type + "]";
        }
    }
}
