package org.itsallcode.jdbc.resultset;

/**
 * Represents the type of a column.
 */
public class ColumnType {
    private final int jdbcType;
    private final String typeName;
    private final String className;
    private final int precision;
    private final int scale;
    private final int displaySize;

    ColumnType(final int jdbcType, final String typeName, final String className, final int precision, final int scale,
            final int displaySize) {
        this.jdbcType = jdbcType;
        this.typeName = typeName;
        this.className = className;
        this.precision = precision;
        this.scale = scale;
        this.displaySize = displaySize;
    }

    /**
     * Get the JDBC type as defined in {@link java.sql.Types}.
     * 
     * @return JDBC type
     */
    public int getJdbcType() {
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

    @Override
    public String toString() {
        return "ColumnType [jdbcType=" + jdbcType + ", typeName=" + typeName + ", className=" + className
                + ", precision=" + precision + ", scale=" + scale + ", displaySize=" + displaySize + "]";
    }
}
