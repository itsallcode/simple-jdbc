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
     * @param context   database context
     * @param resultSet result set
     * @param rowNum    the current row number (zero based)
     * @return the converted row
     * @throws SQLException if accessing the result set fails
     */
    T mapRow(Context context, ResultSet resultSet, int rowNum) throws SQLException;

    /**
     * Create a {@link RowMapper} that creates generic {@link Row} objects.
     * 
     * @return a new row mapper
     */
    public static RowMapper<Row> generic() {
        return simple(row -> row);
    }

    /**
     * Create a {@link RowMapper} that creates {@link List}s of simple column
     * objects.
     * 
     * @return a new row mapper
     */
    public static RowMapper<List<Object>> columnValueList() {
        return simple(row -> row.getColumnValues().stream().map(ColumnValue::getValue).toList());
    }

    /**
     * Create a {@link RowMapper} that builds custom objects for each row.
     * 
     * @param converter row type converter
     * @param <T>       row type
     * @return a new row mapper
     */
    public static <T> RowMapper<T> simple(final ColumnValuesConverter<T> converter) {
        return new GenericRowMapper<>(converter);
    }

    /**
     * A simplified row mapper that gets a list of column values as input.
     */
    @FunctionalInterface
    public interface ColumnValuesConverter<T> {
        /**
         * Convert a single row.
         * 
         * @param row column values
         * @return converted object
         */
        T mapRow(Row row);
    }
}
