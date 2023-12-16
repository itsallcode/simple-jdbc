package org.itsallcode.jdbc.dialect;

/**
 * Base class for implementing a {@link DbDialect}.
 */
public abstract class BaseDbDialect implements DbDialect {
    private final String jdbcUrlPrefix;

    /**
     * Create a new instance.
     * 
     * @param jdbcUrlPrefix the JDBC URL prefix supported by this dialect
     */
    protected BaseDbDialect(final String jdbcUrlPrefix) {
        this.jdbcUrlPrefix = jdbcUrlPrefix.toLowerCase();
    }

    @Override
    public boolean supportsUrl(final String jdbcUrl) {
        return jdbcUrl.toLowerCase().startsWith(jdbcUrlPrefix);
    }
}
