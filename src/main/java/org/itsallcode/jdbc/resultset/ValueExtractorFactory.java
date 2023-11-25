package org.itsallcode.jdbc.resultset;

/**
 * This factory creates {@link ResultSetValueExtractor} based on
 * {@link ColumnType}.
 */
public interface ValueExtractorFactory {

    /**
     * Create a new {@link ResultSetValueExtractor}.
     * 
     * @param type the column type
     * @return the new value extractor
     */
    ResultSetValueExtractor create(final ColumnType type);

    /**
     * Create a new factory.
     * 
     * @return a new factory
     */
    public static ValueExtractorFactory create() {
        return new ClassicValueExtractorFactor();
    }

    public static ValueExtractorFactory createModernType() {
        return new ModernValueExtractorFactor();
    }
}
