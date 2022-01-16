package org.itsallcode.jdbc.update;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ParameterMapper
{
    private final Map<Class<?>, Mapper> mappers;

    private ParameterMapper(Map<Class<?>, Mapper> mappers)
    {
        this.mappers = mappers;
    }

    public static ParameterMapper create()
    {
        final Map<Class<?>, Mapper> mappers = new HashMap<>();
        mappers.put(LocalDate.class, Object::toString);
        return new ParameterMapper(mappers);
    }

    public Object map(Object value)
    {
        if (mappers.containsKey(value.getClass()))
        {
            return mappers.get(value.getClass()).map(value);
        }
        return value;
    }

    @FunctionalInterface
    private interface Mapper
    {
        Object map(Object value);
    }
}
