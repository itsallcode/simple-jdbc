package org.itsallcode.jdbc.dialect;

import java.time.*;
import java.time.format.DateTimeFormatter;

import org.itsallcode.jdbc.resultset.generic.ColumnMetaData;

/**
 * Dialect for the Exasol database.
 */
public class ExasolDialect extends AbstractDbDialect {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");

    /**
     * Create a new instance.
     */
    public ExasolDialect() {
        super("jdbc:exa:");
    }

    @Override
    public ColumnValueExtractor createExtractor(final ColumnMetaData column) {
        return switch (column.type().jdbcType()) {
        case TIMESTAMP -> Extractors.timestampToUTCInstant();
        case CLOB -> Extractors.clobToString();
        case BLOB -> Extractors.blobToBytes();
        case DATE -> Extractors.dateToLocalDate();
        default -> Extractors.generic();
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> ColumnValueSetter<T> createSetter(final Class<T> type) {
        if (type == LocalDate.class) {
            return (ColumnValueSetter<T>) Setters.localDateToString();
        }
        if (type == Instant.class) {
            return (ColumnValueSetter<T>) Setters.instantToString(DATE_TIME_FORMATTER, UTC_ZONE);
        }
        if (type == LocalDateTime.class) {
            return (ColumnValueSetter<T>) Setters.localDateTimeToString(DATE_TIME_FORMATTER);
        }
        return Setters.generic();
    }
}
