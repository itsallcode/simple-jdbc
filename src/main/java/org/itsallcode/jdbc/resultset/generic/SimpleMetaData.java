package org.itsallcode.jdbc.resultset.generic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.itsallcode.jdbc.UncheckedSQLException;

/**
 * A wrapper for {@link ResultSetMetaData} to simplify usage.
 */
public class SimpleMetaData {
    private final List<ColumnMetaData> columns;

    private SimpleMetaData(final List<ColumnMetaData> columns) {
        this.columns = columns;
    }

    public static SimpleMetaData create(final ResultSet resultSet) {
        return create(getMetaData(resultSet));
    }

    private static ResultSetMetaData getMetaData(final ResultSet resultSet) {
        try {
            return resultSet.getMetaData();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error getting meta data", e);
        }
    }

    private static SimpleMetaData create(final ResultSetMetaData metaData) {
        try {
            final List<ColumnMetaData> columns = createColumnMetaData(metaData);
            return new SimpleMetaData(columns);
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error extracting meta data", e);
        }
    }

    private static List<ColumnMetaData> createColumnMetaData(final ResultSetMetaData metaData)
            throws SQLException {
        final int columnCount = metaData.getColumnCount();
        final List<ColumnMetaData> columns = new ArrayList<>(columnCount);
        for (int column = 1; column <= columnCount; column++) {
            final ColumnMetaData columnMetaData = ColumnMetaData.create(metaData,
                    column);
            columns.add(columnMetaData);
        }
        return columns;
    }

    /**
     * Get all column metadata for the result set.
     * 
     * @return column metadata
     */
    public List<ColumnMetaData> getColumns() {
        return columns;
    }

    /**
     * Get column metadata for a given index (one based).
     * 
     * @param index column index (one based)
     * @return column metadata
     */
    public ColumnMetaData getColumnByIndex(final int index) {
        return columns.get(index - 1);
    }

    /**
     * Represents the metadata of a single column.
     */
    public static class ColumnMetaData {
        private final int columnIndex;
        private final String name;
        private final String label;
        private final ColumnType type;

        private ColumnMetaData(final int columnIndex, final String name, final String label, final ColumnType type) {
            this.columnIndex = columnIndex;
            this.name = name;
            this.label = label;
            this.type = type;
        }

        private static ColumnMetaData create(final ResultSetMetaData metaData, final int columnIndex)
                throws SQLException {
            final String className = metaData.getColumnClassName(columnIndex);
            final int displaySize = metaData.getColumnDisplaySize(columnIndex);
            final String label = metaData.getColumnLabel(columnIndex);
            final String name = metaData.getColumnName(columnIndex);
            final JdbcType jdbcType = JdbcType.forType(metaData.getColumnType(columnIndex));
            final String typeName = metaData.getColumnTypeName(columnIndex);
            final int precision = metaData.getPrecision(columnIndex);
            final int scale = metaData.getScale(columnIndex);
            final ColumnType columnType = new ColumnType(jdbcType, typeName, className, precision, scale, displaySize);
            return new ColumnMetaData(columnIndex, name, label, columnType);
        }

        /**
         * Get the column index (1 based).
         * 
         * @return column index (1 based)
         */
        public int getColumnIndex() {
            return columnIndex;
        }

        /**
         * Get the column name.
         * 
         * @return column name
         */
        public String getName() {
            return name;
        }

        /**
         * Get the column label.
         * 
         * @return column label
         */
        public String getLabel() {
            return label;
        }

        /**
         * Get the SQL type.
         * 
         * @return SQL type
         */
        public ColumnType getType() {
            return type;
        }

        @Override
        public String toString() {
            return "ColumnMetaData [columnIndex=" + columnIndex + ", name=" + name + ", label=" + label + ", type="
                    + type + "]";
        }
    }
}
