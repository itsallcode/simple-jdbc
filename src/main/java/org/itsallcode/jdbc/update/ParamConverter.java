package org.itsallcode.jdbc.update;

@FunctionalInterface
public interface ParamConverter<T>
{
    Object[] map(T row);
}
