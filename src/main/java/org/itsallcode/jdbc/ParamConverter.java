package org.itsallcode.jdbc;

/**
 * This converts a domain object to an array of types supported by the database
 * when inserting rows.
 * 
 * @param <T> row type
 */
@FunctionalInterface
public interface ParamConverter<T> {
    /**
     * Converts a domain object to a row that can be inserted into the database.
     * 
     * @param row domain object to convert
     * @return converted row
     */
    Object[] map(T row);

    /**
     * Identity parameter convert that returns object arrays unchanged.
     * 
     * @return a new identity parameter converter
     */
    static ParamConverter<Object[]> identity() {
        return row -> row;
    }
}
