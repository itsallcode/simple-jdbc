package org.itsallcode.jdbc.dialect;

import java.time.LocalDate;
import java.time.LocalTime;

import org.itsallcode.jdbc.resultset.generic.SimpleMetaData.ColumnMetaData;

public class H2Dialect implements DbDialect {

    public Extractor createConverter(final ColumnMetaData column) {
        return switch (column.getType().getJdbcType()) {
        case TIMESTAMP -> Extractors.timestampToUTCInstant();
        case CLOB -> Extractors.clobToString();
        case BLOB -> Extractors.blobToBytes();
        case TIME -> Extractors.forType(LocalTime.class);
        case DATE -> Extractors.forType(LocalDate.class);
        default -> Extractors.generic();
        };
    }
}
