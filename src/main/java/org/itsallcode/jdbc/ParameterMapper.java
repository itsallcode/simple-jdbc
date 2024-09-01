package org.itsallcode.jdbc;

import static java.util.stream.Collectors.toMap;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

/**
 * This class converts parameters before setting them for a prepared statement,
 * e.g. in {@link PreparedStatementSetter}.
 */
public final class ParameterMapper {
    private final Map<String, Mapper<?>> mappers;

    private ParameterMapper(final Map<String, Mapper<?>> mappers) {
        this.mappers = new HashMap<>(mappers);
    }

    /**
     * Create a new mapper with predefined converters for date time types.
     * 
     * @return a pre-configured mapper
     */
    public static ParameterMapper create() {
        final List<Mapper<?>> mappers = new ArrayList<>();
        final DateTimeFormatter instantFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        final ZoneId utc = ZoneId.of("UTC");

        mappers.add(createMapper(LocalDate.class, Object::toString));
        mappers.add(createMapper(Instant.class,
                o -> instantFormatter.format(LocalDateTime.ofInstant(o, utc))));
        mappers.add(createMapper(LocalDateTime.class, instantFormatter::format));
        return new ParameterMapper(mappers.stream().collect(toMap(Mapper::getTypeName, Function.identity())));
    }

    private static <T> Mapper<T> createMapper(final Class<T> type, final Function<T, Object> mapper) {
        return new Mapper<>(type, mapper);
    }

    private Optional<Mapper<?>> getMapper(final Class<?> type) {
        return Optional.ofNullable(mappers.get(type.getName()));
    }

    /**
     * Converts a single value.
     * 
     * @param value value to convert
     * @return converted value
     */
    public Object map(final Object value) {
        if (value == null) {
            return null;
        }
        return getMapper(value.getClass())
                .map(m -> m.map(value))
                .orElse(value);
    }

    private static class Mapper<T> {
        private final Class<T> type;
        private final Function<T, Object> mapperFunction;

        Mapper(final Class<T> type, final Function<T, Object> mapper) {
            this.type = type;
            this.mapperFunction = mapper;
        }

        Object map(final Object value) {
            return mapperFunction.apply(type.cast(value));
        }

        String getTypeName() {
            return type.getName();
        }
    }
}
