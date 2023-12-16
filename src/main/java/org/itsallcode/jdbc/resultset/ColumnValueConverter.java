package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.itsallcode.jdbc.dialect.ColumnValueExtractor;

interface ColumnValueConverter {

    <T> T getObject(ResultSet resultSet, int columnIndex, Class<T> type) throws SQLException;

    Object getObject(ResultSet resultSet, int columnIndex) throws SQLException;

    static ColumnValueConverter simple(final ColumnValueExtractor extractor) {
        return new ColumnValueConverter() {
            @Override
            public <T> T getObject(final ResultSet resultSet, final int columnIndex, final Class<T> type)
                    throws SQLException {
                return type.cast(getObject(resultSet, columnIndex));
            }

            @Override
            public Object getObject(final ResultSet resultSet, final int columnIndex) throws SQLException {
                return extractor.getObject(resultSet, columnIndex);
            }
        };
    }
}
