package org.itsallcode.jdbc;

import org.itsallcode.jdbc.resultset.ValueExtractorFactory;

/**
 * This represents a context with configuration for the Simple JDBC framework.
 */
public class Context {
    /**
     * Get the configured {@link ValueExtractorFactory}.
     * 
     * @return value extractor factory
     */
    public ValueExtractorFactory getValueExtractorFactory() {
        return ValueExtractorFactory.create();
    }

    /**
     * Get the configured {@link ParameterMapper}.
     * 
     * @return parameter mapper
     */
    public ParameterMapper getParameterMapper() {
        return ParameterMapper.create();
    }
}
