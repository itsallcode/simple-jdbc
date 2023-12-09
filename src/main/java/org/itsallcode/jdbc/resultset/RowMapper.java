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
        return generic(row -> row);
    }

    /**
     * Create a {@link RowMapper} that creates {@link List}s of simple column
     * objects.
     * 
     * @return a new row mapper
     */
    public static RowMapper<List<Object>> columnValueList() {
        return generic(row -> row.getColumnValues().stream().map(ColumnValue::getValue).toList());
    }

    private static <T> RowMapper<T> generic(final ColumnValuesConverter<T> converter) {
        return new GenericRowMapper<>(converter);
    }

    public static <T> RowMapper<T> create(final Simple<T> mapper) {
        return (context, resultSet, rowNum) -> mapper.mapRow(resultSet, rowNum);
    }

    @FunctionalInterface
    public interface Simple<T> {
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
