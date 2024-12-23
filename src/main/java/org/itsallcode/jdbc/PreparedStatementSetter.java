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
     * <p>
     * Only call {@link PreparedStatement#setObject(int, Object)} or similar
     * methods. Do not call {@link PreparedStatement#addBatch()}.
     * 
     * @param preparedStatement prepared statement
     * @throws SQLException if setting values fails
     */
    void setValues(PreparedStatement preparedStatement) throws SQLException;
}
