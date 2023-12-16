package org.itsallcode.jdbc.dialect;

import org.itsallcode.jdbc.resultset.generic.SimpleMetaData.ColumnMetaData;

public class ExasolDialect implements DbDialect {

    public Extractor createConverter(final ColumnMetaData column) {
        return switch (column.getType().getJdbcType()) {
        case TIMESTAMP -> Extractors.timestampToUTCInstant();
        case CLOB -> Extractors.clobToString();
        case BLOB -> Extractors.blobToBytes();
        case DATE -> Extractors.dateToLocalDate();
        default -> Extractors.generic();
        };
    }
}
