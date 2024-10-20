package org.itsallcode.jdbc.dialect;

import java.sql.*;
import java.util.Calendar;
import java.util.TimeZone;

final class Extractors {

    @SuppressWarnings({ "java:S2143", "java:S2885" }) // Need to use calendar api; using Calendar in a thread-safe way
    private static final Calendar UTC_CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    private Extractors() {
    }

    static ColumnValueExtractor timestampToUTCInstant() {
        return nonNull((resultSet, columnIndex) -> resultSet.getTimestamp(columnIndex, UTC_CALENDAR).toInstant());
    }

    public static ColumnValueExtractor timestampToInstant() {
        return nonNull((resultSet, columnIndex) -> resultSet.getTimestamp(columnIndex).toInstant());
    }

    static ColumnValueExtractor dateToLocalDate() {
        return nonNull((resultSet, columnIndex) -> resultSet.getDate(columnIndex, UTC_CALENDAR).toLocalDate());
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
