package org.itsallcode.jdbc.resultset;

import java.sql.*;
import java.util.Calendar;
import java.util.TimeZone;

class ModernValueExtractorFactor implements ValueExtractorFactory {
    private final Calendar utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    @Override
    public ResultSetValueExtractor create(final ColumnType type) {
        return (resultSet, columnIndex) -> new ColumnValue(type, getValue(type, resultSet, columnIndex));
    }

    private Object getValue(final ColumnType type, final ResultSet resultSet, final int columnIndex)
            throws SQLException {
        final Object object = resultSet.getObject(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        }
        switch (type.getJdbcType()) {
        case Types.TIMESTAMP:
            final Timestamp timestamp = resultSet.getTimestamp(columnIndex, utcCalendar);
            return timestamp.toInstant();
        case Types.DATE:
            return resultSet.getDate(columnIndex, utcCalendar).toLocalDate();
        default:
            return object;
        }
    }
}