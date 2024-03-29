package org.itsallcode.jdbc;

/**
 * This represents a context with configuration for the Simple JDBC framework.
 */
public class Context {

    private Context() {
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

        private ContextBuilder() {
        }

        /**
         * Build a new context.
         * 
         * @return a new context
         */
        public Context build() {
            return new Context();
        }
    }
}
