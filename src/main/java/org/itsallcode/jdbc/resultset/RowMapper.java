package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.itsallcode.jdbc.Context;
import org.itsallcode.jdbc.resultset.generic.*;
import org.itsallcode.jdbc.resultset.generic.GenericRowMapper.ColumnValuesConverter;

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
        return generic(row -> row.columnValues().stream().map(ColumnValue::value).toList());
    }

    private static <T> RowMapper<T> generic(final ColumnValuesConverter<T> converter) {
        return new GenericRowMapper<>(converter);
    }

    /**
     * Creates a new new {@link RowMapper} from a {@link SimpleRowMapper}.
     * <p>
     * Use this if the mapper doesn't need the {@link Context}.
     * 
     * @param <T>    generic row type
     * @param mapper the simple row mapper
     * @return a new {@link RowMapper}
     */
    public static <T> RowMapper<T> create(final SimpleRowMapper<T> mapper) {
        return (context, resultSet, rowNum) -> mapper.mapRow(resultSet, rowNum);
    }

    /**
     * Converts a single row from a {@link ResultSet} to a generic row type.
     * <p>
     * Use this interface with method {@link RowMapper#create(SimpleRowMapper)} if
     * you don't need the {@link Context}.
     * 
     * @param <T> generic row type
     */
    @FunctionalInterface
    public interface SimpleRowMapper<T> {
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

}
