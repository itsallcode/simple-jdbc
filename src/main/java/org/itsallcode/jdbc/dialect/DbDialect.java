package org.itsallcode.jdbc.dialect;

import org.itsallcode.jdbc.resultset.generic.ColumnMetaData;

/**
 * A database specific dialect.
 */
public interface DbDialect {

    /**
     * Check if this dialect supports the database type with the given JDBC URL.
     * 
     * @param jdbcUrl JDBC URL
     * @return {@code true} if this dialect supports the database
     */
    boolean supportsUrl(String jdbcUrl);

    /**
     * Create an extractor for the given column.
     * 
     * @param column the column for which to get the extractor
     * @return extractor
     */
    ColumnValueExtractor createExtractor(final ColumnMetaData column);

    /**
     * Create a new column value setter for setting values on a
     * {@link java.sql.PreparedStatement} specific to the dialect.
     * 
     * @param <T>  object type
     * @param type object type
     * @return column value setter
     */
    <T> ColumnValueSetter<T> createSetter(Class<T> type);
}
