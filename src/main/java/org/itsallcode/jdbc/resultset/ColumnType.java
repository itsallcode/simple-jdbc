package org.itsallcode.jdbc.resultset;

public class ColumnType
{
    private final int jdbcType;
    private final String typeName;
    private final String className;
    private final int precision;
    private final int scale;
    private final int displaySize;

    ColumnType(int jdbcType, String typeName, String className, int precision, int scale, int displaySize)
    {
        this.jdbcType = jdbcType;
        this.typeName = typeName;
        this.className = className;
        this.precision = precision;
        this.scale = scale;
        this.displaySize = displaySize;
    }

    public int getJdbcType()
    {
        return jdbcType;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public String getClassName()
    {
        return className;
    }

    public int getPrecision()
    {
        return precision;
    }

    public int getScale()
    {
        return scale;
    }

    public int getDisplaySize()
    {
        return displaySize;
    }

    @Override
    public String toString()
    {
        return "ColumnType [jdbcType=" + jdbcType + ", typeName=" + typeName + ", className=" + className
                + ", precision=" + precision + ", scale=" + scale + ", displaySize=" + displaySize + "]";
    }
}
