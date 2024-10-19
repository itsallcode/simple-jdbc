package org.itsallcode.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Instances of this class allow setting values for a {@link PreparedStatement}
 * for multiple rows.
 * 
 * @param <T> row type
 */
@FunctionalInterface
public interface RowPreparedStatementSetter<T> {
    /**
     * Set values for the given prepared statement.
     * 
     * 
     * @param row               the row for which to set values
     * @param preparedStatement the prepared statement
     * @throws SQLException if setting values fails
     */
    void setValues(T row, PreparedStatement preparedStatement) throws SQLException;
}
