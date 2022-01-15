package org.itsallcode.jdbc.resultset;

public class ResultSetValue
{
    private final ColumnType type;
    private final Object value;

    ResultSetValue(ColumnType type, Object value)
    {
        this.type = type;
        this.value = value;
    }

    public ColumnType getType()
    {
        return type;
    }

    public Object getValue()
    {
        return value;
    }

    public String getString()
    {
        return cast(String.class);
    }

    private <T> T cast(Class<T> type)
    {
        return type.cast(value);
    }
}
