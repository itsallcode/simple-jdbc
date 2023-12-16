package org.itsallcode.jdbc.dialect;

import org.itsallcode.jdbc.resultset.generic.ColumnMetaData;

class ExasolDialect implements DbDialect {

    public ColumnValueExtractor createExtractor(final ColumnMetaData column) {
        return switch (column.type().jdbcType()) {
        case TIMESTAMP -> Extractors.timestampToUTCInstant();
        case CLOB -> Extractors.clobToString();
        case BLOB -> Extractors.blobToBytes();
        case DATE -> Extractors.dateToLocalDate();
        default -> Extractors.generic();
        };
    }
}
