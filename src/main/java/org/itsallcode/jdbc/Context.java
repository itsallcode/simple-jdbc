package org.itsallcode.jdbc;

import java.sql.ResultSet;
import java.util.Objects;

import org.itsallcode.jdbc.dialect.DbDialect;
import org.itsallcode.jdbc.resultset.ConvertingResultSet;

/**
 * This represents a context with configuration for the Simple JDBC framework.
 */
public class Context {

    private final DbDialect dialect;

    private Context(final ContextBuilder builder) {
        this.dialect = Objects.requireNonNull(builder.dialect, "dialect");
    }

    /**
     * Get the configured {@link ParameterMapper}.
     * 
     * @return parameter mapper
     */
    public ParameterMapper getParameterMapper() {
        return ParameterMapper.create();
    }

    /**
     * Wraps the given result set in a result set that converts values for the
     * current DB dialect.
     * 
     * @param resultSet the result set to wrap
     * @return the wrapped result set
     * @see ConvertingResultSet
     */
    public ResultSet convertingResultSet(final ResultSet resultSet) {
        return ConvertingResultSet.create(dialect, resultSet);
    }

    /**
     * Create a new builder for {@link Context} objects.
     * 
     * @return a new builder
     */
    public static ContextBuilder builder() {
        return new ContextBuilder();
    }

    /**
     * A builder for {@link Context} objects.
     */
    public static class ContextBuilder {

        private DbDialect dialect;

        private ContextBuilder() {
        }

        /**
         * Set the DB dialect for the new context.
         * 
         * @param dialect DB dialect
         * @return {@code this} for fluent programming
         */
        public ContextBuilder dialect(final DbDialect dialect) {
            this.dialect = dialect;
            return this;
        }

        /**
         * Build a new context.
         * 
         * @return a new context
         */
        public Context build() {
            return new Context(this);
        }
    }
}
