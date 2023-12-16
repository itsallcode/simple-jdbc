package org.itsallcode.jdbc.resultset.generic;

import java.sql.*;
import java.util.Calendar;
import java.util.TimeZone;

import org.itsallcode.jdbc.dialect.Extractor;

class ModernValueExtractorFactor implements ValueExtractorFactory {
    private final Calendar utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    @Override
    public Extractor create(final ColumnType type) {
        return (resultSet, columnIndex) -> new ColumnValue(type, getValue(type, resultSet, columnIndex));
    }

    private Object getValue(final ColumnType type, final ResultSet resultSet, final int columnIndex)
            throws SQLException {
        final Object object = resultSet.getObject(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        }
        switch (type.getJdbcType()) {
        case TIMESTAMP:
            final Timestamp timestamp = resultSet.getTimestamp(columnIndex, utcCalendar);
            return timestamp.toInstant();
        case TIME:
            final Time time = resultSet.getTime(columnIndex);
            return time.toLocalTime();
        case DATE:
            return resultSet.getDate(columnIndex, utcCalendar).toLocalDate();
        case CLOB:
            final Clob clob = resultSet.getClob(columnIndex);
            return clob.getSubString(1, (int) clob.length());
        case BLOB:
            final Blob blob = resultSet.getBlob(columnIndex);
            return blob.getBytes(1, (int) blob.length());
        default:
            return object;
        }
    }
}