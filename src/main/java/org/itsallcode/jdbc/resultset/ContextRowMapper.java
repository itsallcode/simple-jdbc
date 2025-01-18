package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.itsallcode.jdbc.Context;
import org.itsallcode.jdbc.dialect.DbDialect;
import org.itsallcode.jdbc.resultset.generic.*;
import org.itsallcode.jdbc.resultset.generic.GenericRowMapper.ColumnValuesConverter;

/**
 * Converts a single row from a {@link ResultSet} to a generic row type.
 * 
 * @param <T> generic row type
 */
@FunctionalInterface
public interface ContextRowMapper<T> {

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
     * @param dialect DB dialect
     * @return a new row mapper
     */
    static RowMapper<Row> generic(final DbDialect dialect) {
        return generic(dialect, row -> row);
    }

    /**
     * Create a {@link RowMapper} that creates {@link List}s of simple column
     * objects.
     * 
     * @param dialect DB dialect
     * @return a new row mapper
     */
    static RowMapper<List<Object>> columnValueList(final DbDialect dialect) {
        return generic(dialect, row -> row.columnValues().stream().map(ColumnValue::value).toList());
    }

    private static <T> RowMapper<T> generic(final DbDialect dialect, final ColumnValuesConverter<T> converter) {
        return new GenericRowMapper<>(dialect, converter);
    }

    /**
     * Creates a new new {@link ContextRowMapper} from a {@link SimpleRowMapper}.
     * <p>
     * Use this if the mapper doesn't need the {@link Context}.
     * 
     * @param <T>    generic row type
     * @param mapper the simple row mapper
     * @return a new {@link ContextRowMapper}
     */
    static <T> ContextRowMapper<T> create(final SimpleRowMapper<T> mapper) {
        return (context, resultSet, rowNum) -> mapper.mapRow(resultSet);
    }

    /**
     * Creates a new new {@link ContextRowMapper} from a {@link RowMapper}.
     * <p>
     * Use this if the mapper doesn't need the {@link Context}.
     * 
     * @param <T>    generic row type
     * @param mapper the simple row mapper
     * @return a new {@link ContextRowMapper}
     */
    static <T> ContextRowMapper<T> create(final RowMapper<T> mapper) {
        return (context, resultSet, rowNum) -> mapper.mapRow(resultSet, rowNum);
    }
}
