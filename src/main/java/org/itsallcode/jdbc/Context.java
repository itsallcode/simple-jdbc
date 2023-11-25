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

    public static ContextBuilder builder() {
        return new ContextBuilder();
    }

    public static class ContextBuilder {
        private boolean useModernTypes = false;

        private ContextBuilder() {
        }

        public ContextBuilder useModernTypes(final boolean useModernTypes) {
            this.useModernTypes = useModernTypes;
            return this;
        }

        public Context build() {
            return new Context(this);
        }
    }
}
