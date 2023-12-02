package org.itsallcode.jdbc.resultset;

import java.sql.*;
import java.util.Calendar;
import java.util.TimeZone;

class ModernValueExtractorFactor implements ValueExtractorFactory {
    private final Calendar utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    private final ValueConverterFactory converterFactory = new ValueConverterFactory();

    @Override
    public ResultSetValueExtractor create(final ColumnType type) {
        return (resultSet, columnIndex) -> new ColumnValue(type, converterFactory.build(type),
                getValue(type, resultSet, columnIndex));
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
        case Types.TIME:
            final Time time = resultSet.getTime(columnIndex);
            return time.toLocalTime();
        case Types.DATE:
            return resultSet.getDate(columnIndex, utcCalendar).toLocalDate();
        case Types.CLOB:
            final Clob clob = resultSet.getClob(columnIndex);
            return clob.getSubString(1, (int) clob.length());
        case Types.BLOB:
            final Blob blob = resultSet.getBlob(columnIndex);
            return blob.getBytes(1, (int) blob.length());
        default:
            return object;
        }
    }
}