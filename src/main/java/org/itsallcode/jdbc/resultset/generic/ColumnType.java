package org.itsallcode.jdbc.resultset.generic;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Represents the type of a column.
 * 
 * @param jdbcType    JDBC type
 * @param typeName    database specific column type name
 * @param className   fully qualified class name of the objects returned by
 *                    {@link ColumnValue#value()}
 * @param precision   column precision
 * @param scale       column scale
 * @param displaySize display size
 */
public record ColumnType(JdbcType jdbcType, String typeName, String className, int precision, int scale,
        int displaySize) {

    static ColumnType create(final ResultSetMetaData metaData, final int columnIndex) throws SQLException {
        final String className = metaData.getColumnClassName(columnIndex);
        final int displaySize = metaData.getColumnDisplaySize(columnIndex);
        final JdbcType jdbcType = JdbcType.forType(metaData.getColumnType(columnIndex));
        final String typeName = metaData.getColumnTypeName(columnIndex);
        final int precision = metaData.getPrecision(columnIndex);
        final int scale = metaData.getScale(columnIndex);
        return new ColumnType(jdbcType, typeName, className, precision, scale, displaySize);
    }
}
