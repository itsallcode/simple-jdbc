package org.itsallcode.jdbc.update;

@FunctionalInterface
public interface RowMapper<T>
{
    Object[] map(T row);
}
