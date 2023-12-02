package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;

class OriginalValueExtractorFactor implements ValueExtractorFactory {
    @Override
    public ResultSetValueExtractor create(final ColumnType type) {
        return (resultSet, columnIndex) -> new ColumnValue(type, getValue(resultSet, columnIndex));
    }

    private Object getValue(final ResultSet resultSet, final int columnIndex) throws SQLException {
        return resultSet.getObject(columnIndex);
    }
}