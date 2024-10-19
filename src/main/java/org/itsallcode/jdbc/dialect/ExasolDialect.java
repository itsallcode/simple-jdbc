package org.itsallcode.jdbc.dialect;

import org.itsallcode.jdbc.resultset.generic.ColumnMetaData;

/**
 * Dialect for the Exasol database.
 */
public class ExasolDialect extends AbstractDbDialect {

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
}
