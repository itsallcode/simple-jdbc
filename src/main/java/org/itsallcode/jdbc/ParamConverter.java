package org.itsallcode.jdbc;

@FunctionalInterface
public interface ParamConverter<T>
{
    Object[] map(T row);

    public static ParamConverter<Object[]> identity()
    {
        return row -> row;
    }
}
