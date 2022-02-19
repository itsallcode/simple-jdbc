package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ValueExtractorFactory {
    public static ValueExtractorFactory create() {
        return new ValueExtractorFactory();
    }

    public ResultSetValueExtractor create(ColumnType type) {
        return (resultSet, columnIndex) -> new ColumnValue(type, getValue(type, resultSet, columnIndex));
    }

    private Object getValue(ColumnType type, ResultSet resultSet, int columnIndex) throws SQLException {
        return resultSet.getObject(columnIndex);
    }
}
