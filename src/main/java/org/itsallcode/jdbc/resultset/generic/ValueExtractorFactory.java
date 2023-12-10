package org.itsallcode.jdbc.resultset.generic;

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
     * Create a new factory that convert values as follows:
     * <ul>
     * <li>{@link java.sql.Timestamp} -> {@link java.time.Instant}</li>
     * <li>{@link java.sql.Date} -> {@link java.time.LocalDate}</li>
     * </ul>
     * 
     * @return a new factory
     */
    public static ValueExtractorFactory createModernType() {
        return new ModernValueExtractorFactor();
    }
}
