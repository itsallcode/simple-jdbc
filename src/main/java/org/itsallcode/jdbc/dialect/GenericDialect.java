package org.itsallcode.jdbc.dialect;

import org.itsallcode.jdbc.resultset.generic.ColumnMetaData;

/**
 * A generic {@link DbDialect} without any special handling.
 */
public class GenericDialect implements DbDialect {
    /**
     * Create a new instance.
     */
    public GenericDialect() {
    }

    @Override
    public boolean supportsUrl(final String jdbcUrl) {
        return true;
    }

    @Override
    public ColumnValueExtractor createExtractor(final ColumnMetaData column) {
        return Extractors.generic();
    }

    @Override
    public <T> ColumnValueSetter<T> createSetter(final Class<T> type) {
        return Setters.generic();
    }
}
