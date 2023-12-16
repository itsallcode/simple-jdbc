package org.itsallcode.jdbc.dialect;

import java.sql.*;
import java.util.Calendar;
import java.util.TimeZone;

class Extractors {

    private Extractors() {
    }

    static ColumnValueExtractor timestampToUTCInstant() {
        final Calendar utcCalendar = createUtcCalendar();
        return nonNull((resultSet, columnIndex) -> resultSet.getTimestamp(columnIndex, utcCalendar).toInstant());
    }

    static ColumnValueExtractor dateToLocalDate() {
        final Calendar utcCalendar = createUtcCalendar();
        return nonNull((resultSet, columnIndex) -> resultSet.getDate(columnIndex, utcCalendar).toLocalDate());
    }

    private static Calendar createUtcCalendar() {
        return Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    }

    private static ColumnValueExtractor nonNull(final ColumnValueExtractor extractor) {
        return (resultSet, columnIndex) -> {
            resultSet.getObject(columnIndex);
            if (resultSet.wasNull()) {
                return null;
            }
            return extractor.getObject(resultSet, columnIndex);
        };
    }

    static ColumnValueExtractor clobToString() {
        return nonNull((resultSet, columnIndex) -> {
            final Clob clob = resultSet.getClob(columnIndex);
            return clob.getSubString(1, (int) clob.length());
        });
    }

    static ColumnValueExtractor blobToBytes() {
        return nonNull((resultSet, columnIndex) -> {
            final Blob blob = resultSet.getBlob(columnIndex);
            return blob.getBytes(1, (int) blob.length());
        });
    }

    static ColumnValueExtractor forType(final Class<?> type) {
        return (resultSet, columnIndex) -> resultSet.getObject(columnIndex, type);
    }

    static ColumnValueExtractor generic() {
        return ResultSet::getObject;
    }
}
