package org.itsallcode.jdbc.resultset;

/**
 * Represents a generic column value.
 */
public class ColumnValue {
    private final ColumnType type;
    private final Object value;

    ColumnValue(final ColumnType type, final Object value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Get the column type.
     * 
     * @return column type
     */
    public ColumnType getType() {
        return type;
    }

    /**
     * Get the column value.
     * 
     * @return column value
     */
    public Object getValue() {
        return value;
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
        return type.cast(value);
    }

    @Override
    public String toString() {
        return "ResultSetValue [type=" + type + ", value=" + value + "]";
    }
}
