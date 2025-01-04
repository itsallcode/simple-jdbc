package org.itsallcode.jdbc.dialect;

import org.itsallcode.jdbc.resultset.generic.ColumnMetaData;

/**
 * A generic {@link DbDialect} without any special handling.
 */
public final class GenericDialect implements DbDialect {
    /** Singleton instance of the generic DB dialect. */
    public static final DbDialect INSTANCE = new GenericDialect();

    private GenericDialect() {
        // Nothing to do
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
