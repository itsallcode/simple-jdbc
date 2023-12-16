package org.itsallcode.jdbc;

import static java.util.stream.Collectors.toList;

import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;

import org.itsallcode.jdbc.dialect.DbDialect;
import org.itsallcode.jdbc.resultset.*;
import org.itsallcode.jdbc.resultset.generic.SimpleMetaData;
import org.itsallcode.jdbc.resultset.generic.ValueExtractorFactory;

/**
 * This represents a context with configuration for the Simple JDBC framework.
 */
public class Context {

    private final DbDialect dialect;

    private Context(final ContextBuilder builder) {
        this.dialect = Objects.requireNonNull(builder.dialect, "dialect");
    }

    /**
     * Get the configured {@link ValueExtractorFactory}.
     * 
     * @return value extractor factory
     */
    public ValueExtractorFactory getValueExtractorFactory() {
        return ValueExtractorFactory.createModernType();
    }

    /**
     * Get the configured {@link ParameterMapper}.
     * 
     * @return parameter mapper
     */
    public ParameterMapper getParameterMapper() {
        return ParameterMapper.create();
    }

    ResultSet convertingResultSet(final ResultSet resultSet) {
        final SimpleMetaData metaData = SimpleMetaData.create(resultSet, this);
        final List<ColumnValueConverter> converters = metaData.getColumns().stream()
                .map(col -> ColumnValueConverter.simple(dialect.createConverter(col)))
                .collect(toList());
        return new ConvertingResultSet(resultSet, ResultSetValueConverter.create(metaData, converters));
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
