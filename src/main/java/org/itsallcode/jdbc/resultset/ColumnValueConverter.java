package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.itsallcode.jdbc.dialect.Extractor;

@FunctionalInterface
public interface ColumnValueConverter {

    <T> T getObject(ResultSet resultSet, int columnIndex, Class<T> type) throws SQLException;

    static ColumnValueConverter generic() {
        return simple(ResultSet::getObject);
    }

    static ColumnValueConverter simple(final Extractor extractor) {
        return new ColumnValueConverter() {
            @Override
            public <T> T getObject(final ResultSet resultSet, final int columnIndex, final Class<T> type)
                    throws SQLException {
                return type.cast(extractor.getObject(resultSet, columnIndex));
            }
        };
    }
}
