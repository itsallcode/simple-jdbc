package org.itsallcode.jdbc.dialect;

import java.sql.PreparedStatement;
import java.time.*;
import java.time.format.DateTimeFormatter;

final class Setters {
    private Setters() {
    }

    static <T> ColumnValueSetter<T> generic() {
        return PreparedStatement::setObject;
    }

    static ColumnValueSetter<LocalDate> localDateToString() {
        return (final PreparedStatement stmt, final int parameterIndex, final LocalDate date) -> stmt
                .setString(parameterIndex, date.toString());
    }

    static ColumnValueSetter<Instant> instantToString(final DateTimeFormatter dateTimeFormatter,
            final ZoneId timeZone) {
        return (final PreparedStatement stmt, final int parameterIndex, final Instant instant) -> stmt
                .setString(parameterIndex, dateTimeFormatter.format(LocalDateTime.ofInstant(instant, timeZone)));
    }

    static ColumnValueSetter<LocalDateTime> localDateTimeToString(final DateTimeFormatter dateTimeFormatter) {
        return (final PreparedStatement stmt, final int parameterIndex, final LocalDateTime localDateTime) -> stmt
                .setString(parameterIndex, dateTimeFormatter.format(localDateTime));
    }
}
