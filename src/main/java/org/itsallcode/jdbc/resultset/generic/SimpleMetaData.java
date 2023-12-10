package org.itsallcode.jdbc.resultset.generic;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.itsallcode.jdbc.Context;
import org.itsallcode.jdbc.UncheckedSQLException;

/**
 * A wrapper for {@link ResultSetMetaData} to simplify usage.
 */
public class SimpleMetaData {
    private final List<ColumnMetaData> columns;

    private SimpleMetaData(final List<ColumnMetaData> columns) {
        this.columns = columns;
    }

    static SimpleMetaData create(final ResultSetMetaData metaData, final Context context) {
        try {
            final List<ColumnMetaData> columns = createColumnMetaData(metaData, context);
            return new SimpleMetaData(columns);
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error extracting meta data", e);
        }
    }

    private static List<ColumnMetaData> createColumnMetaData(final ResultSetMetaData metaData, final Context context)
            throws SQLException {
        final ValueExtractorFactory valueExtractorFactory = context.getValueExtractorFactory();
        final int columnCount = metaData.getColumnCount();
        final List<ColumnMetaData> columns = new ArrayList<>(columnCount);
        for (int column = 1; column <= columnCount; column++) {
            final ColumnMetaData columnMetaData = ColumnMetaData.create(metaData,
                    valueExtractorFactory, column);
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
     * Represents the metadata of a single column.
     */
    public static class ColumnMetaData {
        private final int columnIndex;
        private final String name;
        private final String label;
        private final ColumnType type;
        private final ValueExtractorFactory valueExtractorFactory;

        private ColumnMetaData(final int columnIndex, final String name, final String label, final ColumnType type,
                final ValueExtractorFactory valueExtractorFactory) {
            this.columnIndex = columnIndex;
            this.name = name;
            this.label = label;
            this.type = type;
            this.valueExtractorFactory = valueExtractorFactory;
        }

        private static ColumnMetaData create(final ResultSetMetaData metaData,
                final ValueExtractorFactory valueExtractorFactory, final int columnIndex) throws SQLException {
            final String className = metaData.getColumnClassName(columnIndex);
            final int displaySize = metaData.getColumnDisplaySize(columnIndex);
            final String label = metaData.getColumnLabel(columnIndex);
            final String name = metaData.getColumnName(columnIndex);
            final int jdbcType = metaData.getColumnType(columnIndex);
            final String typeName = metaData.getColumnTypeName(columnIndex);
            final int precision = metaData.getPrecision(columnIndex);
            final int scale = metaData.getScale(columnIndex);
            final ColumnType columnType = new ColumnType(jdbcType, typeName, className, precision, scale, displaySize);
            return new ColumnMetaData(columnIndex, name, label, columnType, valueExtractorFactory);
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

        ResultSetValueExtractor getValueExtractor() {
            return valueExtractorFactory.create(this.type);
        }

        @Override
        public String toString() {
            return "ColumnMetaData [columnIndex=" + columnIndex + ", name=" + name + ", label=" + label + ", type="
                    + type + "]";
        }
    }
}
