package org.itsallcode.jdbc.resultset.generic;

/**
 * Represents the type of a column.
 */
public record ColumnType(JdbcType jdbcType, String typeName, String className, int precision, int scale,
        int displaySize) {

    /**
     * Get the JDBC type.
     * 
     * @return JDBC type
     */
    public JdbcType getJdbcType() {
        return jdbcType;
    }

    /**
     * Get the database specific column type name.
     * 
     * @return type name
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * The fully qualified class name of the objects returned by
     * {@link ColumnValue#getValue()}.
     * 
     * @return class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Get the column precision.
     * 
     * @return precision
     */
    public int getPrecision() {
        return precision;
    }

    /**
     * Get the column scale.
     * 
     * @return scale
     */
    public int getScale() {
        return scale;
    }

    /**
     * Get the display size.
     * 
     * @return display size
     */
    public int getDisplaySize() {
        return displaySize;
    }
}
