package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Converts a single row from a {@link ResultSet} to a generic row type.
 * 
 * @param <T> generic row type
 */
@FunctionalInterface
public interface RowMapper<T> {

    /**
     * Converts a single row from a {@link ResultSet} to a generic row type.
     * 
     * @param resultSet result set
     * @param rowNum    the current row number (zero based)
     * @return the converted row
     * @throws SQLException if accessing the result set fails
     */
    T mapRow(ResultSet resultSet, int rowNum) throws SQLException;
}
