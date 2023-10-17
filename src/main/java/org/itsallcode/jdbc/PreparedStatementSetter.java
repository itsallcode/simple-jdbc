package org.itsallcode.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Instances of this class allow setting values for a {@link PreparedStatement}.
 */
@FunctionalInterface
public interface PreparedStatementSetter {
    /**
     * Set values for the given prepared statement.
     * 
     * @param preparedStatement the prepared statement
     * @throws SQLException if setting values fails
     */
    void setValues(PreparedStatement preparedStatement) throws SQLException;
}
