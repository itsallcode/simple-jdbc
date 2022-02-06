package org.itsallcode.jdbc.update;

import static java.util.stream.Collectors.toMap;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ParameterMapper
{
    private final Map<Class<?>, Mapper<?>> mappers;

    private ParameterMapper(Map<Class<?>, Mapper<?>> mappers)
    {
        this.mappers = mappers;
    }

    public static ParameterMapper create()
    {
        final List<Mapper<?>> mappers = new ArrayList<>();
        final DateTimeFormatter instantFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        final ZoneId utc = ZoneId.of("UTC");

        mappers.add(createMapper(LocalDate.class, Object::toString));
        mappers.add(createMapper(Instant.class,
                o -> instantFormatter.format(LocalDateTime.ofInstant(o, utc))));
        mappers.add(createMapper(LocalDateTime.class, instantFormatter::format));
        return new ParameterMapper(mappers.stream().collect(toMap(Mapper::getType, Function.identity())));
    }

    private static <T> Mapper<T> createMapper(Class<T> type, Function<T, Object> mapper)
    {
        return new Mapper<>(type, mapper);
    }

    public Object map(Object value)
    {
        if (value == null)
        {
            return null;
        }
        if (mappers.containsKey(value.getClass()))
        {
            return mappers.get(value.getClass()).map(value);
        }
        return value;
    }

    private static class Mapper<T>
    {
        private final Class<T> type;
        private final Function<T, Object> mapperFunction;

        Mapper(Class<T> type, Function<T, Object> mapper)
        {
            this.type = type;
            this.mapperFunction = mapper;
        }

        Object map(Object value)
        {
            return mapperFunction.apply(type.cast(value));
        }

        Class<T> getType()
        {
            return type;
        }
    }
}
