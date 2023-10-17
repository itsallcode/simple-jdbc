package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This factory creates {@link ResultSetValueExtractor} based on
 * {@link ColumnType}.
 */
public class ValueExtractorFactory {

    private ValueExtractorFactory() {
        // Use static factory method
    }

    /**
     * Create a new factory.
     * 
     * @return a new factory
     */
    public static ValueExtractorFactory create() {
        return new ValueExtractorFactory();
    }

    /**
     * Create a new {@link ResultSetValueExtractor}.
     * 
     * @param type the column type
     * @return the new value extractor
     */
    public ResultSetValueExtractor create(final ColumnType type) {
        return (resultSet, columnIndex) -> new ColumnValue(type, getValue(resultSet, columnIndex));
    }

    private Object getValue(final ResultSet resultSet, final int columnIndex) throws SQLException {
        return resultSet.getObject(columnIndex);
    }
}
