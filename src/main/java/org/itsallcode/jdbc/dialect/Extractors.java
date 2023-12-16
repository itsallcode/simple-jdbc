package org.itsallcode.jdbc.dialect;

import java.sql.*;
import java.util.Calendar;
import java.util.TimeZone;

public class Extractors {
    private static final Calendar UTC_CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    private Extractors() {
    }

    static Extractor timestampToUTCInstant() {
        return nonNull((resultSet, columnIndex) -> resultSet.getTimestamp(columnIndex, UTC_CALENDAR).toInstant());
    }

    private static Extractor nonNull(final Extractor extractor) {
        return (resultSet, columnIndex) -> {
            resultSet.getObject(columnIndex);
            if (resultSet.wasNull()) {
                return null;
            }
            return extractor.getObject(resultSet, columnIndex);
        };
    }

    static Extractor clobToString() {
        return nonNull((resultSet, columnIndex) -> {
            final Clob clob = resultSet.getClob(columnIndex);
            return clob.getSubString(1, (int) clob.length());
        });
    }

    static Extractor blobToBytes() {
        return nonNull((resultSet, columnIndex) -> {
            final Blob blob = resultSet.getBlob(columnIndex);
            return blob.getBytes(1, (int) blob.length());
        });
    }

    static Extractor forType(final Class<?> type) {
        return (resultSet, columnIndex) -> resultSet.getObject(columnIndex, type);
    }

    static Extractor generic() {
        return ResultSet::getObject;
    }
}
