package org.itsallcode.jdbc.resultset.generic;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Extracts a column value from a {@link ResultSet}.
 */
@FunctionalInterface
public interface ResultSetValueExtractor {
    /**
     * Extracts a column value from a {@link ResultSet}.
     * 
     * @param resultSet   the result set
     * @param columnIndex column index (1 based)
     * @return the column value
     * @throws SQLException if reading the result set fails
     */
    ColumnValue extractValue(ResultSet resultSet, int columnIndex) throws SQLException;
}
