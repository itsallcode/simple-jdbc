package org.itsallcode.jdbc.resultset.generic;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;

/**
 * A wrapper for {@link ResultSetMetaData} to simplify usage.
 * 
 * @param columns all column metadata for the result set
 */
public record SimpleMetaData(List<ColumnMetaData> columns) {

    /**
     * Create a new {@link SimpleMetaData} for a given {@link ResultSet}.
     * 
     * @param resultSet the result set
     * @return simple metadata
     */
    public static SimpleMetaData create(final ResultSet resultSet) {
        return new SimpleMetaData(ColumnMetaData.create(resultSet));
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
