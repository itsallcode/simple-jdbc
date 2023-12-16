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

    /**
     * Create a new {@link SimpleMetaData} for a given {@link ResultSet}.
     * 
     * @param resultSet the result set
     * @return simple metadata
     */
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
}
