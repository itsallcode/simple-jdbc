package org.itsallcode.jdbc.dialect;

import java.sql.PreparedStatement;
import java.time.*;
import java.time.format.DateTimeFormatter;

public final class Setters {
    private Setters() {
    }

    public static <T> ColumnValueSetter<T> generic() {
        return PreparedStatement::setObject;
    }

    static ColumnValueSetter<LocalDate> localDateToString() {
        return (final PreparedStatement stmt, final int parameterIndex, final LocalDate date) -> stmt
                .setString(parameterIndex, date.toString());
    }

    public static ColumnValueSetter<Instant> instantToString(final DateTimeFormatter dateTimeFormatter,
            final ZoneId timeZone) {
        return (final PreparedStatement stmt, final int parameterIndex, final Instant instant) -> stmt
                .setString(parameterIndex, dateTimeFormatter.format(LocalDateTime.ofInstant(instant, timeZone)));
    }

    public static ColumnValueSetter<LocalDateTime> localDateTimeToString(final DateTimeFormatter dateTimeFormatter) {
        return (final PreparedStatement stmt, final int parameterIndex, final LocalDateTime localDateTime) -> stmt
                .setString(parameterIndex, dateTimeFormatter.format(localDateTime));
    }
}
