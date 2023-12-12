package org.itsallcode.jdbc.resultset.generic;

/**
 * Represents the type of a column.
 */
public final class ColumnType {
    private final JdbcType jdbcType;
    private final String typeName;
    private final String className;
    private final int precision;
    private final int scale;
    private final int displaySize;

    ColumnType(final JdbcType jdbcType, final String typeName, final String className, final int precision,
            final int scale,
            final int displaySize) {
        this.jdbcType = jdbcType;
        this.typeName = typeName;
        this.className = className;
        this.precision = precision;
        this.scale = scale;
        this.displaySize = displaySize;
    }

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((jdbcType == null) ? 0 : jdbcType.hashCode());
        result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
        result = prime * result + ((className == null) ? 0 : className.hashCode());
        result = prime * result + precision;
        result = prime * result + scale;
        result = prime * result + displaySize;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ColumnType other = (ColumnType) obj;
        if (jdbcType != other.jdbcType) {
            return false;
        }
        if (typeName == null) {
            if (other.typeName != null) {
                return false;
            }
        } else if (!typeName.equals(other.typeName)) {
            return false;
        }
        if (className == null) {
            if (other.className != null) {
                return false;
            }
        } else if (!className.equals(other.className)) {
            return false;
        }
        if (precision != other.precision) {
            return false;
        }
        if (scale != other.scale) {
            return false;
        }
        if (displaySize != other.displaySize) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ColumnType [jdbcType=" + jdbcType + ", typeName=" + typeName + ", className=" + className
                + ", precision=" + precision + ", scale=" + scale + ", displaySize=" + displaySize + "]";
    }
}
