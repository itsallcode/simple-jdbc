package org.itsallcode.jdbc.dialect;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Implementors of this interface optionally convert an object and set it on a
 * {@link PreparedStatement}.
 * 
 * @param <T> object type
 */
public interface ColumnValueSetter<T> {

    /**
     * Optionally convert an object and set it on a {@link PreparedStatement}.
     * 
     * @param stmt           prepared statement
     * @param parameterIndex parameter index
     * @param object         object to convert
     * @throws SQLException if setting fails
     */
    void setObject(PreparedStatement stmt, int parameterIndex, T object) throws SQLException;
}
