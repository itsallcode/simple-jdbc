package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.itsallcode.jdbc.Context;

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

    /**
     * Create a {@link RowMapper} that creates generic {@link Row} objects.
     * 
     * @param context the context
     * @return a new row mapper
     */
    static RowMapper<Row> createGenericRowMapper(final Context context) {
        return new GenericRowMapper(context);
    }

    /**
     * Create a {@link RowMapper} that creates {@link List}s of simple column
     * objects.
     * 
     * @param context the context
     * @return a new row mapper
     */
    static RowMapper<List<Object>> createListRowMapper(final Context context) {
        return new ListRowMapper(context);
    }
}
