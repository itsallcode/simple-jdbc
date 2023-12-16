package org.itsallcode.jdbc.dialect;

import org.itsallcode.jdbc.resultset.generic.SimpleMetaData.ColumnMetaData;

/**
 * A database specific dialect.
 */
public interface DbDialect {
    /**
     * Create an extractor for the given column.
     * 
     * @param column the column for which to get the extractor
     * @return extractor
     */
    ColumnValueExtractor createExtractor(final ColumnMetaData column);

    /**
     * Create a dialect for the Exasol database.
     * 
     * @return dialect for Exasol
     */
    public static DbDialect exasol() {
        return new ExasolDialect();
    }

    /**
     * Create a dialect for the H2 database.
     * 
     * @return dialect for H2
     */
    public static DbDialect h2() {
        return new H2Dialect();
    }
}
