package org.itsallcode.jdbc;

import org.itsallcode.jdbc.resultset.ValueExtractorFactory;

/**
 * This represents a context with configuration for the Simple JDBC framework.
 */
public class Context {

    private final boolean useModernTypes;

    private Context(final ContextBuilder builder) {
        this.useModernTypes = builder.useModernTypes;
    }

    /**
     * Get the configured {@link ValueExtractorFactory}.
     * 
     * @return value extractor factory
     */
    public ValueExtractorFactory getValueExtractorFactory() {
        if (useModernTypes) {
            return ValueExtractorFactory.createModernType();
        } else {
            return ValueExtractorFactory.create();
        }
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
        private boolean useModernTypes = false;

        private ContextBuilder() {
        }

        /**
         * Configure the context to convert legacy types returned by the result set to
         * modern types.
         * 
         * @param useModernTypes {@code true} to convert legacy types
         * @return {@code this} for fluent programming
         */
        public ContextBuilder useModernTypes(final boolean useModernTypes) {
            this.useModernTypes = useModernTypes;
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
