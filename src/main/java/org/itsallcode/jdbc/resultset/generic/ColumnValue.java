package org.itsallcode.jdbc.resultset.generic;

/**
 * Represents a generic column value.
 * 
 * @param type  column type
 * @param value column value
 */
public record ColumnValue(ColumnType type, Object value) {

    /**
     * Get the column value cast to the given type.
     * 
     * @param type expected type
     * @param <T>  result type
     * @return value of the given type
     */
    public <T> T getValue(final Class<T> type) {
        return cast(type);
    }

    /**
     * Get the column value as a string.
     * 
     * @return column value as string
     */
    public String getString() {
        return cast(String.class);
    }

    private <T> T cast(final Class<T> type) {
        return type.cast(this.value);
    }
}
