package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;

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
     * Create a new factory that does not convert values but returns them as
     * returned by {@link ResultSet#getObject(int)}.
     * 
     * @return a new factory
     */
    public static ValueExtractorFactory create() {
        return new OriginalValueExtractorFactor();
    }

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
