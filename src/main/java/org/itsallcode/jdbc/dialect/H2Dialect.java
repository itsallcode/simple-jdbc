package org.itsallcode.jdbc.dialect;

import java.time.LocalDate;
import java.time.LocalTime;

import org.itsallcode.jdbc.resultset.generic.ColumnMetaData;

/**
 * DB dialect for the H2 database.
 */
public class H2Dialect extends BaseDbDialect {

    /**
     * Create a new instance.
     */
    public H2Dialect() {
        super("jdbc:h2:");
    }

    @Override
    public ColumnValueExtractor createExtractor(final ColumnMetaData column) {
        return switch (column.type().jdbcType()) {
        case TIMESTAMP -> Extractors.timestampToUTCInstant();
        case TIMESTAMP_WITH_TIMEZONE -> Extractors.offsetDateTime();
        case CLOB -> Extractors.clobToString();
        case BLOB -> Extractors.blobToBytes();
        case TIME -> Extractors.forType(LocalTime.class);
        case DATE -> Extractors.forType(LocalDate.class);
        default -> Extractors.generic();
        };
    }
}
